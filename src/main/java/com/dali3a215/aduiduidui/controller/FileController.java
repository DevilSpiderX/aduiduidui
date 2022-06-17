package com.dali3a215.aduiduidui.controller;

import com.dali3a215.aduiduidui.controller.response.ResultArray;
import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.FileService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import io.vavr.Tuple2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Controller
@RequestMapping("/api/file")
public class FileController {
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;
    @Resource(name = "fileService")
    private FileService fileService;

    @GetMapping("/list")
    public ResultArray<AduiFile> list(@RequestParam String path, HttpSession session) {
        ResultArray<AduiFile> respResult = new ResultArray<>();
        respResult.setData(new LinkedList<>());
        String uid = (String) session.getAttribute("uid");
        for (Tuple2<Driver, String> var : userDriverService.getDriverByUid(uid)) {
            respResult.getData().addAll(fileService.list(var._1, var._2, path));
        }
        return respResult;
    }
}
