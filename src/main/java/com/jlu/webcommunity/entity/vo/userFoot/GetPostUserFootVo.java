package com.jlu.webcommunity.entity.vo.userFoot;

import lombok.Data;

@Data
public class GetPostUserFootVo {
    private Boolean readStatus;
    private Boolean likeStatus;
    private Boolean collectStatus;
}
