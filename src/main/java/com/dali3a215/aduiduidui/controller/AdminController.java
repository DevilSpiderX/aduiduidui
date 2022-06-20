package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.service.SystemConfigService;
import com.dali3a215.aduiduidui.util.AduiCipher;
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
            if (uid.equals(adminUid) && AduiCipher.sha256Encrypt(password).equals(adminPassword)) {
                respResult.setCode(0);
                respResult.setMsg("登录成功");
                respResult.setData(new HashMap<>());
                respResult.getData().put("adminName", systemConfigService.getAdminName());

                session.setAttribute("admin", true);
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
        respResult.setCode(0);
        respResult.setMsg("退出成功");
        return respResult;
    }

    @PostMapping("/adminUpdate")
    @ResponseBody
    public ResultMap<Void> adminUpdate(@RequestBody JSONObject reqBody) {
        ResultMap<Void> respResult = new ResultMap<>();
        String adminName = reqBody.getString("adminName");
        String adminUid = reqBody.getString("adminUid");
        String adminPassword = reqBody.getString("adminPassword");
        systemConfigService.setAdminName(adminName);
        systemConfigService.setAdminUid(adminUid);
        systemConfigService.setAdminPassword(adminPassword);
        respResult.setCode(0);
        respResult.setMsg("修改成功");
        return respResult;
    }

    @PostMapping("/updateSearchCacheKeepTime")
    @ResponseBody
    public ResultMap<Void> updateSearchCacheKeepTime(@RequestBody JSONObject reqBody) {
        ResultMap<Void> respResult = new ResultMap<>();
        long time = reqBody.getLongValue("time");
        systemConfigService.setSearchCacheKeepTime(time);
        respResult.setCode(0);
        respResult.setMsg("修改成功");
        return respResult;
    }

    @PostMapping("/setSystemConfig")
    @ResponseBody
    public ResultMap<Void> setSystemConfig(@RequestBody JSONObject reqBody) {
        ResultMap<Void> respResult = new ResultMap<>();
        String key = reqBody.getString("key");
        String value = reqBody.getString("value");
        String remark = reqBody.getString("remark");
        systemConfigService.setValue(key, value, remark);
        respResult.setCode(0);
        respResult.setMsg("添加成功");
        return respResult;
    }

    @PostMapping("/removeSystemConfig")
    @ResponseBody
    public ResultMap<Void> removeSystemConfig(@RequestBody JSONObject reqBody) {
        ResultMap<Void> respResult = new ResultMap<>();
        String key = reqBody.getString("key");
        int n = systemConfigService.remove(key);
        if (n == 0) {
            respResult.setCode(0);
            respResult.setMsg("删除成功");
        } else if (n == 1) {
            respResult.setCode(1);
            respResult.setMsg("删除失败");
        } else if (n == 2) {
            respResult.setCode(2);
            respResult.setMsg("禁止删除基础配置");
        }
        return respResult;
    }
}
