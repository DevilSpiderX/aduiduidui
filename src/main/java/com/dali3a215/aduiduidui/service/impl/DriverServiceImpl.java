package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service("driverService")
public class DriverServiceImpl implements DriverService {
    private final Logger logger = LoggerFactory.getLogger(DriverServiceImpl.class);
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();

    @Override
    public List<Driver> list() {
        return dao.select(new Driver());
    }

    @Override
    public Driver get(long id) {
        return dao.selectById(new Driver(), id);
    }

    @Override
    public boolean add(String name, boolean enable_cache, boolean auto_refresh_cache, boolean enable_search,
                       boolean search_ignore_case, long max_size, String title, String value) {
        Driver driver = new Driver();
        driver.setName(name);
        driver.setEnableCache(enable_cache);
        driver.setAutoRefreshCache(auto_refresh_cache);
        driver.setEnableSearch(enable_search);
        driver.setSearchIgnoreCase(search_ignore_case);
        driver.setMaxSize(max_size);
        driver.setUsedSize(0L);
        driver.setTitle(title);
        driver.setValue(value);
        boolean result;
        Path path = Paths.get(value);
        Path parentPath = path.getParent();
        if (parentPath.toFile().exists()) {
            File file = path.toFile();
            if (!file.exists()) {
                if (file.mkdir()) {
                    result = dao.insert(driver) == 1;
                    if (result) {
                        logger.info("新建驱动器成功");
                    } else {
                        logger.error("新建驱动器失败");
                    }
                } else {
                    result = false;
                    logger.error("创建驱动器目录失败");
                }
            } else if (file.isDirectory()) {
                result = dao.insert(driver) == 1;
                if (result) {
                    logger.info("新建驱动器成功");
                } else {
                    logger.error("新建驱动器失败");
                }
            } else {
                result = false;
                FileAlreadyExistsException e = new FileAlreadyExistsException(file.toString());
                logger.error(e.getMessage(), e);
            }
        } else {
            result = false;
            FileNotFoundException e = new FileNotFoundException(parentPath.toString());
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        return dao.deleteById(Driver.class, id) == 1;
    }

    @Override
    public double getSpaceUsage(long id) {
        return getSpaceUsage(get(id));
    }

    @Override
    public double getSpaceUsage(Driver driver) {
        return BigDecimal.valueOf(driver.getUsedSize())
                .divide(BigDecimal.valueOf(driver.getMaxSize()), RoundingMode.HALF_UP)
                .doubleValue();
    }
}
