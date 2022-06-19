package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.entity.SearchCache;
import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.service.*;
import com.dali3a215.aduiduidui.util.SuffixToMediaType;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;
import org.teasoft.honey.osql.core.ConditionImpl;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service("searchCacheService")
public class SearchCacheServiceImpl implements SearchCacheService {
    private final Logger logger = LoggerFactory.getLogger(SearchCacheServiceImpl.class);
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();
    @Resource(name = "driverService")
    private DriverService driverService;
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;


    @Override
    public List<SearchCache> get(String key, String uid) {
        SearchCache cache = new SearchCache();
        cache.setKey(key);
        cache.setUid(uid);
        return dao.select(cache);
    }

    @Override
    public void generateCache() {
        long timestamp = System.currentTimeMillis();

        List<Driver> driverList = driverService.list();
        for (Driver driver : driverList) {
            if (!driver.getEnableCache() || !driver.getAutoRefreshCache()) continue;
            for (Tuple2<User, String> tu : userDriverService.getUserByDriveId(driver.getId())) {
                List<SearchCache> cacheList = _generateCache(driver, tu._1, tu._2, "");

                List<SearchCache> insertCacheList = new LinkedList<>();
                List<SearchCache> updateCacheList = new LinkedList<>();
                for (SearchCache searchCache : cacheList) {
                    Condition con = new ConditionImpl().op("key", Op.eq, searchCache.getKey()).and()
                            .op("value", Op.eq, searchCache.getValue()).and()
                            .op("content_type", Op.eq, searchCache.getContentType()).and()
                            .op("uid", Op.eq, searchCache.getUid()).and()
                            .op("drive_id", Op.eq, searchCache.getDriveId());
                    List<SearchCache> l = dao.select(new SearchCache(), con);
                    if (l.size() == 1) {
                        if (Objects.equals(searchCache.getSize(), l.get(0).getSize())) continue;
                        searchCache.setId(l.get(0).getId());
                        updateCacheList.add(searchCache);
                        cacheList.remove(searchCache);
                    } else {
                        if (l.size() > 1) dao.delete(new SearchCache(), con);
                        insertCacheList.add(searchCache);
                    }
                    searchCache.setTimestamp(timestamp);
                }
                int n = dao.insert(insertCacheList);
                logger.info("{}:Driver-{}新增{}个搜索缓存", tu._1.getUid(), driver.getId(), Math.max(n, 0));
                n = 0;
                for (SearchCache cache : updateCacheList) {
                    int un = dao.updateById(cache, null);
                    n += Math.max(un, 0);
                }
                logger.info("{}:Driver-{}更新{}个搜索缓存", tu._1.getUid(), driver.getId(), n);
            }
        }
    }

    private List<SearchCache> _generateCache(Driver driver, User user, String physicalPath, String currentPath) {
        List<SearchCache> cacheList = new LinkedList<>();
        Path userPath = Paths.get(driver.getValue(), physicalPath);
        Path path = Paths.get(driver.getValue(), physicalPath, currentPath);
        if (Files.exists(path) && Files.isDirectory(path)) {
            try (Stream<Path> childPaths = Files.list(path)) {
                childPaths.forEach(childPath -> {
                    Path relativePath = childPath.subpath(userPath.getNameCount(), childPath.getNameCount());
                    if (Files.isDirectory(childPath)) {
                        cacheList.addAll(_generateCache(driver, user, physicalPath, relativePath.toString()));
                    } else {
                        SearchCache cache = new SearchCache();
                        String fileName = childPath.getFileName().toString();
                        cache.setKey(fileName);
                        cache.setValue(relativePath.toString().replace("\\", "/"));
                        cache.setContentType(SuffixToMediaType.getMediaTypeByName(fileName).toString());
                        try {
                            cache.setSize(Files.size(childPath));
                        } catch (IOException e) {
                            cache.setSize(-1L);
                        }
                        cache.setUid(user.getUid());
                        cache.setDriveId(driver.getId());
                        cacheList.add(cache);
                    }
                });
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return cacheList;
    }

    @Override
    public boolean addCache(String key, String value, MediaType contentType, long size, String uid, int driveId) {
        long nowTimestamp = System.currentTimeMillis();
        SearchCache searchCache = new SearchCache();
        searchCache.setKey(key);
        if (value.startsWith("/")) value = value.substring(1);
        searchCache.setValue(value);
        searchCache.setContentType(contentType.toString());
        searchCache.setUid(uid);
        searchCache.setDriveId(driveId);

        List<SearchCache> l = dao.select(searchCache);
        int n;
        if (l.size() == 1) {
            searchCache.setId(l.get(0).getId());
            searchCache.setSize(size);
            searchCache.setTimestamp(nowTimestamp);
            n = dao.updateById(searchCache, null);
        } else {
            if (l.size() > 1) dao.delete(searchCache);
            searchCache.setSize(size);
            searchCache.setTimestamp(nowTimestamp);
            n = dao.insert(searchCache);
        }
        return n == 1;
    }

    @Override
    public void cleanCacheByTime() {
        long keepTime = systemConfigService.getSearchCacheKeepTime();
        long nowTimestamp = System.currentTimeMillis();
        List<SearchCache> cacheList = dao.select(new SearchCache());
        int n = 0;
        for (SearchCache cache : cacheList) {
            if (nowTimestamp - cache.getTimestamp() >= keepTime) {
                int dn = dao.deleteById(SearchCache.class, cache.getId());
                n += Math.max(dn, 0);
            }
        }
        logger.info("删除{}个过期缓存", n);
    }

    @Override
    public void cleanCache() {
        dao.delete(new SearchCache());
    }
}
