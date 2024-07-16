package com.jlu.webcommunity.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Value("${image.image-path}")
    private String rootPath;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if(file == null){
            return null;
        }
        if(!Objects.equals(file.getContentType(), MediaType.IMAGE_JPEG.toString())){
            return null;
        }
        String fileName = UUID.randomUUID() + ".jpg";
        FileUtil.writeBytes(file.getBytes(), rootPath + fileName);
        return fileName;
    }
}
