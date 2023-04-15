// pages/sell/sell.js

import { commodity } from "../../api/api.js";
import request from "../../api/request.js"
import cos from "../../api/cos.js"

const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        location: {
            title: '',
            address: '',
            lat: 0.0,
            lng: 0.0,
        },
        price: '',
        imageList: [],
        description: '',
        exchange: false,
        edit: undefined,
        locationHaveBeenSet: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        if (options.edit) {
            this.setData({
                edit: options.edit,
                locationHaveBeenSet: true
            });
            this.renderCommodityInfoBeforeEdit();
        }
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        wx.setNavigationBarTitle({
            title: this.data.edit ? '编辑商品' : '发布商品',
        });
    },

    // 跳转到位置选择界面
    chooseLocation() {
        const _this = this;
        wx.navigateTo({
            url: '/pages/location/location',
            events: {
                locationChangeEvent: function(location) {
                    _this.setData({
                        location: location,
                        locationHaveBeenSet: true,
                    });
                }
            }
        });
    },

    // 图片加载完成后的逻辑
    afterRead(event) {
        console.log(event);
        const _imageList = this.data.imageList;
        const files = event.detail.file;
        for (let i = 0; i < files.length; i++) {
            _imageList.push(files[i]);
        }
        this.setData({
            imageList: _imageList
        });
    },

    // 删除图片
    deleteImage(event) {
        const index = event.detail.index;
        const _imageList = this.data.imageList;
        _imageList.splice(index, 1);
        this.setData({
            imageList: _imageList
        });
    },

    // 提交信息
    async onSubmit() {
        const _this = this;
        if (_this.data.description === '') {
            wx.showToast({
                title: '请输入商品描述信息',
                icon: 'none'
            });
            return;
        }
        if (_this.data.price === '') {
            wx.showToast({
                title: '请设置预期价格',
                icon: 'none'
            });
            return;
        }
        if (!_this.data.locationHaveBeenSet) {
            wx.showToast({
                title: '请设置交易地点',
                icon: 'none'
            });
            return;
        }
        wx.showLoading({
            title: '正在发布中'
        });
        // 将图片上传至COS服务
        const imagePaths = await this.uploadImage();
        const data = {
            description: this.data.description,
            images: imagePaths,
            exchange: this.data.exchange,
            price: this.data.price,
            latitude: this.data.location.lat,
            longitude: this.data.location.lng,
            location: this.data.location.title,
            address: this.data.location.address,
        };
        console.log(data);
        let url = commodity.sell;
        if (this.data.edit) {
            url = commodity.edit + '/' + this.data.edit;
        }
        // 再将数据上传到服务器
        request.post(url, data)
        .then(() => {
            wx.showToast({
                title: '发布成功',
                icon: 'none'
            });
            wx.hideLoading();
            _this.eventToPrevPage();
            wx.navigateBack({
                delta: 1
            });
        })
        .catch((err) => {
            wx.showToast({
                title: '发布失败',
                icon: 'none'
            });
            wx.hideLoading();
            console.log("发布失败", err);
        });
    },

    // 设置复选框的状态
    onCheckboxChange(event) {
        this.setData({
            exchange: event.detail,
        });
    },

    noop() {},

    toggle() {
        const checkbox = this.selectComponent(`.checkbox`);
        checkbox.toggle();
    },

    async uploadImage() {
        const _this = this;
        const _imageList = _this.data.imageList;
        const pathList = [];
        for (let i = 0; i < _imageList.length; i++) {
            const imagePath = await _this.uploadToCos(_imageList[i]);
            pathList.push(imagePath);
        }
        return pathList;
    },

    async uploadToCos(image) {
        const filepath = image.url;
        // 若当前路径已经是云端路径，则无需再上传到COS服务器
        if (filepath.startsWith("https://")) {
            return filepath;
        }
        const extension = filepath.substr(filepath.lastIndexOf('.'));
        const res = await cos.uploadImage(filepath, extension);
        const imagePath = 'https://' + res.Location;
        return imagePath;
    },

    // 将商品编辑前的数据渲染到编辑框中
    renderCommodityInfoBeforeEdit() {
        const _this = this;
        const detailUrl = commodity.detail + '/' + _this.data.edit;
        request.get(detailUrl)
        .then((res) => {
            const data = res.data.data;
            console.log("获取到编辑前的商品信息: ", data);
            _this.setData({
                description: data.description,
                exchange: data.exchange,
                location: {
                    title: data.location,
                    address: data.address,
                    lat: data.latitude,
                    lng: data.longitude,
                },
                price: data.price,
            });
            if (data.images) {
                const _imageList = [];
                for (let i = 0; i < data.images.length; i++) {
                    if (data.images[i] !== "") {
                        _imageList.push({
                            url: data.images[i]
                        });
                    }
                }
                _this.setData({
                    imageList: _imageList
                });
            }
        })
        .catch((err) => {
            if (err.data && err.data.code && err.data.code === 401) {
                app.toLogin();
            }
            console.log("获取编辑前商品信息失败", err);
            wx.showToast({
                title: '获取商品信息失败，请稍后重试',
                icon: 'none',
                duration: 1500,
                success: () => {
                    wx.navigateBack({
                        delta: 1
                    });
                },
            });
        });
    },

    // 若为编辑界面，则回退时发送事件给前一界面进行页面刷新
    eventToPrevPage() {
        if (this.data.edit) {
            const eventChannel = this.getOpenerEventChannel();
            eventChannel.emit('commodityEditEvent');
        }
    },


})