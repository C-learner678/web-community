package com.jlu.webcommunity.task;

import com.jlu.webcommunity.service.ScoreService;
import com.jlu.webcommunity.service.UserFollowService;
import com.jlu.webcommunity.service.UserFootService;
import com.jlu.webcommunity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 这个定时任务类负责统计和更新缓存
@Slf4j
@Component
public class StatisticTask {
    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ScoreService scoreService;

    // 每天上午三点更新统计，确保mysql和redis一致性
    @Scheduled(cron = "0 0 3 ? * *") // cron表达式，意思是每天上午3点执行任务
    public void cron() {
        userFollowService.countUserFollow();
        userService.setAllUserStatus();
        userFootService.countUserFoot();
    }

    // 每天零点，清零每日得分
    @Scheduled(cron = "0 0 0 ? * *")
    public void cron1() {
        scoreService.clearDailyScore();
    }
}
