package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.Driver;

import java.util.List;

public interface DriverService {
    List<Driver> list();
    Driver get(long id);

    int add(String name, boolean enable_cache, boolean auto_refresh_cache, boolean enable_search,
            boolean search_ignore_case, long max_size, String title, String value);

    int delete(int id);
}
