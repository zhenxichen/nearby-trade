package com.zxchen.nearby.controllers;

import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.service.ISysUserService;
import com.zxchen.nearby.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping("/text")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String path = imageService.uploadImage(file);
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + "/" + path;
    }

    @RequestMapping("/account")
    public SysUser getAccount() {
        return sysUserService.selectUserByUid(100L);
    }
}
