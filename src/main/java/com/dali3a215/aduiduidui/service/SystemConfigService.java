package com.dali3a215.aduiduidui.service;

public interface SystemConfigService {

    void init();

    String getAdminUid();

    void setAdminUid(String uid);

    String getAdminPassword();

    void setAdminPassword(String password);

    String getAdminName();

    void setAdminName(String name);
}
