package com.zxchen.nearby.common.service.impl;

import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.service.ImageService;
import com.zxchen.nearby.common.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传图片服务的实现类
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    public String uploadImage(MultipartFile file) {
        System.out.println("upload");
        String format = DateFormatUtil.getCurrentDateSeparateBySlash();
        // 获取目录
        File folder = new File(uploadPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 重命名文件
        String oldFileName = file.getOriginalFilename();
        String extension
                = oldFileName.substring(oldFileName.lastIndexOf('.'));    // 获取扩展名
        String newFileName = UUID.randomUUID().toString() + extension;

        try {
            file.transferTo(new File(folder, newFileName));
            return format + "/" + newFileName;
        } catch (IOException e) {
            throw new CustomException("写入文件失败");
        }
    }
}
