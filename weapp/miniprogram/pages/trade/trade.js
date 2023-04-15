// pages/trade/trade.js

import request from '../../api/request.js';
import api from '../../api/api.js';
import cos from '../../api/cos.js';

const util = require('../../utils/util.js');
const app = getApp();

const tradeTypeMap = {
    'buy': 0,
    'exchange': 1,
};

Page({

    /**
     * 页面的初始数据
     */
    data: {
        cid: '',        // 商品ID
        location: {
            title: '',
            address: '',
            lat: 0.0,
            lng: 0.0
        },
        date: '',
        timePicker: {
            minDate: '',
            maxDate: '',
        },
        timeFormatter: undefined,
        showTimePicker: false,
        currentTime: 0,
        formattedTime: '',
        activeTab: 'buy',
        price: '',
        exchangeDesc: '请输入用户交换的物品描述信息',
        exchangeImage: [],
        commodity: {},
        exchange: true,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            cid: options.cid,
            activeTab: options.type
        });
        this.initTime();
        this.getCommoditySimpleInfo();
    },

    onReady: function() {
        // 先将textarea的值设为非空值，在渲染完成后再置为空
        // 以处理textarea初次加载时无法正常加载样式的问题
        this.setData({
            exchangeDesc: ''
        });
    },

    // 初始化时间相关数据
    initTime() {
        const current = new Date();
        const afterThreeMonths = new Date(current);
        afterThreeMonths.setMonth(current.getMonth() + 3);
        this.setData({
            timePicker: {
                minDate: current.getTime(),
                maxDate: afterThreeMonths.getTime(),
            },
            value: current.getTime(),
            timeFormatter: function(type, value) {
                if (type === 'year') {
                    return `${value}年`;
                }
                if (type === 'month') {
                    return `${value}月`;
                }
                if (type === 'day') {
                    return `${value}日`;
                }
                if (type === 'hour') {
                    return `${value}时`;
                }
                if (type === 'minute') {
                    return `${value}分`;
                }
                return value;
            }
        })
    },

    // 选择交易地点
    chooseLocation() {
        const _this = this;
        wx.navigateTo({
            url: '/pages/location/location',
            events: {
                locationChangeEvent: function(location) {
                    _this.setData({
                        location: location
                    });
                }
            }
        });
    },

    // 选择交易时间
    chooseTime() {
        this.setData({
            showTimePicker: true
        });
    },
    
    // 响应选择后
    onTimeChosen(event) {
        const timestamp = event.detail;
        const current = new Date(timestamp);
        const formattedTime = util.formatTimeUntilMinute(current);
        this.setData({
            showTimePicker: false,
            currentTime: current.getTime(),
            formattedTime: formattedTime
        })
    },

    // 处理上传的交换物品图片
    readImage(event) {
        console.log(event);
        const _imageList = this.data.exchangeImage;
        const files = event.detail.file;
        _imageList.push(files[0]);
        this.setData({
            exchangeImage: _imageList
        });
    },

    // 响应删除图片的逻辑
    handleDeleteImage() {
        // 由于本页面设置只能上传一张图片，因此只需清空图片列表即可
        this.setData({
            exchangeImage: []
        });
    },

    // 响应提交订单的逻辑
    onConfirm() {
        if (!this.checkInfo()) {
            return;
        }
        if (this.data.activeTab === 'buy') {
            this.onBuyConfirm();
        } else if (this.data.activeTab === 'exchange') {
            this.onExchangeConfirm();
        }
        
    },

    // 响应提交购买单的逻辑
    onBuyConfirm() {
        const _this = this;
        wx.showLoading({
            title: '提交订单中'
        });
        const data = {
            cid: _this.data.cid,
            tradeTime: _this.data.formattedTime,
            latitude: _this.data.location.lat,
            longitude: _this.data.location.lng,
            location: _this.data.location.title,
            address: _this.data.location.address,
            type: tradeTypeMap['buy'],
            price: _this.data.price,
            description: null,
            image: null,
        };
        request.post(api.order.create, data)
        .then(() => {
            wx.hideLoading();
            _this.showToast('订单建立成功');
            wx.navigateBack({
                delta: 1
            });
        })
        .catch((err) => {
            wx.hideLoading();
            _this.handleNetworkError(err);
        });
    },

    // 响应提交交换单的逻辑
    async onExchangeConfirm() {
        const _this = this;
        wx.showLoading({
            title: '提交订单中'
        });
        const _imageList = _this.data.exchangeImage;
        let imagePath;
        if (_imageList.length === 0) {
            imagePath = '';
        } else {
            const image = _this.data.exchangeImage[0];
            imagePath = await _this.uploadToCos(image);
        }
        const data = {
            cid: _this.data.cid,
            tradeTime: _this.data.formattedTime,
            latitude: _this.data.location.lat,
            longitude: _this.data.location.lng,
            location: _this.data.location.title,
            address: _this.data.location.address,
            type: tradeTypeMap['exchange'],
            price: null,
            description: _this.data.exchangeDesc,
            image: imagePath,
        };
        request.post(api.order.create, data)
        .then(() => {
            wx.hideLoading();
            _this.showToast('订单建立成功');
            wx.navigateBack({
                delta: 1
            });
        })
        .catch(err => {
            wx.hideLoading();
            _this.handleNetworkError(err);
        });
    },

    async uploadToCos(image) {
        const filepath = image.url;
        const extension = filepath.substr(filepath.lastIndexOf('.'));
        const res = await cos.uploadImage(filepath, extension);
        const imagePath = 'https://' + res.Location;
        return imagePath;
    },

    // 校验信息是否填写完全
    checkInfo() {
        if (this.data.formattedTime === '') {
            this.showToast('请选择交易时间');
            return false;
        }
        if (this.data.location.title === '') {
            this.showToast('请选择交易地点');
            return false;
        }
        if (this.data.activeTab === 'buy') {
            if (this.data.price === '') {
                this.showToast('请输入价格');
                return false;
            }
            if (isNaN(this.data.price)) {
                this.showToast('请输入数字作为价格');
                return false;
            }
        } else if (this.data.activeTab === 'exchange') {
            if (this.data.exchangeDesc === '') {
                this.showToast('请输入交换物描述信息');
                return false;
            }
        }
        return true;
    },

    // 获取商品的简要信息
    getCommoditySimpleInfo() {
        const _this = this;
        const url = api.commodity.tradeInfo + '/' + this.data.cid;
        request.get(url)
        .then((res) => {
            const data = res.data.data;
            _this.setData({
                commodity: {
                    image: data.image,
                    desc: data.description,
                },
                exchange: data.exchange,
            });
            // 若不支持交换，需要重绘van-tabs组件，否则底部条位置会错误
            if (data.exchange === false) {
                _this.selectComponent('#van-tabs').resize();
            }
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        });
    },

    showToast(msg) {
        wx.showToast({
            title: msg,
            icon: 'none',
            duration: 1500,
            mask: false
        });
    },

    // 响应网络错误
    handleNetworkError(err) {
        console.log(err);
        if (err.data && err.data.code === 401) {
            this.showToast('登录权限过期，请重新登录');
            app.toLogin();
            return;
        }
        if (err.statusCode !== 200) {
            this.showToast('网络错误，错误码: ' + err.statusCode);
            return;
        }
        if (err.data.msg) {
            this.showToast('网络错误: ' + err.data.msg);
            return;
        }
    },
})