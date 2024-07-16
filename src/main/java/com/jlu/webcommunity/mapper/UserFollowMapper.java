package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.UserFollow;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.vo.SelectUserFollowCountVo;
import com.jlu.webcommunity.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowMapper extends BaseMapper<UserFollow> {
    List<UserInfo> selectUserFollowByPage(
            @Param("followUserId") Long followUserId, @Param("pageParam") PageParam pageParam);
    List<UserInfo> selectUserFollowerByPage(
            @Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
    List<SelectUserFollowCountVo> selectUserFollowCount();
    List<SelectUserFollowCountVo> selectUserFollowerCount();
}
