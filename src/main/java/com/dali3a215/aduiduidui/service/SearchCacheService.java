package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.SearchCache;
import org.springframework.http.MediaType;

import java.util.List;

public interface SearchCacheService {

    List<SearchCache> get(String key, String uid);

    void generateCache();

    boolean addCache(String key, String value, MediaType contentType, long size, String uid, int driveId);

    void cleanCacheByTime();

    void cleanCache();
}
