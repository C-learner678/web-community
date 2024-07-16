package com.jlu.webcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@MapperScan("com.jlu.webcommunity.mapper")
public class WebCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebCommunityApplication.class, args);
    }

}
