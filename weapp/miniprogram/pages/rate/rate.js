// pages/rate/rate.js

import request from "../../api/request";
import api from "../../api/api";

const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        rate: 0,
        comment: '',
        oid: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            oid: options.oid,
        });
    },

    handleChange(e) {
        this.setData({
            rate: e.detail
        });
    },

    handleSubmit() {
        console.log(this.data.comment);
        if (this.data.rate < 1) {
            wx.showToast({
                title: '请设置评分',
                icon: 'none'
            });
            return;
        }
        const data = {
            oid: this.data.oid,
            rate: this.data.rate,
        };
        request.post(api.order.rate, data)
        .then(() => {
            wx.showToast({
                title: '评价成功',
                duration: 500,
                success: () => {
                    wx.navigateBack({
                        delta: 1
                    });
                }
            })
        });
    }
})