package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {

    User getUserByUid(String uid);

    User getUserWithInfo(String uid);

    String encrypt(String password);

    boolean verify(String uid, String password);

    int register(String uid, String password, String username, String sex, String info);

    int update(String uid, String password, String username, String sex, String info);

    boolean isLogged(HttpSession session);
}
