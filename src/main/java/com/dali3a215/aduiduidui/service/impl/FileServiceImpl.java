package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Service("fileService")
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public List<AduiFile> list(Driver driver, String physicalPath, String path) {
        if (path == null) path = "/";
        List<AduiFile> resultList = new LinkedList<>();
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
        return resultList;
    }
}
