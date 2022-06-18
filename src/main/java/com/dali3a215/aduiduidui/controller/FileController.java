package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultArray;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.service.FileService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/api/file")
public class FileController {
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;
    @Resource(name = "fileService")
    private FileService fileService;

    @GetMapping("/list")
    @ResponseBody
    public ResultArray<AduiFile> listGet(@RequestParam(required = false) String path, HttpSession session) {
        ResultArray<AduiFile> respResult = new ResultArray<>();
        String uid = (String) session.getAttribute("uid");
        respResult.setCode(0);
        respResult.setMsg("获取成功");
        respResult.setData(fileService.list(uid, path));
        return respResult;
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultArray<AduiFile> listPost(@RequestBody JSONObject reqBody, HttpSession session) {
        return listGet(reqBody.getString("path"), session);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResultMap<Void> upload(HttpServletRequest req, HttpSession session) {
        ResultMap<Void> respResult = new ResultMap<>();
        boolean flag;
        try {
            InputStream in = req.getInputStream();
            String uid = (String) session.getAttribute("uid");
            String path = req.getHeader("path");
            long contentLength = req.getContentLengthLong();
            MediaType mediaType = req.getContentType() == null ? null : MediaType.parseMediaType(req.getContentType());
            flag = fileService.write(in, uid, path, contentLength, mediaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (flag) {
            respResult.setCode(0);
            respResult.setMsg("上传成功");
        } else {
            respResult.setCode(1);
            respResult.setMsg("上传失败");
        }
        return respResult;
    }

}
