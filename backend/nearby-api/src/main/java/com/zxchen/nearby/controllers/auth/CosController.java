package com.zxchen.nearby.controllers.auth;

import com.tencent.cloud.Response;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.service.CosService;
import com.zxchen.nearby.common.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 腾讯云 COS 服务相关接口
 */
@RestController
@RequestMapping("/cos")
public class CosController {

    private CosService cosService;

    /**
     * 返回 STS（临时密钥）给前端
     *
     * @return 若操作成功，返回200，并返回腾讯云临时密钥；若登录态出现问题，将返回401
     *         临时密钥结构参考https://cloud.tencent.com/document/product/1312/48195
     */
    @RequestMapping("/sts")
    public HttpResult sts() {
        Response response = cosService.createCredential();
        return HttpResult.success(response);
    }

    /**
     * 生成一个日期和随机ID组成的存储路径给前端
     *
     * @return 操作成功返回200，并在data字段中存储生成的路径
     */
    @RequestMapping("/path")
    public HttpResult path() {
        String path = cosService.createFilePath();
        return HttpResult.success("生成路径成功", path);
    }

    @Autowired
    public void setCosStsService(CosService cosService) {
        this.cosService = cosService;
    }
}
