package com.jlu.webcommunity;

import com.jlu.webcommunity.dao.UserFollowDao;
import com.jlu.webcommunity.entity.vo.SelectUserFollowCountVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class WebCommunityApplicationTests {
    @Autowired
    private UserFollowDao userFollowDao;

    @Test
    public void test() {
        List<SelectUserFollowCountVo> list = userFollowDao.getBaseMapper().selectUserFollowCount();
        List<SelectUserFollowCountVo> list2 = userFollowDao.getBaseMapper().selectUserFollowerCount();
        System.out.println();
    }
}
