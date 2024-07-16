package com.jlu.webcommunity.entity.dto.user;

import lombok.Data;

@Data
public class LoginDto {
    private String name;
    private String password;
    private String captcha; //验证码
    private String captchaKey; //验证码的key，存于redis中
}
