package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.IncludeType;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserByUid(String uid) {
        if (uid == null) return null;
        User user = new User();
        user.setUid(uid);
        List<User> result = dao.select(user, 1);
        if (result.size() == 0) return null;
        return result.get(0);
    }

    @Override
    public User getUserWithInfo(String uid) {
        if (uid == null) return null;
        User user = new User();
        user.setUid(uid);
        List<User> result = dao.select(user, "username,sex,information");
        if (result.size() == 0) return null;
        user = result.get(0);
        return user;
    }

    @Override
    public String encrypt(String password) {
        StringBuilder resultBld = new StringBuilder();
        try {
            MessageDigest SHA256Digest = MessageDigest.getInstance("SHA-256");
            byte[] buff = SHA256Digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : buff) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    resultBld.append(0);
                }
                resultBld.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        return resultBld.toString();
    }

    @Override
    public boolean verify(String uid, String password) {
        User user = getUserByUid(uid);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(encrypt(password));
    }

    @Override
    public int register(String uid, String password, String username, String sex, String info) {
        User newUser = initNewUser(uid, encrypt(password), username, sex, info);
        return dao.insert(newUser, IncludeType.INCLUDE_EMPTY);
    }

    @Override
    public int update(String uid, String password, String username, String sex, String info) {
        if (password == null && username == null && sex == null && info == null) return 0;
        User newUser = new User();
        newUser.setUid(uid);
        newUser.setPassword(password == null || password.equals("") ? null : encrypt(password));
        newUser.setUsername(username);
        newUser.setSex(sex != null && sex.equals("") ? "M" : sex);
        newUser.setInformation(info);
        return dao.updateBy(newUser, "uid", IncludeType.INCLUDE_EMPTY);
    }

    private User initNewUser(String uid, String password, String userName, String sex, String info) {
        User newUser = new User();
        newUser.setUid(uid);
        newUser.setPassword(password);
        newUser.setUsername(userName == null ? "" : userName);
        newUser.setSex(sex == null || sex.equals("") ? "M" : sex);
        newUser.setInformation(info == null ? "" : info);
        return newUser;
    }

    @Override
    public boolean isLogged(HttpSession session) {
        return session.getAttribute("userStatus") != null &&
                session.getAttribute("userStatus").equals("logged");
    }

    @Override
    public List<User> getUserList() {
        return dao.select(new User());
    }
}
