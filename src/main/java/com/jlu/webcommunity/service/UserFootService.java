package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.post.GetPostByPageDto;
import com.jlu.webcommunity.entity.dto.userFoot.GetCollectPostByPageDto;
import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootCountDto;
import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootDto;
import com.jlu.webcommunity.entity.dto.userFoot.ModifyPostUserFootDto;
import com.jlu.webcommunity.entity.vo.post.GetPostByPageVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootCountVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootVo;

import java.util.List;

public interface UserFootService {
    GetPostUserFootVo getPostUserFoot(GetPostUserFootDto dto);
    Boolean modifyPostUserFoot(ModifyPostUserFootDto dto);
    void modifyPostUserFoot1(ModifyPostUserFootDto dto);
    GetPostUserFootCountVo getPostUserFootCount(GetPostUserFootCountDto dto);
    List<GetPostByPageVo> getCollectPostByPage(GetCollectPostByPageDto dto);
    void countUserFoot();
}
