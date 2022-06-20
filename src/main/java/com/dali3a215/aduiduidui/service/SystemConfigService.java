package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.SystemConfig;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface SystemConfigService {

    void init();

    String getValue(String key);

    void setValue(String key, String value, String remark);

    List<SystemConfig> list();

    int remove(String key);

    String getAdminUid();

    void setAdminUid(String uid);

    String getAdminPassword();

    void setAdminPassword(String password);

    String getAdminName();

    void setAdminName(String name);

    boolean isAdmin(HttpSession session);

    long getSearchCacheKeepTime();

    void setSearchCacheKeepTime(long keepTime);
}
