// app.js

import { $Message } from './dist/iview/base/index';

App({
    onLaunch: function () {
        if (!wx.cloud) {
            console.error('请使用 2.2.3 或以上的基础库以使用云能力');
        } else {
            wx.cloud.init({
                // env 参数说明：
                //   env 参数决定接下来小程序发起的云开发调用（wx.cloud.xxx）会默认请求到哪个云环境的资源
                //   此处请填入环境 ID, 环境 ID 可打开云控制台查看
                //   如不填则使用默认环境（第一个创建的环境）
                // env: 'my-env-id',
                traceUser: true,
            });
        }

        this.globalData = {
            location: {},
            jwt: '',
            uid: '',
            baseUrl: 'https://www.zhenxichen.cn:18080',
            socketUrl: 'wss://www.zhenxichen.cn:18080',
            socketTask: null,
            unread: false,
        };
    },

    toLogin: function() {
        this.globalData.jwt = '';
        this.globalData.uid = '';
        wx.removeStorage({
            key: 'uid'
        });
        wx.removeStorage({
            key: 'jwt'
        });
        wx.reLaunch({
            url: '/pages/login/login',
        });
    },

    // 与后台socket连接，以接收即时通讯消息
    connectSocket() {
        const sockTask = wx.connectSocket({
            url: this.globalData.socketUrl + '/im/chat',
            header: {
                'Content-Type':'application/json',
                'Authorization': 'Bearer ' + this.globalData.jwt
            },
            success: function(res) {
                console.log("连接即时通讯服务器成功", res);
            },
            fail: function(err) {
                console.log("连接即时通讯服务器失败", err);
            }
        });
        this.globalData.socketTask = sockTask;
        // 绑定关闭连接响应函数
        sockTask.onClose((res) => {
            console.log("Socket连接已关闭");
        });
        sockTask.onMessage((res) => {
            this.onMessage(res);
        });
    },

    // 处理 WebSocket 消息响应逻辑
    onMessage(res) {
        const data = JSON.parse(res.data);
        console.log(data);
        const pageStack = getCurrentPages();
        const currentPage = pageStack[pageStack.length - 1];
        // 若当前页面定义了onMessage函数，则交由当前页面处理
        if (currentPage.onMessage) {
            currentPage.onMessage(data);
            return;
        }
        // 过滤目标不是自己的消息
        if (data.to != this.globalData.uid) {
            return;
        }
        // 修改全局变量，以便提示有未读消息
        this.globalData.unread = true;
        // 若当前页面有tabbar，更新tabbar显示状态
        const tabBar = currentPage.getTabBar();
        if (tabBar) {
            tabBar.updateMessageCount();
        }
        // 通过 message 组件通知用户新消息
        let message;
        if (data.type === 'text') {
            message = '新消息: ' + data.content;
        } else if (data.type === 'image') {
            message = '新消息: [图片]';
        } else if (data.type === 'location') {
            message = '新消息: [位置]';
        } else {
            message = '您有一条新消息';
        }
        $Message({
            content: message
        });
    },

    // 断开WebSocket连接
    disconnectSocket() {
        const sockTask = this.globalData.socketTask;
        sockTask.close();
        this.globalData.socketTask = null;
    },


});
