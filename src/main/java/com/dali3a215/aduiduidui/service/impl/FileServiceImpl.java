package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.FileService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import com.dali3a215.aduiduidui.util.SuffixToMediaType;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Service("fileService")
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Resource(name = "userDriverService")
    private UserDriverService userDriverService;

    @Override
    public List<AduiFile> list(String uid, String path) {
        if (path == null) path = "/";
        List<AduiFile> resultList = new LinkedList<>();
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            Driver driver = tu._1;
            String physicalPath = tu._2;
            Path realPath = Paths.get(driver.getValue(), physicalPath, path);
            if (Files.isDirectory(realPath)) {
                try (Stream<Path> childPaths = Files.list(realPath)) {
                    childPaths.forEach(childPath -> {
                        AduiFile file = new AduiFile();
                        file.setName(childPath.getFileName().toString());
                        file.setPath(childPath.toAbsolutePath().toString().replace("\\", "/"));
                        file.setDirectory(Files.isDirectory(childPath));
                        resultList.add(file);
                    });
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return resultList;
    }

    @Override
    public boolean exist(String uid, String path) {
        if (path == null) throw new NullPointerException("Filepath is null");
        boolean result = false;
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            Driver driver = tu._1;
            String physicalPath = tu._2;
            Path realPath = Paths.get(driver.getValue(), physicalPath, path);
            result = Files.exists(realPath, LinkOption.NOFOLLOW_LINKS);
        }
        return result;
    }

    @Override
    public boolean remove(String uid, String path) {
        return false;
    }

    @Override
    public boolean write(InputStream originIn, String uid, String path, long contentLength, MediaType contentType) {
        if (originIn == null || uid == null || path == null) return false;
        if (contentType == null) {
            int n = path.lastIndexOf('.');
            String fileSuffix = path.substring(n);
            contentType = SuffixToMediaType.getMediaType(fileSuffix);
        }
        //未写完，把搜索缓存写完之后再写
        return false;
    }

    @Override
    public boolean read(OutputStream originOut, String uid, String path) {
        return false;
    }
}
