// pages/commodity/commodity.js

import request from '../../api/request.js';
import { commodity, collection } from '../../api/api.js';

const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: undefined,
        sellerId: '',
        avatar: '',
        name: '',
        time: '',
        detail: '',
        segments: [],
        imageList: [],
        isSeller: false,
        location: undefined,
        couldExchange: false,
        price: '',
        offShelf: true,
        marker: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log(options);
        const cid = options.id;
        this.setData({
            id: cid
        });
        this.getDetail(cid);
    },

    onPullDownRefresh: function() {
        this.getDetail(this.data.id);
        wx.stopPullDownRefresh();
    },

    setMockData() {
        if (this.data.id === '00000001') {
            const detail = '为了测试 这里加一段相对比较长的描述性文字，用来测试是否会出现超过两行'
                + '\n换行测试\n并测试段落间距' + '';
            const images = [];
            images.push('https://img.yzcdn.cn/vant/cat.jpeg');
            this.setData({
                avatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                name: '卖家',
                time: '2021/11/18 12:20',
                detail: detail,
                imageList: images
            });
            console.log(this.data.imageList);
        }
    },

    /**
     * 将详情字符串通过换行符分割为段落
     */
    detailToSegment() {
        console.log(this.data.detail);
        const segments = this.data.detail.split('\n');
        this.setData({
            segments: segments
        });
        console.log(this.data.segments);
    },

    // 查看卖家个人信息
    sellerInfo() {
        wx.navigateTo({
            url: '/pages/info/info?uid=' + this.data.sellerId
        })
    },

    // 响应图片点击事件
    onImageClick(e) {
        const _this = this;
        const url = e.currentTarget.dataset.src;
        wx.previewImage({
            current: url,
            urls: _this.data.imageList
        });
    },

    // 响应地图点击事件
    onMapClick() {
        wx.openLocation({
            latitude: this.data.location.lat,
            longitude: this.data.location.lng,
            scale: 16,
            name: this.data.location.title,
            address: this.data.location.address,
        });
    },

    // 联系卖家
    contact() {
        wx.navigateTo({
            url: '/pages/chat/chat?uid=' + this.data.sellerId,
        });
    },

    // 编辑商品信息
    edit() {
        const _this = this;
        wx.navigateTo({
            url: '/pages/sell/sell?edit=' + this.data.id,
            events: {
                commodityEditEvent() {
                    _this.getDetail(_this.data.id);
                }
            }
        });
    },

    // 收藏
    collect() {
        const data = {
            cid: this.data.id
        };
        wx.showLoading({
            title: '收藏中'
        });
        request.post(collection.add, data)
        .then(() => {
            this.setData({
                collected: true
            });
            wx.hideLoading();
        })
        .catch((err) => {
            wx.hideLoading();
            this.handleError(err);
        });
    },

    // 取消收藏
    cancelCollect() {
        wx.showLoading({
            title: '取消收藏中'
        });
        request.post(collection.cancel, { cid: this.data.id })
        .then(() => {
            this.setData({
                collected: false
            });
            wx.hideLoading();
        })
        .catch((err) => {
            wx.hideLoading();
            this.handleError(err);
        });
    },

    // 建立交换单
    exchange() {
        const path = '/pages/trade/trade?type=exchange&cid=' + this.data.id;
        wx.navigateTo({
            url: path,
        });
    },

    // 建立交易单
    buy() {
        const path = '/pages/trade/trade?type=buy&cid=' + this.data.id;
        wx.navigateTo({
            url: path,
        });
    },

    // 下架商品
    drop() {
        const _this = this;
        const url = commodity.off + '/' + this.data.id;
        request.get(url)
        .then(() => {
            _this.setData({
                offShelf: true,
            });
            wx.showToast({
                title: '下架成功',
                icon: 'none'
            });
        })
        .catch((err) => {
            console.log("下架操作失败: ", err);
            if (err.data.msg) {
                wx.showToast({
                    title: '下架操作失败: ' + err.data.msg,
                    icon: 'none'
                });
            }
        });
    },

    // 从后端获取商品详细信息
    async getDetail(cid) {
        const url = commodity.detail + '/' + cid;
        request.get(url)
        .then((res) => {
            const data = res.data.data;
            console.log(data);
            this.setData({
                sellerId: data.sellerId,
                name: data.sellerName,
                avatar: data.sellerAvatar,
                isSeller: data.seller,
                detail: data.description,
                collected: data.collected,
                location: {
                    lat: data.latitude,
                    lng: data.longitude,
                    title: data.location,
                    address: data.address,
                },
                imageList: data.images,
                time: data.time,
                couldExchange: data.exchange,
                price: data.price,
                offShelf: data.offShelf,
                marker: [
                    {
                        latitude: data.latitude,
                        longitude: data.longitude,
                        title: data.location,
                    }
                ]
            });
            this.detailToSegment();
        })
        .catch((err) => {
            this.handleError(err);
        });
    },

    // 统一对错误进行处理
    handleError(err) {
        console.log(err);
        if (err.data !== undefined && err.data.code === 401) {
            app.toLogin();
        }
    },
})