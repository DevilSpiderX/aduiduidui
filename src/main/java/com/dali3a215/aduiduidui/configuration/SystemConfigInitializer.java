package com.dali3a215.aduiduidui.configuration;

import com.dali3a215.aduiduidui.service.SystemConfigService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SystemConfigInitializer implements CommandLineRunner {
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;

    @Override
    public void run(String... args) throws Exception {
        systemConfigService.init();
        userDriverService.checkAllSpace();
    }
}
