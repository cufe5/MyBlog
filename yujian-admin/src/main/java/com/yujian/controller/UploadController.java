package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.exception.SystemException;
import com.yujian.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) {
        try {
            return uploadService.uploadImg(multipartFile);
        } catch (SystemException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传上传失败");
        }
    }
}

