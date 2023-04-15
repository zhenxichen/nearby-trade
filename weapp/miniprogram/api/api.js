// api/api.js

const loginByPhone = '/login/phone';
const loginByWechat = '/login/wechat';

const signUp = '/signup';

const checkAuth = '/auth/check';

const settingInfo = '/setting/info';
const settingAvatar = '/setting/avatar';
const settingUsername = '/setting/username';
const settingPassword = '/setting/password';
const settingBindWechat = '/setting/wechat/bind';
const settingUnbindWechat = '/setting/wechat/unbind';
const settingPhone = '/setting/phone/update';
const settingPhoneVerificationCode = '/setting/phone/vc';

const commoditySell = '/commodity/sell';
const commodityFind = '/commodity/find';
const commoditySearch = '/commodity/search';
const commodityDetail = '/commodity/detail';
const commodityEdit = '/commodity/edit';
const commodityOff = '/commodity/off';
const commodityTradeInfo = '/commodity/trade/info';

const collectionAdd = '/collection/add';
const collectionCancel = '/collection/cancel';
const collectionList = '/collection/list';

const userInfoMine = '/user/info/mine';
const userInfo = '/user/info/info';
const userOnSell = '/user/info/selling';
const userInfoChat = '/user/info/chat/info';

const messageList = '/message/list';
const messageHistory = '/message/history';
const messageUnreadCount = '/message/unread';

const orderList = '/order/list';
const orderCreate = '/order/create';
const orderCancel = '/order/cancel';
const orderAccept = '/order/accept';
const orderConfirm = '/order/confirm';
const orderDetail = '/order/detail';
const orderRate = '/order/rate';

module.exports = {
    login: {
        phone: loginByPhone,
        wechat: loginByWechat
    },
    signup: signUp,
    auth: {
        check: checkAuth
    },
    setting: {
        info: settingInfo,
        avatar: settingAvatar,
        username: settingUsername,
        password: settingPassword,
        wechat: {
            bind: settingBindWechat,
            unbind: settingUnbindWechat
        },
        phone: {
            update: settingPhone,
            vc: settingPhoneVerificationCode,
        },
    },
    commodity: {
        sell: commoditySell,
        find: commodityFind,
        search: commoditySearch,
        detail: commodityDetail,
        edit: commodityEdit,
        off: commodityOff,
        tradeInfo: commodityTradeInfo,
    },
    collection: {
        add: collectionAdd,
        cancel: collectionCancel,
        list: collectionList
    },
    userInfo: {
        mine: userInfoMine,
        info: userInfo,
        onSell: userOnSell,
        chat: userInfoChat,
    },
    message: {
        list: messageList,
        history: messageHistory,
        unread: messageUnreadCount,
    },
    order: {
        list: orderList,
        create: orderCreate,
        cancel: orderCancel,
        accpet: orderAccept,
        confirm: orderConfirm,
        detail: orderDetail,
        rate: orderRate,
    },
}