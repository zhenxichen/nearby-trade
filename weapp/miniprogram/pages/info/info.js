// pages/info/info.js

import request from "../../api/request.js";
import api from "../../api/api.js";

const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        uid: '',
        myself: false,
        avatar: '',
        username: '',
        rate: 0.0,
        activeTab: 'onsell',
        sellingList: [],
        collectionList: []
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log(options);
        this.setData({
            uid: options.uid
        });
        if (options.collection === "true") {
            this.setData({
                myself: true,
                activeTab: 'collection',
            });
        }
        this.initData();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听用户下拉刷新
     */
    onPullDownRefresh: function() {
        this.initData();
        wx.stopPullDownRefresh();
    },

    onCollectionDelete(event) {
        console.log(event);
    },

    setMockData() {
        const collectionList = [
            {
                no: '00000001',
                image: 'https://img.yzcdn.cn/vant/cat.jpeg',
                title: '猫猫的图片 neko 为了测试 这里加一段相对比较长的描述性文字，用来测试是否会出现超过两行',
                avatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                seller: '为了测试这里再放一个非常长的超过显示长度的用户名',
                distance: '5.0km'
            }, 
            {
                no: '00000002',
                image: '',
                title: '一个正常长度的标题',
                avatar: '',
                seller: '三个字',
                distance: '500m'
            },
        ];
        const sellingList = [
            {
                no: '00000001',
                image: 'https://img.yzcdn.cn/vant/cat.jpeg',
                title: '猫猫的图片 neko 为了测试 这里加一段相对比较长的描述性文字，用来测试是否会出现超过两行',
                avatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                seller: '为了测试这里再放一个非常长的超过显示长度的用户名',
                distance: '5.0km'
            }
        ]
        for (let i = 0; i < 10; i++) {
            collectionList.push(collectionList[0]);
        }
        this.setData({
            myself: true,
            avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
            username: '我的用户名',
            rate: 3.6,
            collectionList: collectionList,
            sellingList: sellingList
        });
    },

    // 从后端获取数据并初始化界面
    initData() {
        const _this = this;
        const infoUrl = api.userInfo.info + '/' + _this.data.uid;
        // 获取用户基本信息
        request.get(infoUrl)
        .then((res) => {
            const data = res.data.data;
            console.log(data);
            _this.setData({
                uid: data.uid,
                username: data.username,
                avatar: data.avatar,
                rate: data.rate,
                myself: data.myself,
            });
            // 若在访问自己的个人信息页面
            if (data.myself === true) {
                // 获取用户收藏列表
                _this.getCollectionList();
                _this.recoverTabBottom();
            }
        })
        .catch((err) => {
            _this.handleError(err);
        });
        // 获取在售商品列表
        const onSellUrl = api.userInfo.onSell + '/' + _this.data.uid;
        request.post(onSellUrl, {
            latitude: app.globalData.location.lat,
            longitude: app.globalData.location.lng,
        })
        .then((res) => {
            const list = res.data.data.list;
            const length = res.data.data.length;
            const _sellingList = [];
            for (let i = 0; i < length; i++) {
                _sellingList.push({
                    no: list[i].cid,
                    image: list[i].image,
                    title: list[i].title,
                    seller: list[i].sellerName,
                    avatar: list[i].sellerAvatar,
                    distance: list[i].distance,
                });
            }
            _this.setData({
                sellingList: _sellingList
            });
        })
        .catch((err) => {
            _this.handleError(err);
        })
    },

    // 从后端获取收藏列表
    getCollectionList() {
        const location = {
            latitude: app.globalData.location.lat,
            longitude: app.globalData.location.lng,
        };
        request.post(api.collection.list, location)
        .then((res) => {
            const list = res.data.data.list;
            const length = res.data.data.length;
            const _collectionList = [];
            for (let i = 0; i < length; i++) {
                _collectionList.push({
                    no: list[i].cid,
                    image: list[i].image,
                    title: list[i].title,
                    seller: list[i].sellerName,
                    avatar: list[i].sellerAvatar,
                    distance: list[i].distance,
                });
            }
            this.setData({
                collectionList: _collectionList
            });
        })
        .catch((err) => {
            _this.handleError(err);
        });
    },

    // 处理BUG：移动tab底部位置再移回，修复tab位置错误的问题
    recoverTabBottom() {
        this.selectComponent('#van-tabs').resize();
    },

    handleError(err) {
        console.log(err);
        if (err.data !== undefined && err.data.code === 401) {
            app.toLogin();
        }
    },
})