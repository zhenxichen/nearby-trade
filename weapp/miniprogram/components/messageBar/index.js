// components/messageBar/index.js

const DEFAULT_PATH = '/pages/chat/chat'

Component({
    /**
     * 组件的属性列表
     */
    properties: {
        avatar: {
            type: String,
            value: ''
        },
        name: {
            type: String,
            value: ''
        },
        message: {
            type: String,
            value: ''
        },
        count: {
            type: Number,
            value: 0
        },
        uid: {
            type: String,
            value: ''
        },
    },

    /**
     * 组件的初始数据
     */
    data: {

    },

    /**
     * 组件的方法列表
     */
    methods: {
        handleClick() {
            const url = DEFAULT_PATH + '?uid=' + this.data.uid;
            wx.navigateTo({
                url: url
            });
        }
    }
})
