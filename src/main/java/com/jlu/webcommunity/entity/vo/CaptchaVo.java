package com.jlu.webcommunity.entity.vo;

import lombok.Data;

@Data
public class CaptchaVo {
    private String key;
    private String captchaImage; //Base64
}
