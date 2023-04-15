// pages/location/location.js

const MapKey = require('../../constants/mapKey.js');

const key = MapKey.CHOOSE_LOCATION_KEY;
const referer = '附近交易平台';

const chooseLocation = requirePlugin('chooseLocation');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.navigateTo({
            url: `plugin://chooseLocation/index?key=${key}&referer=${referer}`
        });
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        const location = chooseLocation.getLocation();
        this.backfill(location);
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {
        chooseLocation.setLocation(null);
    },

    /**
     * 将腾讯位置服务地图选点返回的位置信息返回给调用界面
     * 
     * @param {*} e 返回的位置信息，包含字段如下
     * "name": 所选位置名称 "address": "所选位置地址" "latitude": 纬度 "longitude": 经度
     */
    backfill(e) {
        if (e === null) {
            return;
        }
        console.log(e);
        const location = {
            lat: e.latitude,
            lng: e.longitude,
            title: e.name,
            address: e.address,
        };
        console.log(location);
        // 向前一页面返回位置数据
        const eventChannel = this.getOpenerEventChannel();
        eventChannel.emit('locationChangeEvent', location);
        // 返回前一页面
        wx.navigateBack({
            delta: 1
        });
    }
})