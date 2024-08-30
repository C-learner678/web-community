package com.jlu.webcommunity.entity.dto.userFoot;

import com.jlu.webcommunity.enums.UserFootTypeEnum;
import lombok.Data;

@Data
public class ModifyPostUserFootDto {
    private Long postId;
    private UserFootTypeEnum type;
    private Boolean positive; // 要设置的状态
}
