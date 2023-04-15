// components/commodityCard/index.js

const DETAIL_PATH = '/pages/commodity/commodity';

Component({
    /**
     * 组件的属性列表
     */
    properties: {
        image: {
            type: String,
            value: ''
        },
        title: {
            type: String,
            value: ''
        },
        avatar: {
            type: String,
            value: ''
        },
        seller: {
            type: String,
            value: ''
        },
        distance: {
            type: String,
            value: ''
        },
        comNumber: {
            type: String,
            value: ''
        }
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
            console.log("this.data", this.data);
            const url = DETAIL_PATH + '?id=' + this.data.comNumber;
            wx.navigateTo({
                url: url,
            });
        }
    }
})
