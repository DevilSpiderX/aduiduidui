package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.DriverService;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import java.util.List;

@Service("driverService")
public class DriverServiceImpl implements DriverService {
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
    public int add(String name, boolean enable_cache, boolean auto_refresh_cache, boolean enable_search,
                   boolean search_ignore_case, long max_size, String title, String value) {
        Driver driver = new Driver();
        driver.setName(name);
        driver.setEnableCache(enable_search);
        driver.setAutoRefreshCache(auto_refresh_cache);
        driver.setEnableSearch(enable_search);
        driver.setSearchIgnoreCase(search_ignore_case);
        driver.setMaxSize(max_size);
        driver.setTitle(title);
        driver.setValue(value);
        return dao.insert(driver);
    }

    @Override
    public int delete(int id) {
        return dao.deleteById(Driver.class, id);
    }
}
