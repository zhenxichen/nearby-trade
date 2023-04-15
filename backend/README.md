# 附近交易平台后端

## 项目结构

主要分为以下模块：
- nearby-common: 通用工具模块
- nearby-im: 即时通讯模块
- nearby-order: 订单业务模块
- nearby-api: 对外调用接口模块

## 技术选型
### 平台
- [腾讯云服务器](https://cloud.tencent.com/product/cvm)
- [腾讯云对象存储服务](https://cloud.tencent.com/product/cos)
选用COS服务主要原因是CVM（云服务器）的网络带宽受限，仅为1Mb/s

