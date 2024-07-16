package com.jlu.webcommunity.entity.dto.user;

import lombok.Data;

@Data
public class RegisterDto {
    private String name;
    private String password;
    private String captcha;
    private String captchaKey;
}
