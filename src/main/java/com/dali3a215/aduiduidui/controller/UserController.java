package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultArray;
import com.dali3a215.aduiduidui.controller.response.ResultData;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Resource(name = "userService")
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public ResultMap<Void> login(@RequestBody JSONObject reqJson, HttpSession session) {
        ResultMap<Void> respResult = new ResultMap<>();
        if (reqJson.containsKey("uid") && reqJson.containsKey("password")) {
            String uid = reqJson.getString("uid");
            String password = reqJson.getString("password");
            if (userService.verify(uid, password)) {
                session.setAttribute("userStatus", "logged");
                session.setAttribute("uid", uid);

                respResult.setCode(0);
                respResult.setMsg("登录成功");
            } else {
                respResult.setCode(1);
                respResult.setMsg("密码错误");
            }
        } else {
            respResult.setCode(2);
            respResult.setMsg("uid或者password不能为空");
        }
        return respResult;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResultMap<Void> logout(HttpSession session) {
        ResultMap<Void> respResult = new ResultMap<>();
        session.removeAttribute("userStatus");
        session.removeAttribute("uid");
        respResult.setCode(0);
        respResult.setMsg("退出成功");
        return respResult;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultMap<Void> register(@RequestBody JSONObject reqJson) {
        ResultMap<Void> respResult = new ResultMap<>();
        if (reqJson.containsKey("uid") && reqJson.containsKey("password")
                && !reqJson.getString("password").equals("")) {
            String uid = reqJson.getString("uid");
            String password = reqJson.getString("password");
            String userName = reqJson.getString("username");
            String sex = reqJson.getString("sex");
            String info = reqJson.getString("information");
            if (userService.register(uid, password, userName, sex, info)) {
                respResult.setCode(0);
                respResult.setMsg("注册成功");
            } else {
                respResult.setCode(1);
                respResult.setMsg("注册失败");
            }
        } else {
            respResult.setCode(2);
            respResult.setMsg("uid或者password不能为空");
        }
        return respResult;
    }

    @GetMapping("/info")
    @ResponseBody
    public ResultData<User> infoGet(@RequestParam String uid) {
        ResultData<User> respResult = new ResultData<>();
        User user = userService.getUserWithInfo(uid);
        if (user != null) {
            respResult.setCode(0);
            respResult.setMsg("获取成功");
            respResult.setData(user);
        } else {
            respResult.setCode(1);
            respResult.setMsg("用户不存在");
        }
        return respResult;
    }

    @PostMapping("/info")
    @ResponseBody
    public ResultData<User> infoPost(@RequestBody JSONObject reqJson) {
        return infoGet(reqJson.getString("uid"));
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultMap<Void> update(@RequestBody JSONObject reqJson, HttpSession session) {
        ResultMap<Void> respResult = new ResultMap<>();
        String uid = (String) session.getAttribute("uid");
        String username = reqJson.getString("username");
        String password = reqJson.getString("password");
        String sex = reqJson.getString("sex");
        String information = reqJson.getString("information");
        if (userService.update(uid, password, username, sex, information)) {
            respResult.setCode(0);
            respResult.setMsg("修改成功");
        } else {
            respResult.setCode(1);
            respResult.setMsg("修改失败");
        }
        return respResult;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultMap<Void> delete(@RequestBody JSONObject reqJson) {
        ResultMap<Void> respResult = new ResultMap<>();
        String uid = reqJson.getString("uid");
        if (userService.delete(uid)) {
            respResult.setCode(0);
            respResult.setMsg("删除成功");
        } else {
            respResult.setCode(1);
            respResult.setMsg("删除失败");
        }
        return respResult;
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResultArray<User> list() {
        ResultArray<User> respResult = new ResultArray<>();
        respResult.setCode(0);
        respResult.setMsg("获取成功");
        List<User> users = userService.getUserList();
        for (User user : users) {
            user.setPassword(null);
        }
        respResult.setData(users);
        return respResult;
    }
}
