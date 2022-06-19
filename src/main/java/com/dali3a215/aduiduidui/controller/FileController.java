package com.dali3a215.aduiduidui.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dali3a215.aduiduidui.controller.response.ResultArray;
import com.dali3a215.aduiduidui.controller.response.ResultMap;
import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.service.FileService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/api/file")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

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
        int flag;
        try {
            InputStream in = req.getInputStream();
            String uid = (String) session.getAttribute("uid");
            String path = req.getHeader("path");
            long contentLength = req.getContentLengthLong();
            MediaType mediaType = req.getContentType() == null ? null : MediaType.parseMediaType(req.getContentType());
            boolean cover = Boolean.parseBoolean(req.getHeader("cover"));
            flag = fileService.write(in, uid, path, contentLength, mediaType, cover);
        } catch (IOException e) {
            flag = 1;
            logger.error(e.getMessage(), e);
        }
        switch (flag) {
            case 0: {
                respResult.setCode(0);
                respResult.setMsg("上传成功");
                break;
            }
            case 1: {
                respResult.setCode(1);
                respResult.setMsg("上传失败");
                break;
            }
            case 2: {
                respResult.setCode(2);
                respResult.setMsg("参数不能为空");
                break;
            }
            case 3: {
                respResult.setCode(3);
                respResult.setMsg("驱动器空间不足");
                break;
            }
            case 4: {
                respResult.setCode(4);
                respResult.setMsg("cover参数为false且文件存在");
                break;
            }
            case 5: {
                respResult.setCode(5);
                respResult.setMsg("文件无法覆盖");
                break;
            }
        }
        return respResult;
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, @RequestParam String path, HttpServletResponse resp,
                         HttpSession session) throws IOException {
        String uid = (String) session.getAttribute("uid");
        boolean flag = fileService.read(resp, uid, fileName, path);
        if (!flag) {
            resp.sendError(404);
        }
    }
}
