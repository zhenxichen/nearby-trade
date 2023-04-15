// pages/order/order.js

import request from "../../api/request";
import api from "../../api/api";

const PAGE_KEY = 'order';
const typeMap = {
    'all': 0,
    'buy': 1,
    'sell': 2,
};
const statusMap = {
    'all': -1,
    'waiting': 0,
    'progress': 1,
    'finished': 2,
};

Page({

    /**
     * 页面的初始数据
     */
    data: {
        activeTab: 'all',
        statusTab: 'all',
        orderList: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.renderOrderList();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.getTabBar().setData({
            current: PAGE_KEY
        });
        this.getTabBar().updateMessageCount();
        wx.setNavigationBarTitle({
            title: '订单列表'
        });
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.setData({
            activeTab: 'all',
            statusTab: 'all',
        });
        this.renderOrderList();
        wx.stopPullDownRefresh();
    },

    // 从后端获取数据并渲染订单列表
    renderOrderList() {
        const _this = this;
        const data = {
            role: typeMap[_this.data.activeTab],
            status: statusMap[_this.data.statusTab],
        };
        request.post(api.order.list, data)
        .then((res) => {
            console.log(res);
            const list = res.data.data;
            const length = list.length;
            const _orderList = [];
            for (let i = 0; i < length; i++) {
                const order = list[i];
                _orderList.push({
                    orderNo: order.oid,
                    isSeller: order.seller,
                    status: order.status,
                    image: order.image,
                    title: order.description,
                    oppositeId: order.oppositeId,
                    exchange: order.exchange,
                    price: order.price,
                    rate: order.rate,
                });
            }
            _this.setData({
                orderList: _orderList
            });
        })
    },

    // 处理类型标签页切换
    handleTabChange(event) {
        this.setData({
            activeTab: event.detail.name,
            statusTab: 'all'
        });
        this.renderOrderList();
    },

    // 处理状态标签页切换
    handleStatusTabChange(event) {
        this.setData({
            statusTab: event.detail.name
        });
        this.renderOrderList();
    },

    // 处理用户点击评价按钮
    onRateClick(e) {
        const oid = e.oid;
    },

    setMockData() {
        const orders = [];
        orders.push({
            orderNo: '2021112115300124',
            type: '购买',
            status: '已完成',
            image: 'https://img.yzcdn.cn/vant/cat.jpeg',
            title: '商品名称以及对商品的一些介绍,这里加长一点商品的名称，看一下换行的表现效果是是什么样的',
            oppositeId: '123456',
            exchange: false,
            rate: true,
            price: 1300.0
        });
        orders.push({
            orderNo: '2021112115300125',
            type: '购买',
            status: '进行中',
            image: 'https://img.yzcdn.cn/vant/cat.jpeg',
            title: '商品名称以及对商品的一些介绍',
            exchange: true,
            rate: false,
            price: 3.0
        });
        this.setData({
            orderList: orders
        });
    }
})