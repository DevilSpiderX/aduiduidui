package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultArray;
import com.dali3a215.aduiduidui.controller.response.ResultData;
import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api/driver")
public class DriverController {
    @Resource(name = "driverService")
    private DriverService driverService;

    @RequestMapping("/list")
    @ResponseBody
    public ResultArray<Driver> list() {
        ResultArray<Driver> respResult = new ResultArray<>();
        respResult.setCode(0);
        respResult.setMsg("获取成功");
        respResult.setData(driverService.list());
        return respResult;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultData<Void> add(@RequestBody JSONObject reqBody) {
        ResultData<Void> respResult = new ResultData<>();

        String[] keys = new String[]{"name", "enable_cache", "auto_refresh_cache", "enable_search",
                "search_ignore_case", "max_size", "title", "value"};
        for (String key : keys) {
            if (!reqBody.containsKey(key)) {
                respResult.setCode(2);
                respResult.setMsg(key + "参数不存在");
                return respResult;
            }
        }

        String name = reqBody.getString(keys[0]);
        boolean enable_cache = reqBody.getBooleanValue(keys[1], false);
        boolean auto_refresh_cache = reqBody.getBooleanValue(keys[2], false);
        boolean enable_search = reqBody.getBooleanValue(keys[3], true);
        boolean search_ignore_case = reqBody.getBooleanValue(keys[4], false);
        long max_size = reqBody.getLongValue(keys[5]);
        String title = reqBody.getString(keys[6]);
        String value = reqBody.getString(keys[7]);
        if (driverService.add(name, enable_cache, auto_refresh_cache, enable_search, search_ignore_case, max_size,
                title, value)) {
            respResult.setCode(0);
            respResult.setMsg("添加成功");
        } else {
            respResult.setCode(1);
            respResult.setMsg("添加失败");
        }
        return respResult;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultData<Void> delete(@RequestBody JSONObject reqBody) {
        ResultData<Void> respResult = new ResultData<>();

        if (!reqBody.containsKey("id")) {
            respResult.setCode(2);
            respResult.setMsg("id参数不能为空");
            return respResult;
        }

        int id = reqBody.getIntValue("id");
        if (driverService.delete(id)) {
            respResult.setCode(0);
            respResult.setMsg("删除成功");
        } else {
            respResult.setCode(1);
            respResult.setMsg("删除失败");
        }
        return respResult;
    }
}
