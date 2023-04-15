// pages/login/login.js

import { login } from '../../api/api.js'
import request from '../../api/request.js'

const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        phone: '',
        password: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },

    // 提交登录指令
    login() {
        if (!this.checkInput()) {
            return;
        }
        wx.showLoading({
            title: '登录中'
        });
        request.post(login.phone, this.data, false)
        .then((res) => {
            wx.hideLoading();
            console.log(res);
            wx.setStorage({
                key: 'jwt',
                data: res.data.data.token,
            });
            wx.setStorage({
                key: 'uid',
                data: res.data.data.uid
            });
            app.globalData.jwt = res.data.data.token;
            app.globalData.uid = res.data.data.uid;
            wx.switchTab({
                url: '/pages/index/index',
            });
        })
        .catch((err) => {
            wx.hideLoading();
            if (err.data.code === 403) {
                wx.showToast({
                    title: '密码错误',
                    icon: 'none',
                    duration: 1000,
                });
            } else {
                console.log(err);
                wx.showToast({
                    title: '登录失败',
                    icon: 'none',
                    duration: 1000,
                });
            }
        })
        
    },

    // 检验是否输入正确
    checkInput() {
        if (this.data.phone === '' || this.data.phone === undefined) {
            wx.showToast({
                title: '请输入手机号',
                icon: 'none',
                duration: 1000,
            });
            return false;
        }
        if (this.data.phone.length !== 11) {
            wx.showToast({
                title: '请输入11位手机号',
                icon: 'none',
                duration: 1000,
            });
            return false;
        }
        if (this.data.password === '' || this.data.password === undefined) {
            wx.showToast({
                title: '密码',
                icon: 'none',
                duration: 1000,
            });
            return false;
        }
        return true;
    },

    // 通过微信快速登录
    wechatLogin() {
        wx.cloud.callFunction({
            name: 'quickstartFunctions',
            data: {
                type: 'getOpenId'
            },
        })
        .then((res) => {
            console.log(res);
            const data = {
                openid: res.result.openid
            };
            wx.showLoading({
                title: '登录中'
            });
            return request.post(login.wechat, data, false)
        })
        .then((res) => {
            wx.hideLoading();
            console.log(res);
            wx.setStorage({
                key: 'jwt',
                data: res.data.data.token,
            });
            wx.setStorage({
                key: 'uid',
                data: res.data.data.uid
            });
            app.globalData.jwt = res.data.data.token;
            app.globalData.uid = res.data.data.uid;
            wx.switchTab({
                url: '/pages/index/index',
            });
        })
        .catch((err) => {
            console.log("微信快捷登录失败: ", err);
            wx.hideLoading();
            if (err.data && err.data.msg) {
                wx.showToast({
                    title: err.data.msg,
                    icon: 'none',
                    duration: 1000,
                });
            } else {
                wx.showToast({
                    title: '微信快捷登录失败',
                    icon: 'none',
                    duration: 1000,
                });
            }
        })
    },
    
    // 跳转到注册界面
    signUp() {
        wx.navigateTo({
            url: '/pages/login/signup'
        });
    },

    // 跳转到找回密码界面
    forget() {

    },

    // 将bindinput绑定一个不进行任何操作的函数，避免警告
    doNothing() {}
})