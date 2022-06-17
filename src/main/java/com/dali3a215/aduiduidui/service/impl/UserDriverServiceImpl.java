package com.dali3a215.aduiduidui.service.impl;


import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.entity.UserDriver;
import com.dali3a215.aduiduidui.exception.NotEnoughSpaceException;
import com.dali3a215.aduiduidui.service.DriverService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import com.dali3a215.aduiduidui.service.UserService;
import com.dali3a215.aduiduidui.util.AduiCipher;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service("userDriverService")
public class UserDriverServiceImpl implements UserDriverService {
    private final Logger logger = LoggerFactory.getLogger(UserDriverServiceImpl.class);
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();

    private final static double RESERVE_RATIO = 0.05;
    @Resource(name = "userService")
    private UserService userService;
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

    @Override
    public boolean allocateSpace(String uid, int driverID, String physicalPath) {
        Driver driver = driverService.get(driverID);

        if (driverService.getSpaceUsage(driver) > (1 - RESERVE_RATIO)) {
            return false;
        }

        UserDriver userDriver = new UserDriver();
        userDriver.setUid(uid);
        userDriver.setDriveId(driverID);
        userDriver.setPhysicalPath(physicalPath);

        return dao.insert(userDriver) == 1;
    }

    @Override
    public void checkAllSpace() {
        logger.info("开始检查用户空间，并分配空间");
        for (User user : userService.getUserList()) {
            checkUserSpace(user.getUid());
        }
        logger.info("检查结束");
    }

    @Override
    public void checkUserSpace(String uid) {
        UserDriver userDriver = new UserDriver();
        userDriver.setUid(uid);
        List<UserDriver> userDriverList = dao.select(userDriver);

        List<Integer> driverIds = new LinkedList<>();

        boolean flag = true;//给用户新增驱动器标志
        for (UserDriver var : userDriverList) {
            Driver driver = driverService.get(var.getDriveId());
            if (driverService.getSpaceUsage(driver) < 1 - RESERVE_RATIO) {
                flag = false;
            }
            driverIds.add(var.getDriveId());
        }

        if (flag) {
            List<Driver> allDriver = driverService.list();
            Driver thatDriver = null;
            for (Driver var : allDriver) {
                if (driverIds.contains(var.getId())) continue;
                thatDriver = var;
            }
            if (thatDriver == null) {
                String msg = String.format("所有驱动器空间都不足%.2f,共%d个驱动器,请添加新的驱动器",
                        (1 - RESERVE_RATIO) * 100, allDriver.size());
                NotEnoughSpaceException e = new NotEnoughSpaceException(msg);
                logger.error(e.getMessage(), e);
                return;
            }
            String physicalPath = AduiCipher.sha256Encrypt(uid + LocalDateTime.now());
            Path realPath = Paths.get(thatDriver.getValue(), physicalPath);
            File file = realPath.toFile();
            if (!file.exists()) {
                if (file.mkdir()) {
                    if (allocateSpace(uid, thatDriver.getId(), physicalPath)) {
                        logger.info(uid + ":Driver-" + thatDriver.getId() + "分配空间成功");
                    } else {
                        logger.error(uid + ":Driver-" + thatDriver.getId() + "分配空间失败");
                    }
                } else {
                    logger.error(uid + ":Driver-" + thatDriver.getId() + "创建目录失败");
                }
            } else if (file.isDirectory()) {
                if (allocateSpace(uid, thatDriver.getId(), physicalPath)) {
                    logger.info(uid + ":Driver-" + thatDriver.getId() + "分配空间成功");
                } else {
                    logger.error(uid + ":Driver-" + thatDriver.getId() + "分配空间失败");
                }
            } else {
                FileAlreadyExistsException e = new FileAlreadyExistsException(file.toString());
                logger.error(e.getMessage(), e);
            }
        }
    }


}
