// pages/message/message.js

import { message } from '../../api/api.js';
import request from '../../api/request.js';

const app = getApp();
const PAGE_KEY = 'message';

Page({

    /**
     * 页面的初始数据
     */
    data: {
        messageList: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.setNavigationBarTitle({
            title: '消息记录'
        });
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.initPage();
        const tabBar = this.getTabBar();
        tabBar.setData({
            current: PAGE_KEY
        });
        app.globalData.unread = false;
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.initPage();
        wx.stopPullDownRefresh();
    },

    // 响应 WebSocket 消息
    onMessage: function() {
        // 刷新消息列表
        this.initPage();
    },

    // 从后端获取数据，并初始化页面
    initPage() {
        const _this = this;
        request.get(message.list)
        .then((res) => {
            const list = res.data.data;
            const _messageList = [];
            for (let i = 0; i < list.length; i++) {
                _messageList.push({
                    avatar: list[i].avatar,
                    name: list[i].username,
                    message: list[i].content,
                    count: list[i].count,
                    uid: list[i].opposite,
                });
            }
            _this.setData({
                messageList: _messageList
            });
        })
    },


    /**
     * 填入Mock的消息列表
     */
    setMockMessageList() {
        const list = this.data.messageList;
        list.push({
            avatar: "https://i.loli.net/2017/08/21/599a521472424.jpg",
            name: "对方的名字",
            message: "对方发来的消息",
            count: "1",
            uid: "125570",
        });
        list.push({
            avatar: "https://img.yzcdn.cn/vant/cat.jpeg",
            name: "猫猫头像的人",
            message: "猫猫头像的人发来的消息",
            count: "0",
            uid: "125571",
        });
        this.setData({
            messageList: list
        });
    }
})