// api/request.js

const app = getApp();

const request = (path, options, takeToken = true) => {
    return new Promise((resolve, reject) => {
        const url = app.globalData.baseUrl + path;
        const header = {
            'Content-Type':'application/json'
        };
        if (takeToken) {
            header['Authorization'] = 'Bearer ' + app.globalData.jwt;
        }
        wx.request({
            url: url,
            method: options.method,
            data: options.data,
            header: header,
            success: (result) => {
                if (result.data.code === 200) {
                    resolve(result);
                } else {
                    reject(result);
                }
            },
            fail: (error) => {
                reject(error);
            },
            complete: ()=>{}
        });
    })
}

const get = (path, data = {}, takeToken = true) => {
    return request(path, {
        method: 'GET',
        data: data
    }, takeToken);
}

const post = (path, data, takeToken = true) => {
    return request(path, {
        method: 'POST',
        data: JSON.stringify(data)
    }, takeToken);
}

module.exports = {
    request,
    get,
    post,
};