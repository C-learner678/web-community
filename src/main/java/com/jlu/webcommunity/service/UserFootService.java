package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootDto;
import com.jlu.webcommunity.entity.dto.userFoot.ModifyPostUserFootDto;
import com.jlu.webcommunity.entity.vo.GetPostUserFootVo;

public interface UserFootService {
    GetPostUserFootVo getPostUserFoot(GetPostUserFootDto dto);
    Boolean modifyPostUserFoot(ModifyPostUserFootDto dto);
    void modifyPostUserFoot1(ModifyPostUserFootDto dto);
}
