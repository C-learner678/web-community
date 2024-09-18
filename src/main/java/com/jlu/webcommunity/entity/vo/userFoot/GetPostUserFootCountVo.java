package com.jlu.webcommunity.entity.vo.userFoot;

import lombok.Data;

@Data
public class GetPostUserFootCountVo {
    private Long postId;
    private Integer readCnt;
    private Integer likeCnt;
    private Integer collectCnt;
}
