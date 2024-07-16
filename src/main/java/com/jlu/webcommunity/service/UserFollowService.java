package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowByPageDto;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowerByPageDto;

import java.util.List;

public interface UserFollowService {
    boolean isFollow(Long userId, Long followUserId);
    boolean addFollow(Long userId);
    boolean removeFollow(Long userId);
    List<UserInfo> getFollow(GetUserFollowByPageDto dto);
    List<UserInfo> getFollower(GetUserFollowerByPageDto dto);
    Integer getFollowNum(Long userId);
    Integer getFollowerNum(Long userId);
    void countUserFollow();
}
