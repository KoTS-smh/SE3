package com.sec.server.utils;

import com.sec.server.service.TaskService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
public class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Resource(name = "taskService")
    private TaskService taskService;

    @Scheduled(cron="0 0 4 * * ?")
    public void checkTaskEveryDay() {
        taskService.checkTaskEveryDay();
    }
}
