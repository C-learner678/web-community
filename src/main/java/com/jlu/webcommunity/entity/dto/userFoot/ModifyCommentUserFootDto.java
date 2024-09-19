package com.jlu.webcommunity.entity.dto.userFoot;

import lombok.Data;

// 只有喜欢，没有已读和收藏，所以不需要type属性
@Data
public class ModifyCommentUserFootDto {
    private Long commentId;
    private Boolean positive; // 要设置的状态
}
