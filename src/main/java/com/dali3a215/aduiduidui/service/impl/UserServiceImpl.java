package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.service.UserService;
import com.dali3a215.aduiduidui.util.AduiCipher;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.IncludeType;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;
import org.teasoft.honey.osql.core.ConditionImpl;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();

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
        user.setUid(uid);
        return user;
    }

    @Override
    public boolean verify(String uid, String password) {
        User user = getUserByUid(uid);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(AduiCipher.sha256Encrypt(password));
    }

    @Override
    public boolean register(String uid, String password, String username, String sex, String info) {
        User newUser = initNewUser(uid, AduiCipher.sha256Encrypt(password), username, sex, info);
        return dao.insert(newUser, IncludeType.INCLUDE_EMPTY) == 1;
    }

    @Override
    public boolean update(String uid, String password, String username, String sex, String info) {
        if (password == null && username == null && sex == null && info == null) return false;
        User newUser = new User();
        newUser.setUid(uid);
        newUser.setPassword(password == null || password.equals("") ? null : AduiCipher.sha256Encrypt(password));
        newUser.setUsername(username);
        newUser.setSex(sex != null && sex.equals("") ? "M" : sex);
        newUser.setInformation(info);
        return dao.updateBy(newUser, "uid", IncludeType.INCLUDE_EMPTY) == 1;
    }

    @Override
    public boolean delete(String uid) {
        int n = dao.delete(new User(), new ConditionImpl().op("uid", Op.eq, uid));
        return n == 1;
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
