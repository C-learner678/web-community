package com.jlu.webcommunity.enums;

import lombok.Getter;

@Getter
public enum UserFootTypeEnum {
    READ(1),
    LIKE(2),
    COLLECT(3);

    private final int type;
    UserFootTypeEnum(int type){
        this.type = type;
    }
}
