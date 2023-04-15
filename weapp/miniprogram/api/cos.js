// api/cos.js

import request from 'request';

const COS = require('../libs/cos-wx-sdk-v5.js');

const BUCKET = 'zxchen-1301844697';
const REGION = 'ap-chengdu';

// 获取临时密钥
const getAuthorization = function(options, callback) {
    request.get('/cos/sts')
    .then((res) => {
        const data = res.data.data;
        const credentials = data.credentials;
        if (!data || !credentials) {
            return console.error("获取临时密钥失败");
        }
        callback({
            TmpSecretId: credentials.tmpSecretId,
            TmpSecretKey: credentials.tmpSecretKey,
            XCosSecurityToken: credentials.sessionToken,
            StartTime: data.startTime,
            ExpiredTime: data.expiredTime,
        });
    })
}

// 初始化COS对象
const cosInstance = new COS({
    getAuthorization: getAuthorization
});

// 从后端获取上传用文件名
const getFileName = function() {
    return new Promise((resolve, reject) => {
        request.get('/cos/path')
        .then((res) => {
            resolve(res.data.data);
        })
        .catch((err) => {
            console.log(err);
        })
    })
}

const uploadImage = function(filepath, extension) {
    return new Promise((resolve, reject) => {
        getFileName()
        .then((filename) => {
            cosInstance.postObject({
                Bucket: BUCKET,
                Region: REGION,
                Key: filename + extension,
                FilePath: filepath
            }, function(err, data) {
                console.log("err", err);
                if (err !== null && err !== undefined) {
                    console.log(err);
                    reject(err);
                }
                resolve(data);
            });
        });
    })
}


module.exports = {
    uploadImage
}

