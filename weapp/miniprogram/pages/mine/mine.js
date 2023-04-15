// pages/mine/mine.js

import request from '../../api/request.js';
import { userInfo } from '../../api/api.js';

const PAGE_KEY = 'mine';
const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        uid: '',
        avatar: '',
        username: '',
        collections: [],        // 收藏商品的图片列表（最多三张）
        sells: [],              // 在售的商品图片列表
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.setNavigationBarTitle({
            title: '我的'
        });
        this.getMineInfo();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        const tabBar = this.getTabBar();
        tabBar.setData({
            current: PAGE_KEY
        });
        tabBar.updateMessageCount();
    },

    /**
     * 生命周期函数--监听用户下拉刷新
     */
    onPullDownRefresh: function() {
        this.getMineInfo();
        wx.stopPullDownRefresh();
    },

    setMockData() {
        this.setData({
            avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
            username: '我的用户名',
            collections: ['https://img.yzcdn.cn/vant/cat.jpeg']
        });
    },

    /**
     * 跳转到个人信息页面
     */
    personalInfo() {
        const path = '/pages/info/info?uid=' + app.globalData.uid;
        wx.navigateTo({
            url: path,
        });
    },

    /**
     * 跳转到收藏界面
     */
    collection() {
        const path = '/pages/info/info?uid=' + app.globalData.uid 
            + '&collection=true';
        wx.navigateTo({
            url: path
        });
    },

    // 获取“我的”界面需要展示的数据
    getMineInfo() {
        request.get(userInfo.mine)
        .then((res) => {
            const data = res.data.data;
            this.setData({
                uid: data.uid,
                avatar: data.avatar,
                username: data.username,
                collections: data.collectionImages,
                sells: data.sellImages
            });
        })
        .catch((err) => {
            if (err.data !== undefined && err.data.code === 401) {
                app.toLogin();
            }
        })
    },
})