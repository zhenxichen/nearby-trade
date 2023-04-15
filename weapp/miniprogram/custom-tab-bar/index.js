import request from '../api/request.js';
import { message } from '../api/api.js';

const app = getApp();

Component({
    data: {
        current: 'homepage',
        paths: {
            'homepage': "/pages/index/index",
            'message': '/pages/message/message',
            'order': '/pages/order/order',
            'mine': '/pages/mine/mine'
        },
        messageCount: 0,
        messageDot: false
    },
    methods: {
        handleChange({ detail }) {
            this.setData({
                current: detail.key
            });
            wx.switchTab({
                url: this.data.paths[detail.key]
            });
        },
        updateMessageCount() {
            const haveUnread = app.globalData.unread;
            this.setData({
                messageDot: haveUnread
            });
        }
    }
})