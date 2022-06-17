package com.dali3a215.aduiduidui.configuration;

import com.dali3a215.aduiduidui.service.UserDriverService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class AutoCheckAllSpaceTask {
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void AutoCheckAllSpace() {
        userDriverService.checkAllSpace();
    }
}
