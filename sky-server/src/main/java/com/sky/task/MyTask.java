package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务
 */
//@Component
@Slf4j
public class MyTask {
    /**
     * 测试定时任务
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        log.info("测试定时任务：{}",new Date());
    }
}
