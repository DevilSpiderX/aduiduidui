package com.dali3a215.aduiduidui.service.impl;


import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.entity.UserDriver;
import com.dali3a215.aduiduidui.service.DriverService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service("userDriverService")
public class UserDriverServiceImpl implements UserDriverService {
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();
    @Resource(name = "driverService")
    private DriverService driverService;

    @Override
    public List<Tuple2<Driver, String>> getDriverByUid(String uid) {
        List<Tuple2<Driver, String>> result = new LinkedList<>();
        UserDriver userDriver = new UserDriver();
        userDriver.setUid(uid);
        List<UserDriver> userDrivers = dao.select(userDriver);
        for (UserDriver var : userDrivers) {
            result.add(Tuple.of(driverService.get(var.getDriveId()), var.getPhysicalPath()));
        }
        return result;
    }
}
