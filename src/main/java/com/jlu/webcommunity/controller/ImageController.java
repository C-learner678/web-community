package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkBanned.CheckBanned;
import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.controller.result.Result;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Value("${image.image-path}")
    private String rootPath;

    @GetMapping("/getImage/{name}")
    public ResponseEntity<byte[]> getImage(@PathVariable String name) throws IOException {
        // 读取图片文件
        File file = new File(rootPath + name);
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        // 构建 HTTP 响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        // 返回包含图片字节数组的 ResponseEntity
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @CheckLogin
    @CheckBanned
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile file) throws IOException {
        String result = imageService.uploadImage(file);
        if(result != null){
            return Result.success(result);
        }else{
            return Result.fail(StatusCodeEnum.UPLOAD_IMAGE_FAILED);
        }
    }
}
