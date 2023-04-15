// pages/setting/setting.js

import { setting } from '../../api/api.js'
import request from '../../api/request.js'
import cos from '../../api/cos.js'

const app = getApp();

const CAMERA_INDEX = 0;     // 拍照的动作面板索引
const ALBUM_INDEX = 1;      // 相册的动作面板索引

Page({

    /**
     * 页面的初始数据
     */
    data: {
        uid: '',
        avatar: '',
        username: '',
        phone: '',
        bindWechat: false,
        showUserNameDialog: false,
        showPwdDialog: false,
        showPhoneDialog: false,
        newUsername: '',
        oldPassword: '',
        newPassword: '',
        oldPhone: '',
        captcha: '',
        newPhone: '',
        showCountDown: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        const uid = app.globalData.uid;
        this.setData({
            uid: uid
        });
        this.initData();
    },

    setMockData() {
        if (this.data.uid !== 'test') {
            return;
        }
        this.setData({
            avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
            username: '我的用户名',
            phone: '15980662613',
            bindWechat: false,
        })
    },

    // 从后端获取信息并初始化
    initData() {
        const _this = this;
        request.get(setting.info)
        .then((res) => {
            console.log(res.data);
            const data = res.data.data;
            _this.setData({
                username: data.username,
                avatar: data.avatar,
                phone: data.phone,
                bindWechat: data.bindWechat
            })
        })
        .catch((err) => {
            if (err.statusCode !== 200) {
                wx.showToast({
                    title: '网络错误',
                    icon: 'none',
                    duration: 1500,
                });
                return;
            }
            if (err.data.code === 401) {
                app.toLogin();
                return;
            }
        })
    },

    // 处理修改头像选项
    handleAvatar() {
        const _this = this;
        wx.chooseImage({
            count: 1,
            sizeType: ['original','compressed'],
            sourceType: ['album','camera'],
            success: (result)=>{
                console.log(result);
                const filepath = result.tempFiles[0].path;
                const extension = filepath.substr(filepath.lastIndexOf('.'));
                cos.uploadImage(filepath, extension)
                .then((res) => {
                    const uploadPath = 'https://' + res.Location;
                    console.log(uploadPath);
                    const avatarData = {
                        avatar: uploadPath
                    };
                    return request.post(setting.avatar, avatarData);
                })
                .then((res) => {
                    // 若设置成功，则重新获取页面信息
                    _this.initData();
                })
                .catch((err) => {
                    console.log(err);
                });
            },
            fail: (err)=>{
                console.log("获取图片失败：", err);
            },
            complete: ()=>{}
        });
    },

    // 当处理头像消息的动作面板被取消
    onActionSheetCancel() {
        console.log("点击取消");
        this.closeAvatarActionSheet();
    },

    onActionSheetClickOverlay() {
        this.closeAvatarActionSheet();
    },

    // 处理修改用户名选项
    handleUsername() {
        this.setData({
            showUserNameDialog: true
        });
    },

    // 处理修改密码选项
    handlePassword() {
        this.setData({
            showPwdDialog: true
        });
    },

    // 处理修改手机号选项
    handlePhone() {
        this.setData({
            showPhoneDialog: true
        });
    },

    // 处理绑定/解绑微信账号选项
    handleWechat() {
        if (this.data.bindWechat) {
            this.handleUnbind();
        } else {
            this.handleBind();
        }
    },

    // 处理退出账号
    handleLogOut() {
        console.log("退出账号");
        request.get('/logout')
        .then((res) => {
            console.log(res);
        })
        // 断开Socket连接
        app.disconnectSocket();
        // 跳转到登录界面
        app.toLogin();
    },

    // 处理点击绑定微信
    handleBind() {
        const _this = this;
        wx.showModal({
            content: '确定要将当前账号与微信号绑定吗？',
        })
        .then((res) => {
            if (res.confirm) {
                _this.bindWechatAccount();
            }
        });
    },

    // 绑定微信账号具体逻辑
    bindWechatAccount() {
        const _this = this;
        return new Promise((resolve, reject) => {
            wx.showLoading({
                title: '绑定微信账号中'
            });
            wx.cloud.callFunction({
                name: 'quickstartFunctions',
                data: {
                    type: 'getOpenId'
                },
                success: (res) => { resolve(res); },
                fail: (err) => { console.log("获取微信账号错误：", err); }
            })
        }) 
        .then((res) => {
            console.log("获取微信openid成功: ", res);
            const openid = res.result.openid;
            const data = {
                openid: openid
            };
            return request.post(setting.wechat.bind, data)
        })
        .then((res) => {
            wx.hideLoading();
            _this.showToast('绑定成功');
            _this.setData({
                bindWechat: true,
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
            wx.hideLoading();
        })
    },

    // 处理点击解绑微信
    handleUnbind() {
        const _this = this;
        wx.showModal({
            content: '确定解除当前账号与微信号的绑定吗？',
        })
        .then((res) => {
            if (res.confirm) {
                _this.unbindWechatAccount();
            }
        })
    },

    // 处理微信解绑逻辑
    unbindWechatAccount() {
        const _this = this;
        wx.showLoading({
            title: '解绑微信账号中'
        });
        request.get(setting.wechat.unbind)
        .then(() => {
            wx.hideLoading();
            _this.showToast('解除绑定成功');
            _this.setData({
                bindWechat: false,
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
            wx.hideLoading();
        })
    },

    // 提交修改用户名请求
    changeUsername() {
        const _this = this;
        const username = _this.data.newUsername;
        if (username === '') {
            _this.showToast('请输入新用户名');
            return;
        }
        const data = {
            username: username
        };
        request.post(setting.username, data)
        .then((res) => {
            _this.showToast('修改用户名成功');
            _this.initData();
            _this.setData({
                newUsername: ''
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 提交修改密码请求
    changePassword() {
        const _this = this;
        const oldPassword = _this.data.oldPassword;
        if (oldPassword === '') {
            _this.showToast('请输入旧的密码');
            return;
        }
        const newPassword = _this.data.newPassword;
        if (newPassword === '') {
            _this.showToast('请输入新的密码');
            return;
        }
        const data = {
            oldPassword: oldPassword,
            newPassword: newPassword,
        };
        request.post(setting.password, data)
        .then((res) => {
            _this.setData({
                oldPassword: '',
                newPassword: '',
            });
            _this.showToast('修改密码成功');
        })
        .catch((err) => {
            _this.setData({
                oldPassword: '',
                newPassword: '',
            });
            if (err.data && err.data.code === 403) {
                _this.showToast('旧密码错误，请重试');
                return;
            }
            _this.handleNetworkError(err);
        })
    },

    // 发送短信验证码到旧手机号
    sendCaptcha() {
        const _this = this;
        const url = setting.phone.vc;
        const oldPhone = this.data.oldPhone;
        if (oldPhone === '') {
            this.showToast('请输入旧手机号');
            return;
        }
        request.get(url, { phone: oldPhone })
        .then(() => {
            _this.showToast('验证码发送成功');
            _this.setData({
                showCountDown: true,
            });
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        })
    },

    // 当获取验证码的倒计时结束时触发
    onCountDownFinish() {
        this.setData({
            showCountDown: false,
        });
    },

    // 修改手机号
    changePhone() {
        const _this = this;
        if (this.data.oldPhone === '') {
            this.showToast('请输入旧手机号');
            return;
        }
        if (this.data.captcha === '') {
            this.showToast('请输入验证码');
            return;
        }
        if (this.data.newPhone === '') {
            this.showToast('请输入新手机号');
            return;
        }
        const data = {
            phone: this.data.oldPhone,
            verificationCode: this.data.captcha,
            newPhone: this.data.newPhone
        };
        request.post(setting.phone.update, data)
        .then(() => {
            _this.showToast('修改成功');
            _this.setData({
                oldPhone: '',
                captcha: '',
                newPhone: ''
            });
            _this.initData();
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        });
    },

    // 统一响应网络错误
    handleNetworkError(err) {
        console.log(err);
        if (err.data && err.data.code === 401) {
            wx.showToast({
                title: '登录态失效，即将跳转到登录界面',
                icon: 'none'
            });
            app.toLogin();
            return;
        }
        if (err.data && err.data.msg) {
            wx.showToast({
                title: err.data.msg,
                icon: 'none'
            });
            return;
        }
        this.showToast('网络传输出现问题');
    },

    showToast(msg) {
        wx.showToast({
            title: msg,
            icon: 'none'
        });
    }

})