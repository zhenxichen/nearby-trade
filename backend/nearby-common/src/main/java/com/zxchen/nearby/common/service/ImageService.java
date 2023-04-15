package com.zxchen.nearby.common.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 用于处理图片上传的服务
 */
public interface ImageService {
    String uploadImage(MultipartFile file);
}
