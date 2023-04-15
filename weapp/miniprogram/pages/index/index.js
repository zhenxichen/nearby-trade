// pages/index/index.js

const PAGE_KEY = 'homepage';
const mapKey = require('../../constants/mapKey.js');
const paths = require('../../constants/paths.js');
const request = require('../../api/request.js');
const QQMapWx = require('../../libs/qqmap-wx-jssdk.js');
let qqMapSdk;

import { auth, commodity, message } from '../../api/api.js';
import { $Message } from '../../dist/iview/base/index.js';

// 获取全局数据
const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        location: {
            lat: 0.0,
            lng: 0.0,
            title: '正在定位中',
            address: '',
        },
        searchValue: undefined,
        commoditiesList: []
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        // 先校验用户是否已经登录
        this.checkLogin()
        .then(() => {
            // 再对页面进行初始化
            this.initPage();
            // 并与后台即时通讯服务器建立连接
            app.connectSocket();
            // 校验是否存在未读消息
            this.checkUnread();
        })
        .catch((err) => {
            app.toLogin();
        }); 
    },

    initPage() {
        const _this = this;
        qqMapSdk = new QQMapWx({
            key: mapKey.GEO_CODER_KEY
        });
        wx.showLoading({
            title: '正在获取附近商品信息'
        });
        _this.getLocation()
        .then(() => {
            _this.find();
            wx.hideLoading();
        })
        .catch(() => {
            _this.setMockCommodities();
            wx.hideLoading();
            wx.showToast({
                title: '获取商品信息失败',
                icon: 'none'
            });
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {        
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
        console.log("腾讯位置服务Key:", mapKey.GEO_CODER_KEY);
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.initPage();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {
        console.log("reach bottom");
    },

    /**
     * 响应用户对当前定位的点击信息
     */
    handleLocation(event) {
        const _this = this;
        wx.navigateTo({
            url: '/pages/location/location',
            events: {
                locationChangeEvent: function(location) {
                    _this.setData({
                        location: location
                    });
                    app.globalData.location = location;
                    _this.find();
                }
            }
        });
    },

    /**
     * 响应搜索框搜索
     */
    handleSearch() {
        console.log(this.data.searchValue);
        const data = {
            latitude: this.data.location.lat,
            longitude: this.data.location.lng,
            radius: 5000,
            keywords: this.data.searchValue
        }
        request.post(commodity.search, data)
        .then((res) => {
            this.parseListRes(res);
        })
        .catch((err) => {
            this.handleError(err);
        });
    },

    /**
     * 响应搜索框取消
     */
    handleCancel() {
        console.log("cancel");
        console.log(this.data.searchValue);
        this.setData({
            searchValue: ''
        });
    },

    /**
     * 响应加号按钮点击
     */
    handlePub() {
        wx.navigateTo({
            url: '/pages/sell/sell'
        });
    },

    // 截取点击，防止父组件收到点击事件
    catchTap() {
        // do nothing
    },

    /**
     * 获取当前位置信息，并进行逆地址解析
     */
    getLocation() {
        const that = this;
        return new Promise((resolve, reject) => {
            wx.getLocation({
                type: 'gcj02',
            })
            .then((res) => {
                console.log("获取的地理位置信息: ", res);
                app.globalData.location = res;
                const location = res;
                qqMapSdk.reverseGeocoder({
                    location: location.latitude + ',' + location.longitude,
                    success: function(res) {
                        console.log("逆地址解析返回值:", res);
                        const _location = {
                            lat: location.latitude,
                            lng: location.longitude,
                            title: res.result.formatted_addresses.recommend,
                            address: res.result.address
                        }
                        that.setData({
                            location: _location
                        });
                        app.globalData.location = _location;
                        resolve();
                    },
                    fail: function(err) {
                        console.log("获取位置信息失败", err);
                        reject();
                    }
                });
            });
        });        
    },

    // 在加载页面前判断用户是否登录，若未登录则跳转到登录界面
    checkLogin() {
        return new Promise((resolve, reject) => {
            let uid = app.globalData.uid;
            let jwt = app.globalData.jwt;
            if (uid === '' || uid === undefined || jwt === '' || jwt === undefined) {
                uid = wx.getStorageSync('uid');
                jwt = wx.getStorageSync('jwt');
                app.globalData.uid = uid;
                app.globalData.jwt = jwt;
            }
            if (uid === '' || uid === undefined || jwt === '' || jwt === undefined) {
                console.log(uid, jwt);
                reject();
            }
            console.log("用户token: ", jwt);
            request.get(auth.check)
            .then((res) => {
                resolve();
            })
            .catch((err) => {
                console.log(err);
                reject(err);
            })
        })
    },

    /**
     * 查找附近的商品信息
     */
    find() {
        const data = {
            latitude: this.data.location.lat,
            longitude: this.data.location.lng,
            radius: 5000,
        };
        request.post(commodity.find, data)
        .then((res) => {
            this.parseListRes(res);
        })
        .catch((err) => {
            this.handleError(err);
        })
    },

    /**
     * 将后端返回的商品列表渲染到页面上
     */
    parseListRes(res) {
        const data = res.data.data;
        const length = data.length;
        const list = data.list;
        const commoditiesList = [];
        for (let i = 0; i < length; i++) {
            commoditiesList.push({
                no: list[i].cid,
                image: list[i].image,
                title: list[i].title,
                seller: list[i].sellerName,
                avatar: list[i].sellerAvatar,
                distance: list[i].distance
            });
        }
        this.setData({
            commoditiesList: commoditiesList
        });
    },

    /**
     * 使用Mock数据填充商品信息列表
     */
    setMockCommodities() {
        // 用临时变量获取List的引用，之后再用this.setData更新列表数据
        if (app.globalData.uid !== 'test') {
            return;
        }
        const list = this.data.commoditiesList;
        list.push({
            no: '00000001',
            image: 'https://img.yzcdn.cn/vant/cat.jpeg',
            title: '猫猫的图片 neko 为了测试 这里加一段相对比较长的描述性文字，用来测试是否会出现超过两行',
            avatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
            seller: '为了测试这里再放一个非常长的超过显示长度的用户名',
            distance: '5.0km'
        });
        list.push({
            no: '00000002',
            image: '',
            title: '一个正常长度的标题',
            avatar: '',
            seller: '三个字',
            distance: '500m'
        });
        this.setData({
            commoditiesList: list
        });
    },

    /**
     * 处理网络请求错误
     */
    handleError(err) {
        console.log(err);
        if (err.data !== undefined && err.data.code === 401) {
            app.toLogin();
            return;
        }
    },

    /**
     * 通过消息组件显示消息
     */
    showMsg(msg) {
        console.log(msg);
        $Message({
            content: msg
        });
    },

    /**
     * 向后端服务器确认是否存在未读消息
     */
    checkUnread() {
        const _this = this;
        request.get(message.unread)
        .then((res) => {
            const unread = res.data.data;
            if (unread > 0) {
                app.globalData.unread = true;
                _this.getTabBar().updateMessageCount();
            }
        })
        .catch((err) => {
            console.log("确认未读消息失败", err);
        })
    }
})