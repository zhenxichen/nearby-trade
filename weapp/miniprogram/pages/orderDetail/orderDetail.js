// pages/orderDetail/orderDetail.js

import request from "../../api/request";
import api from "../../api/api";

const PAGE_NAME = '订单详情页';
const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        orderNo: '',
        status: '',
        opposite: {
            uid: '',
            name: '',
            avatar: ''
        },
        commodity: {
            id: '',
            image: '',
            desc: ''
        },
        isExchange: false,      // 是否为交换单
        isSeller: false,        // 当前用户是否为卖家
        couldRate: false,       // 当前用户是否可以评价对方
        price: 0.0,             // 该订单金额
        exchange: {             // 交换单相关数据，若为交易单为空
            image: '',          // 交换物品图片
            desc: ''            // 交换物品描述
        },
        createTime: undefined,
        tradeTime: undefined,
        finishTime: undefined,
        location: {
            title: '',
            address: '',
            lat: 0.0,
            lng: 0.0
        },
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            orderNo: options.no
        });
        this.getOrderDetail();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        wx.setNavigationBarTitle({
            title: PAGE_NAME
        });
    },

    onPullDownRefresh () {
        wx.stopPullDownRefresh()
    },

    // 从后端获取订单详情并渲染页面
    getOrderDetail() {
        const _this = this;
        wx.showLoading({
            title: '获取订单详情中',
        });
        const url = api.order.detail + '/' + this.data.orderNo;
        request.get(url)
        .then((res) => {
            const data = res.data.data;
            console.log("订单详情数据: ", data);
            _this.renderPage(data);
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 通过数据渲染页面
    renderPage(data) {
        wx.hideLoading();
        this.setData({
            status: data.status,
            commodity: {
                id: data.cid,
                image: data.commodityImage,
                desc: data.commodityDescription,
            },
            isExchange: data.exchange,
            isSeller: data.seller,
            couldRate: data.rate,
            createTime: data.createTime,
            tradeTime: data.tradeTime,
            finishTime: data.finishTime ? data.finishTime : '',
            location: {
                lat: data.latitude,
                lng: data.longitude,
                title: data.location,
                address: data.address,
            },
            price: data.price,
            exchange: {
                image: data.exchangeImage,
                desc: data.exchangeDescription,
            },
            opposite: {
                uid: data.oppositeId,
                name: data.oppositeName,
                avatar: data.oppositeAvatar,
            },
        });
    },

    // 响应网络错误
    handleNetworkError(err) {
        wx.hideLoading();
        console.log("获取订单详情信息失败: ", err);
        if (err.data && err.data.code && err.data.code === 401) {
            wx.showToast({
                title: '用户登录态失效，即将前往登录界面',
                duration: 1000,
                success: () => {
                    app.toLogin();
                },
            });
            return;
        }
        if (err.data.msg) {
            wx.showToast({
                title: err.data.msg,
                duration: 1500,
                success: () => {
                    wx.navigateBack({
                        delta: 1
                    });
                }
            });
            return;
        }
    },

    setMockData() {
        let isExchange = false;
        let status = '已完成';
        let isSeller = true;
        let couldRate = true;
        if (this.data.orderNo === '2021112115300125') {
            isExchange = true;
            status = '进行中';
            isSeller = false;
            couldRate = false;
        }
        this.setData({
            status: status,
            opposite: {
                uid: '123456',
                avatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                name: '对方的ID'
            },
            tradeTime: '2021/11/22 16:50',
            location: {
                title: '四川大学江安医院东南',
                address: '四川省成都市双流区川大路',
                lat: 30.552715,
                lng: 103.993121
            },
            commodity: {
                id: '00000001',
                image: 'https://img.yzcdn.cn/vant/cat.jpeg',
                desc: '商品的基本描述'
            },
            price: 1300.00,
            isExchange: isExchange,
            isSeller: isSeller,
            exchange: {
                id: '00000002',
                image: '',
                desc: '用来交换的物品描述'
            },
            couldRate: couldRate
        })
    },

    // 响应点击对方信息框事件
    handleOppositeClick() {
        wx.navigateTo({
            url: '/pages/info/info?uid=' + this.data.opposite.uid,
        });
    },

    // 响应点击位置事件
    handleLocationClick() {
        wx.openLocation({
            latitude: this.data.location.lat,
            longitude: this.data.location.lng,
            name: this.data.location.title,
            address: this.data.location.address,
            complete: (res) => {
                console.log(res);
            }
        });
    },

    // 响应确认建单点击事件
    handleConfirmCreateOrder() {
        const _this = this;
        wx.showModal({
            content: '确认要建立订单吗',
            showCancel: true,
            cancelText: '取消',
            cancelColor: '#808080',
            confirmText: '确定',
            confirmColor: '#39B54A',
            success: (result) => {
                if(result.confirm){
                    // 处理建单逻辑
                    _this.handleOrderCreateInternal();
                }
            },
        });
    },

    // 处理建单逻辑
    handleOrderCreateInternal() {
        const _this = this;
        wx.showLoading({
            title: '订单接受中'
        });
        const url = api.order.accpet + '/' + _this.data.orderNo;
        request.get(url)
        .then(() => {
            wx.hideLoading();
            wx.showToast({
                title: '接受成功',
                icon: 'none'
            });
            _this.setData({
                status: '进行中'
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 响应确认结单点击事件
    handleConfirmFinishOrder() {
        const _this = this;
        wx.showModal({
            content: '确认要结单吗',
            showCancel: true,
            cancelText: '取消',
            cancelColor: '#808080',
            confirmText: '确定',
            confirmColor: '#39B54A',
            success: (result) => {
                if(result.confirm){
                    // 处理结单逻辑
                    _this.handleOrderConfirmInternal()
                }
            },
        });
    },

    // 处理结单逻辑
    handleOrderConfirmInternal() {
        const _this = this;
        const url = api.order.confirm + '/' + this.data.orderNo;
        wx.showLoading({
            title: '确认结单中',
        });
        request.get(url)
        .then(() => {
            wx.hideLoading();
            wx.showToast({
                title: '结单成功',
                icon: 'none'
            });
            _this.setData({
                status: '已完成',
                rate: true,
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 响应取消订单点击事件
    handleCancelOrder() {
        const _this = this;
        wx.showModal({
            content: '确认要取消订单吗',
            showCancel: true,
            cancelText: '取消',
            cancelColor: '#808080',
            confirmText: '确定',
            confirmColor: '#39B54A',
            success: (result) => {
                if(result.confirm){
                    // 处理取消订单逻辑
                    _this.handleOrderCancelInternal();
                }
            },
        });
    },

    // 处理取消订单逻辑
    handleOrderCancelInternal() {
        const _this = this;
        wx.showLoading({
            title: '订单取消中'
        });
        const url = api.order.cancel + '/' + _this.data.orderNo;
        request.get(url)
        .then(() => {
            wx.hideLoading();
            wx.showToast({
                title: '取消成功',
                icon: 'none'
            });
            _this.setData({
                status: '已关闭'
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 响应点击评价按钮事件
    handleRate() {
        const _this = this;
        const ratePath = '/pages/rate/rate?oid=' + this.data.orderNo
        wx.navigateTo({
            url: ratePath,
            events: {
                rateEvent: () => {
                    _this.setData({
                        couldRate: false,
                    });
                }
            }
        });
    },

    // 响应点击联系对方按钮事件
    handleContact() {
        const contactPath = '/pages/chat/chat?uid=' + this.data.opposite.uid;
        wx.navigateTo({
            url: contactPath
        });
    },
})