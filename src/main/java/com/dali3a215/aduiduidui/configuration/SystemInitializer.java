package com.dali3a215.aduiduidui.configuration;

import com.dali3a215.aduiduidui.service.SearchCacheService;
import com.dali3a215.aduiduidui.service.StatisticsService;
import com.dali3a215.aduiduidui.service.SystemConfigService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SystemInitializer implements CommandLineRunner {
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;
    @Resource(name = "statisticsService")
    private StatisticsService statisticsService;
    @Resource(name = "searchCacheService")
    private SearchCacheService searchCacheService;

    @Override
    public void run(String... args) throws Exception {
        systemConfigService.init();
        userDriverService.checkAllSpace();
        statisticsService.init();
        searchCacheService.cleanCacheByTime();
        searchCacheService.generateCache();
    }
}
