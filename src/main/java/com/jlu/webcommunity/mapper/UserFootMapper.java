package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.core.PageParam;
import com.jlu.webcommunity.entity.UserFoot;
import com.jlu.webcommunity.entity.dto.userFoot.GetCollectPostByPageDto;
import com.jlu.webcommunity.entity.vo.post.GetPostByPageVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetCommentUserFootCountVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFootMapper extends BaseMapper<UserFoot> {
    List<GetPostUserFootCountVo> selectPostFootCount();
    List<GetPostByPageVo> selectCollectPostByPage(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
    List<GetCommentUserFootCountVo> selectCommentFootCount();
}
