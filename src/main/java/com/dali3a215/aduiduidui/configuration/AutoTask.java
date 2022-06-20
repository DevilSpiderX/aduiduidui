package com.dali3a215.aduiduidui.configuration;

import com.dali3a215.aduiduidui.service.SearchCacheService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class AutoTask {
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;
    @Resource(name = "searchCacheService")
    private SearchCacheService searchCacheService;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void autoCheckAllSpace() {
        userDriverService.checkAllSpace();
    }

    @Scheduled(cron = "0 5 */1 * * ?")
    public void searchCacheAutoTask() {
        searchCacheService.cleanCacheByTime();
        searchCacheService.generateCache();
    }
}
