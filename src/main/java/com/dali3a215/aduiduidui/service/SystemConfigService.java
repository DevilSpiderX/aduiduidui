package com.dali3a215.aduiduidui.service;

import javax.servlet.http.HttpSession;

public interface SystemConfigService {

    void init();

    String getAdminUid();

    void setAdminUid(String uid);

    String getAdminPassword();

    void setAdminPassword(String password);

    String getAdminName();

    void setAdminName(String name);

    boolean isAdmin(HttpSession session);
}
