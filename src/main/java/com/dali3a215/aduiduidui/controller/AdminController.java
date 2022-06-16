package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.service.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Resource(name = "systemConfigService")
    private SystemConfigService systemConfigService;

    @PostMapping("/login")
    @ResponseBody
    public ResultMap<String> login(@RequestBody JSONObject reqBody, HttpSession session) {
        ResultMap<String> respResult = new ResultMap<>();
        String uid = reqBody.getString("uid");
        String password = reqBody.getString("password");
        if (uid != null && password != null) {
            String adminUid = systemConfigService.getAdminUid();
            String adminPassword = systemConfigService.getAdminPassword();
            if (uid.equals(adminUid) && password.equals(adminPassword)) {
                respResult.setCode(0);
                respResult.setMsg("登录成功");
                respResult.setData(new HashMap<>());
                respResult.getData().put("adminName", systemConfigService.getAdminName());

                session.setAttribute("admin", true);
                session.setAttribute("userStatus", "logged");
            } else {
                respResult.setCode(1);
                respResult.setMsg("账号密码错误");
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
        session.removeAttribute("admin");
        session.removeAttribute("userStatus");
        respResult.setCode(0);
        respResult.setMsg("退出成功");
        return respResult;
    }
}
