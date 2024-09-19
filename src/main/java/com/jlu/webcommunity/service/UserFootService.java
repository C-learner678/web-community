package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.userFoot.*;
import com.jlu.webcommunity.entity.vo.post.GetPostByPageVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootCountVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootVo;

import java.util.List;

public interface UserFootService {
    GetPostUserFootVo getPostUserFoot(GetPostUserFootDto dto);
    Boolean modifyPostUserFoot(ModifyPostUserFootDto dto);
    void modifyPostUserFoot1(ModifyPostUserFootDto dto);
    List<GetPostByPageVo> getCollectPostByPage(GetCollectPostByPageDto dto);
    GetPostUserFootCountVo getPostUserFootCount(GetPostUserFootCountDto dto);
    Boolean modifyCommentUserFoot(ModifyCommentUserFootDto dto);
    void countUserFoot();
}
