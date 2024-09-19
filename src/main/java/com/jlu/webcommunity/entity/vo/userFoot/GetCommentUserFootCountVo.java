package com.jlu.webcommunity.entity.vo.userFoot;

import lombok.Data;

@Data
public class GetCommentUserFootCountVo {
    private Long commentId;
    private Integer likeCnt;
}
