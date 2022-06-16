package com.dali3a215.aduiduidui.configuration;

import com.dali3a215.aduiduidui.service.SystemConfigService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SystemConfigInitializer implements CommandLineRunner {
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;

    @Override
    public void run(String... args) throws Exception {
        systemConfigService.init();
    }
}
