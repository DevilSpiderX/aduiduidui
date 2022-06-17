package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {

    User getUserByUid(String uid);

    User getUserWithInfo(String uid);

    boolean verify(String uid, String password);

    boolean register(String uid, String password, String username, String sex, String info);

    boolean update(String uid, String password, String username, String sex, String info);

    boolean delete(String uid);

    boolean isLogged(HttpSession session);

    List<User> getUserList();
}
