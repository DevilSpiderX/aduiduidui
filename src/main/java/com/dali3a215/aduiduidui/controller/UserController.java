package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultData;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.entity.User;
import com.dali3a215.aduiduidui.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Resource(name = "userService")
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public ResultMap login(@RequestBody JSONObject reqJson, HttpSession session) {
        ResultMap resultMap = new ResultMap();
        if (reqJson.containsKey("uid") && reqJson.containsKey("password")) {
            String uid = reqJson.getString("uid");
            String password = reqJson.getString("password");
            if (userService.verify(uid, password)) {
                session.setAttribute("userStatus", "logged");
                session.setAttribute("uid", uid);

                resultMap.setCode(0);
                resultMap.setMsg("登录成功");
            } else {
                resultMap.setCode(1);
                resultMap.setMsg("密码错误");
            }
        } else {
            resultMap.setCode(2);
            resultMap.setMsg("uid或者password不能为空");
        }
        return resultMap;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResultMap logout(HttpSession session) {
        ResultMap resultMap = new ResultMap();
        session.removeAttribute("userStatus");
        session.removeAttribute("uid");
        resultMap.setCode(0);
        resultMap.setMsg("退出成功");
        return resultMap;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultMap register(@RequestBody JSONObject reqJson) {
        ResultMap resultMap = new ResultMap();
        if (reqJson.containsKey("uid") && reqJson.containsKey("password")
                && !reqJson.getString("password").equals("")) {
            String uid = reqJson.getString("uid");
            String password = reqJson.getString("password");
            String userName = reqJson.getString("username");
            String sex = reqJson.getString("sex");
            String info = reqJson.getString("information");
            int result = userService.register(uid, password, userName, sex, info);
            if (result == 1) {
                resultMap.setCode(0);
                resultMap.setMsg("注册成功");
            } else {
                resultMap.setCode(1);
                resultMap.setMsg("注册失败");
            }
        } else {
            resultMap.setCode(2);
            resultMap.setMsg("uid或者password不能为空");
        }
        return resultMap;
    }

    @GetMapping("/info")
    @ResponseBody
    public ResultData<User> infoGet(@RequestParam String uid) {
        ResultData<User> resultData = new ResultData<>();
        User user = userService.getUserWithInfo(uid);
        if (user != null) {
            resultData.setCode(0);
            resultData.setMsg("获取成功");
            resultData.setData(user);
        } else {
            resultData.setCode(1);
            resultData.setMsg("用户不存在");
        }
        return resultData;
    }

    @PostMapping("/info")
    @ResponseBody
    public ResultData<User> infoPost(@RequestBody JSONObject reqJson) {
        return infoGet(reqJson.getString("uid"));
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultMap update(@RequestBody JSONObject reqJson, HttpSession session) {
        ResultMap resultMap = new ResultMap();
        String uid = (String) session.getAttribute("uid");
        String username = reqJson.getString("username");
        String password = reqJson.getString("password");
        String sex = reqJson.getString("sex");
        String information = reqJson.getString("information");
        int result = userService.update(uid, password, username, sex, information);
        if (result == 1) {
            resultMap.setCode(0);
            resultMap.setMsg("修改成功");
        } else {
            resultMap.setCode(1);
            resultMap.setMsg("修改失败");
        }
        return resultMap;
    }
}
