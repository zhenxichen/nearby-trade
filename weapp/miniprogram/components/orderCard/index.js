// components/orderCard/index.js

import request from "../../api/request";
import api from "../../api/api";

const app = getApp();

Component({
    /**
     * 组件的属性列表
     */
    properties: {
        type: {
            type: String,
            value: ''
        },
        image: {
            type: String,
            value: ''
        },
        title: {
            type: String,
            value: ''
        },
        price: {
            type: Number,
            value: 0.0
        },
        oppositeId: {
            type: String,
            value: ''
        },
        orderNo: {
            type: String,
            value: ''
        },
        exchange: {
            type: Boolean,
            value: false
        },
        rate: {
            type: Boolean,
            value: false
        },
        status: {
            type: String,
            value: ''
        },
        isSeller: {
            type: Boolean,
            value: false
        },
    },

    /**
     * 组件的初始数据
     */
    data: {

    },

    /**
     * 组件的方法列表
     */
    methods: {
        // 当订单页面被点击，跳转到订单详情页面
        onClick() {
            const detailPath = '/pages/orderDetail/orderDetail?no=' + this.data.orderNo
            wx.navigateTo({
                url: detailPath
            });
        },

        // 在按钮被点击时，拦截点击事件，避免父组件处理点击事件从而跳转到错误的页面
        catchTap() {
            // do nothing
        },

        // 当评价按钮被点击，跳转到评价页面
        onRateClick() {
            const ratePath = '/pages/rate/rate?oid=' + this.data.orderNo;
            const _this = this;
            wx.navigateTo({
                url: ratePath,
                events: {
                    rateEvent: () => {
                        _this.setData({
                            rate: false,
                        });
                    }
                }
            })
        },

        // 当联系按钮被点击，跳转到聊天页面
        onContactClick() {
            const contactPath = '/pages/chat/chat?uid=' + this.data.oppositeId;
            wx.navigateTo({
                url: contactPath
            });
        },

        // 当取消订单按钮被点击
        onCancelClick() {
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
                        _this.handleOrderCancel();
                    }
                },
            });
        },

        // 处理取消订单逻辑
        handleOrderCancel() {
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

        // 当用户点击确认建单
        onConfirmCreateClick() {
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
                        _this.handleOrderCreate();
                    }
                },
            });
        },

        // 处理建单逻辑
        handleOrderCreate() {
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

        // 当用户点击确认结单
        onConfirmClick() {
            const _this = this;
            wx.showModal({
                content: '确认要结单吗',
                showCancel: true,
                cancelText: '取消',
                cancelColor: '#808080',
                confirmText: '确定',
                confirmColor: '#39B54A',
                success: (result) => {
                    if (result.confirm){
                        // 处理结单逻辑
                        _this.handleOrderConfirm();
                    }
                },
            });
        },

        // 处理结单逻辑
        handleOrderConfirm() {
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

        handleNetworkError(err) {
            wx.hideLoading();
            console.log("OrderCard组件网络错误: ", err);
            if (err.data.code && err.data.code === 401) {
                app.toLogin();
                return;
            }
            if (err.data.msg) {
                wx.showToast({
                    title: err.data.msg,
                    icon: 'none'
                });
                return;
            }
        },
    },
})
