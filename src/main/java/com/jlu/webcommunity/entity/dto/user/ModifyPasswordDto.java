package com.jlu.webcommunity.entity.dto.user;

import lombok.Data;

@Data
public class ModifyPasswordDto {
    private String oldPassword;
    private String newPassword;
}
