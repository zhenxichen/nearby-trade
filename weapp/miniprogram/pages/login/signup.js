// pages/login/signup.js

import { signup } from "../../api/api.js";
import request from "../../api/request.js";

Page({

    /**
     * 页面的初始数据
     */
    data: {
        phone: '',
        username: '',
        password: '',
        passwordAgain: '',
        phoneTip: '',
        passwordTip: '',
    },

    doNothing() {},

    backLogin() {
        wx.navigateBack({
            delta: 1
        });
    },

    signUp() {
        if (!this.checkPhone()) {
            return;
        }
        if (!this.checkPassword()) {
            return;
        }
        const data = {
            phone: this.data.phone,
            username: this.data.username,
            password: this.data.password,
        };
        console.log(data);
        request.post(signup, data, false)
        .then((res) => {
            console.log("注册成功: ", res);
            wx.showToast({
                title: '注册成功',
                icon: 'none',
                success: () => {
                    wx.navigateBack({
                        delta: 1
                    });
                }
            });
        })
        .catch((err) => {
            console.log("注册失败: ", err);
            let msg = '注册失败';
            if (err.data && err.data.msg) {
                msg += (':' + err.data.msg);
            }
            wx.showToast({
                title: msg,
                icon: 'none',
            });
        });
    },

    // 检查手机号是否符合要求
    checkPhone() {
        const phone = this.data.phone;
        if (phone.length !== 11) {
            this.setData({
                phoneTip: '请输入11位手机号码'
            });
            return false;
        }
        if (isNaN(phone)) {
            this.setData({
                phoneTip: '请勿输入非数字内容'
            });
            return false;
        }
        this.setData({
            phoneTip: ''
        });
        return true;
    },

    // 检查两次输入是否一致
    checkPassword() {
        const password = this.data.password;
        const passwordAgain = this.data.passwordAgain;
        if (password === '' || passwordAgain === '') {
            return false;
        }
        if (password !== passwordAgain) {
            this.setData({
                passwordTip: '两次输入密码不一致'
            });
            return false;
        }
        this.setData({
            passwordTip: ''
        });
        return true;
    },
})