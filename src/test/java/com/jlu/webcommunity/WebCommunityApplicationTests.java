package com.jlu.webcommunity;

import com.jlu.webcommunity.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
class WebCommunityApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void test(){

    }
}
