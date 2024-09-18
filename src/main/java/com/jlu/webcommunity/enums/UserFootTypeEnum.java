package com.jlu.webcommunity.enums;

import lombok.Getter;

@Getter
public enum UserFootTypeEnum {
    READ(0),
    LIKE(1),
    COLLECT(2);

    private final int type;
    UserFootTypeEnum(int type){
        this.type = type;
    }
}
