// pages/chat/chat.js

import request from '../../api/request.js';
import api from '../../api/api.js';
import cos from '../../api/cos.js';

const app = getApp();

const SEND_IMAGE_INDEX = 0;     // “发送图片”在动作面板中的位置
const CAMERA_INDEX = 1;         // “拍摄照片”在动作面板中的位置
const LOCATION_INDEX = 2;       // “发送位置”在动作面板中的位置

const messageType = {
    'text': 0,
    'image': 1,
    'location': 2,
    'ack': 3,
};

Page({

    /**
     * 页面的初始数据
     */
    data: {
        myUid: '',          // 用户的UID
        oppositeUid: '',    // 对方的UID
        oppositeName: '',   // 对方的用户名
        messageList: [],    // 消息列表
        myAvatar: '',           // 用户的头像url
        oppositeAvatar: '',     // 对方的头像url
        inputBottom: 0,        // 底端键盘的高度
        inputValue: '',         // 输入框输入的内容
        showActionSheet: false,     // 是否显示ActionSheet
        actionSheetList: [
            {
                index: SEND_IMAGE_INDEX,
                name: '发送图片',
            },
            {
                index: CAMERA_INDEX,
                name: '拍摄照片',
            },
            {
                index: LOCATION_INDEX,
                name: '发送位置',
            },
        ],
        sockTask: null,
        storageKey: '',
        firstLoad: false,   // 初次加载是否已完成
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        const storageKey = 'msg:' + app.globalData.uid + ':' + options.uid;
        this.setData({
            myUid: app.globalData.uid,
            oppositeUid: options.uid,
            sockTask: app.globalData.socketTask,
            storageKey: storageKey,
        });
        // 获取对话双方的用户信息
        this.getChatInfo();
        // 从缓存中读取之前的消息记录，并根据最后的缓存时间，发送request从服务端获取后续的消息
        this.getChatHistory();
    },

    onShow: function() {
        this.queryNewMessage();
    },

    onUnload: function() {
        // 将当前消息列表缓存
        this.cacheChatHistory();
    },

    // 响应接收的消息
    onMessage(data) {
        // 过滤非当前对话对象的消息
        if (data.from != this.data.oppositeUid && data.to != this.data.oppositeUid) {
            return;
        }
        this.renderMessage(data);
        this.sendAck(data);
    },

    setMockData() {
        const messageList = this.data.messageList;

        messageList.push(
            {
                uid: 'test',
                type: 'text',
                content: '一段从我发给对方的信息',
                time: '2021/11/18 12:00:03'
            },
            {
                uid: '125570',
                type: 'text',
                content: '对方回复的消息',
                time: '2021/11/18 12:00:05'
            },
            {
                uid: '125570',
                type: 'text',
                content: '一段相对来说比较长的信息，可以用于测试换行的显示效果是否正确',
                time: '2021/11/18 12:01:10'
            },
            {
                uid: '125570',
                type: 'image',
                content: 'https://img.yzcdn.cn/vant/cat.jpeg',
                time: '2021/11/18 12:02:12'
            },
        );
        this.setData({
            myUid: 'test',
            messageList: messageList,
            myAvatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
            oppositeAvatar: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
            oppositeName: '卖家的名字'
        });
    },

    // 将导航栏标题改为对方的名字
    setNavigationBar() {
        wx.setNavigationBarTitle({
            title: this.data.oppositeName
        });
    },

    // 获取聊天双方的用户信息
    getChatInfo() {
        const _this = this;
        request.get(api.userInfo.chat, { opposite: _this.data.oppositeUid })
        .then((res) => {
            const data = res.data.data;
            _this.setData({
                oppositeName: data.oppositeInfo.username,
                oppositeAvatar: data.oppositeInfo.avatar,
                myAvatar: data.myInfo.avatar
            });
            _this.setNavigationBar();
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        });
    },

    // 获取用户聊天记录
    getChatHistory() {
        const _this = this;
        // 本地缓存的聊天记录
        let localHistory = wx.getStorageSync(_this.data.storageKey);
        console.log("本地缓存读取结果: ", localHistory);

        // 获取最后一条消息的ID
        let lastId = 0;
        if (localHistory && localHistory.length && localHistory.length > 0) {
            lastId = localHistory[localHistory.length - 1].mid;
        } else {
            localHistory = [];
        }
        const data = {
            opposite: _this.data.oppositeUid,
            last: lastId
        };
        request.post(api.message.history, data)
        .then((res) => {
            const list = res.data.data;
            console.log("/message/history接口请求结果: ", list);
            for (let i = 0; i < list.length; i++) {
                localHistory.push({
                    mid: list[i].mid,
                    uid: list[i].from,
                    type: list[i].type,
                    content: _this.getContent(list[i]),
                    time: list[i].time
                });
            }
            _this.setData({
                messageList: localHistory,
                firstLoad: true,
            });
            _this.pageScrollToBottom();
        })
        .catch((err) => {
            _this.handleNetworkError(err);
            _this.setData({
                firstLoad: true
            });
        });
    },

    // 缓存聊天记录
    cacheChatHistory() {
        wx.setStorage({
            key: this.data.storageKey,
            data: this.data.messageList,
        });
    },

    // 输入框获得焦点时
    inputFocus(event) {
        this.setData({
            inputBottom: event.detail.height
        });
    },

    // 输入框失去焦点时
    inputBlur() {
        this.setData({
            inputBottom: 0
        });
    },

    // 响应点击输入框中的"+"按钮
    handleAdd() {
        this.setData({
            showActionSheet: true
        });
    },

    // 响应发送消息
    handleSubmit() {
        const data = {
            to: this.data.oppositeUid,
            type: messageType['text'],
            content: this.data.inputValue
        };
        console.log(data);
        this.data.sockTask.send({
            data: JSON.stringify(data),
            fail: (err) => {
                wx.showToast({
                    title: '消息发送失败，请稍后重试',
                    icon: 'none',
                });
                console.log("消息发送失败", err);
            },
        });
        this.setData({
            inputValue: ''
        });
    },
    
    // 响应ActionSheet取消事件
    handleActionSheetCancel() {
        this.closeActionSheet();
    },

    // 响应ActionSheet点击事件
    handleActionSheetClick(event) {
        this.closeActionSheet();
        switch (event.detail.index) {
            case SEND_IMAGE_INDEX: 
                this.sendImage();
                return;
            case CAMERA_INDEX: 
                this.takePhoto();
                return;
            case LOCATION_INDEX: 
                this.chooseLocation();
                return;
        }
    },

    // 响应点击ActionSheet的遮罩层
    handleClickOverlay() {
        this.closeActionSheet();
    },

    // 关闭动作面板
    closeActionSheet() {
        this.setData({
            showActionSheet: false
        });
    },

    // 将页面滚动至底部
    pageScrollToBottom() {
        wx.createSelectorQuery()
        .select('#chat-page')
        .boundingClientRect((rect) => {
            if (rect) {
                wx.pageScrollTo({
                    scrollTop: rect.height,
                    duration: 0,
                });
            }
        })
        .exec();
    },

    // 发送图片功能的相关逻辑
    sendImage() {
        this.closeActionSheet();
        wx.chooseImage({
            count: 1,
            sizeType: ['original','compressed'],
            sourceType: ['album'],
            success: (result)=>{
                this.sendImageInternal(result);
            },
            fail: (error)=>{
                console.log("选择图片失败: ", error);
            },
            complete: ()=>{}
        });
    },

    // 拍摄照片功能的相关逻辑
    takePhoto() {
        this.closeActionSheet();
        wx.chooseImage({
            count: 1,
            sizeType: ['original','compressed'],
            sourceType: ['camera'],
            success: (result)=>{
                this.sendImageInternal(result);
            },
            fail: (error)=>{
                console.log("拍摄照片失败: ", error);
            },
            complete: ()=>{}
        });
    },

    // 向后端发送图片的内部实现逻辑
    async sendImageInternal(res) {
        const filepath = res.tempFiles[0].path;
        const extension = filepath.substr(filepath.lastIndexOf('.'));
        const cosResult = await cos.uploadImage(filepath, extension);
        const imagePath = 'https://' + cosResult.Location;
        const data = {
            to: this.data.oppositeUid,
            type: messageType['image'],
            content: imagePath
        };
        this.data.sockTask.send({
            data: JSON.stringify(data),
            fail: (err) => {
                wx.showToast({
                    title: '消息发送失败，请稍后重试',
                    icon: 'none',
                });
                console.log("图片消息发送失败", err);
            }
        });
    },

    // 选择要发送的地理位置
    chooseLocation() {
        const _this = this;
        wx.navigateTo({
            url: '/pages/location/location',
            events: {
                locationChangeEvent: function(location) {
                    _this.sendLocation(location);
                }
            }
        });
    },

    // 讲选择的位置信息进行发送的逻辑
    sendLocation(location) {
        const locationStr = JSON.stringify(location);
        const sockTask = app.globalData.socketTask;
        const data = {
            to: this.data.oppositeUid,
            type: messageType['location'],
            content: locationStr
        };
        sockTask.send({
            data: JSON.stringify(data),
            success: (res) => {
                console.log(res);
            },
            fail: (err) => {
                wx.showToast({
                    title: '消息发送失败，请稍后重试',
                    icon: 'none',
                });
                console.log(err);
            },
            complete: ()=>{}
        });
    },

    // 处理网络错误的逻辑
    handleNetworkError(err) {
        console.log(err);
        if (err.data !== undefined && err.data.code === 401) {
            app.toLogin();
        }
    },

    // 读取content，若为位置类型消息需要对content进行json解析
    getContent(data) {
        if (data.type === 'location') {
            return JSON.parse(data.content);
        }
        return data.content;
    },

    // 将新接收到的消息渲染到聊天界面
    renderMessage(data) {
        const _messageList = this.data.messageList;
        const content = this.getContent(data);
        const message = {
            mid: data.mid,
            uid: data.from,
            type: data.type,
            content: content,
            time: data.time
        };
        _messageList.push(message);
        this.setData({
            messageList: _messageList
        });
        this.pageScrollToBottom();
    },

    // 向服务端返回ACK，表示消息已读，避免websocket消息在回到消息列表界面时仍显示未读的情况
    sendAck(data) {
        if (data.to != this.data.myUid) {
            return;
        }
        const sockTask = this.data.sockTask;
        const _data = {
            to: 0,      // 0表示系统消息
            type: messageType['ack'],
            content: data.mid.toString()
        };
        sockTask.send({
            data: JSON.stringify(_data),
            fail: (err)=>{
                console.log(err);
            }
        });
    },

    // 响应图片点击事件
    handleImageClick(event) {
        console.log(event);
        const imagePath = event.currentTarget.dataset.content;
        wx.previewImage({
            urls: [ imagePath ]
        });
    },

    // 响应位置点击事件
    handleLocationClick(event) {
        const content = event.currentTarget.dataset.content;
        wx.openLocation({
            latitude: content.lat,
            longitude: content.lng,
            scale: 18,
            name: content.title,
            address: content.address,
        });
    },

    // 向服务器请求新的消息（暂离页面期间可能有新的消息没有被渲染）
    queryNewMessage() {
        // 若首次加载未完成，不进行请求
        const _this = this;
        if (!this.data.firstLoad) {
            return;
        }
        let lastId = 0;
        const _messageList = this.data.messageList;
        if (_messageList && _messageList.length && _messageList.length > 0) {
            lastId = _messageList[_messageList.length - 1].mid;
        }
        const data = {
            opposite: _this.data.oppositeUid,
            last: lastId
        };
        request.post(api.message.history, data)
        .then((res) => {
            const list = res.data.data;
            console.log("/message/history接口请求结果: ", list);
            for (let i = 0; i < list.length; i++) {
                _messageList.push({
                    mid: list[i].mid,
                    uid: list[i].from,
                    type: list[i].type,
                    content: _this.getContent(list[i]),
                    time: list[i].time
                });
            }
            _this.setData({
                messageList: _messageList
            });
            this.pageScrollToBottom();
        })
        .catch((err) => {
            _this.handleNetworkError(err);
        });
    }
})