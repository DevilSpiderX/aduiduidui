package com.dali3a215.aduiduidui.service;

import javax.servlet.http.HttpSession;

public interface SystemConfigService {

    void init();

    String getValue(String key);

    void setValue(String key, String value, String remark);

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
