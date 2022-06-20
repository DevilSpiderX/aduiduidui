package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.entity.Driver;
import com.dali3a215.aduiduidui.entity.SearchCache;
import com.dali3a215.aduiduidui.service.DriverService;
import com.dali3a215.aduiduidui.service.FileService;
import com.dali3a215.aduiduidui.service.SearchCacheService;
import com.dali3a215.aduiduidui.service.UserDriverService;
import com.dali3a215.aduiduidui.util.SuffixToMediaType;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    @Resource(name = "driverService")
    private DriverService driverService;
    @Resource(name = "searchCacheService")
    private SearchCacheService searchCacheService;

    @Override
    public List<AduiFile> list(String uid, String path) {
        final String finalPath = path == null ? "" : path;
        List<AduiFile> resultList = new LinkedList<>();
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            Driver driver = tu._1;
            String physicalPath = tu._2;
            Path userPath = Paths.get(driver.getValue(), physicalPath);
            Path realPath = Paths.get(driver.getValue(), physicalPath, finalPath);
            if (Files.exists(realPath) && Files.isDirectory(realPath)) {
                try (Stream<Path> childPaths = Files.list(realPath)) {
                    childPaths.forEach(childPath -> {
                        AduiFile file = new AduiFile();
                        String fileName = childPath.getFileName().toString();
                        file.setName(fileName);
                        file.setPath(childPath.subpath(userPath.getNameCount(), childPath.getNameCount()));
                        file.setDriver(driver);
                        file.setDirectory(Files.isDirectory(childPath));
                        if (!file.isDirectory()) file.setContentType(SuffixToMediaType.getMediaTypeByName(fileName));
                        try {
                            file.setSize(Files.size(childPath));
                        } catch (IOException e) {
                            file.setSize(-1L);
                        }
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
    public List<AduiFile> find(String uid, String fileName) {
        List<AduiFile> resultList = new LinkedList<>();
        if (fileName == null) return resultList;
        for (SearchCache cache : searchCacheService.get(fileName, uid)) {
            AduiFile file = new AduiFile();
            file.setName(fileName);
            file.setPath(Paths.get(cache.getValue()));
            file.setDriver(driverService.get(cache.getDriveId()));
            file.setDirectory(false);
            file.setContentType(MediaType.parseMediaType(cache.getContentType()));
            file.setSize(cache.getSize());
            resultList.add(file);
        }
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            Driver driver = tu._1;
            String physicalPath = tu._2;
            if (!driver.getEnableCache()) {
                Path userPath = Paths.get(driver.getValue(), physicalPath);
                Path realPath = Paths.get(driver.getValue(), physicalPath, "");
                resultList.addAll(_find(fileName, driver, userPath, realPath));
            }
        }
        return resultList;
    }

    private List<AduiFile> _find(String fileName, Driver driver, Path userPath, Path realPath) {
        List<AduiFile> resultList = new LinkedList<>();
        if (Files.isDirectory(realPath)) {
            try (Stream<Path> childPaths = Files.list(realPath)) {
                childPaths.forEach(childPath -> resultList.addAll(_find(fileName, driver, userPath, childPath)));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            if (fileName.equals(realPath.getFileName().toString())) {
                AduiFile file = new AduiFile();
                file.setName(fileName);
                file.setPath(realPath.subpath(userPath.getNameCount(), realPath.getNameCount()));
                file.setDriver(driver);
                file.setDirectory(false);
                file.setContentType(SuffixToMediaType.getMediaTypeByName(fileName));
                try {
                    file.setSize(Files.size(realPath));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                resultList.add(file);
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
        boolean result = true;
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            Driver driver = tu._1;
            String physicalPath = tu._2;
            Path realPath = Paths.get(driver.getValue(), physicalPath, path);
            boolean flag = _removeFile(realPath);
            if (!flag) flag = _removeDirectory(realPath);
            if (!flag) result = false;
        }
        return result;
    }

    private boolean _removeFile(Path realPath) {
        if (!Files.isRegularFile(realPath)) return false;
        try {
            Files.delete(realPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private boolean _removeDirectory(Path realPath) {
        if (!Files.isDirectory(realPath)) return false;
        try (Stream<Path> childPaths = Files.list(realPath)) {
            childPaths.forEach(childPath -> {
                if (Files.isDirectory(childPath)) {
                    _removeDirectory(childPath);
                }
                if (Files.isRegularFile(childPath)) {
                    _removeFile(childPath);
                }
            });
            Files.delete(realPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public int write(InputStream originIn, String uid, String path, long contentLength, MediaType contentType, boolean cover) {
        if (originIn == null || uid == null || path == null) return 2;

        //检查文件是否存在
        if (exist(uid, path)) {
            if (cover) {
                if (!remove(uid, path)) {
                    return 5;
                }
            } else {
                logger.info("cover参数为false且文件存在");
                return 4;
            }
        }

        if (contentType == null) contentType = SuffixToMediaType.getMediaTypeByName(path);
        userDriverService.checkUserSpace(uid);
        Path realPath = null;
        Driver driver = null;
        for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
            driver = tu._1;
            String physicalPath = tu._2;
            if (driver.getUsedSize() + contentLength > driver.getMaxSize()) continue;
            realPath = Paths.get(driver.getValue(), physicalPath, path);
        }
        if (realPath == null) {
            logger.error("用户{}所有驱动器空间都不足，请增加驱动器", uid);
            return 3;
        }

        Path parentPath = realPath.getParent();
        if (!Files.exists(parentPath)) {
            try {
                Files.createDirectories(parentPath);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return 1;
            }
        }


        //这里开始写入到文件中
        byte[] buff = new byte[1 << 14];
        long size = 0;
        try (OutputStream out = new FileOutputStream(realPath.toFile())) {
            while (size < contentLength) {
                int len = originIn.read(buff);
                if (len == -1) break;
                out.write(buff);
                out.flush();
                size += len;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return 1;
        }
        driverService.addUsedSize(driver.getId(), size);
        //添加缓存
        if (driver.getEnableCache()) {
            String fileName = realPath.getFileName().toString();
            Path relativePath = Paths.get(path);
            String value = relativePath.toString().replace(File.separatorChar, '/');
            if (value.startsWith("/")) value = value.substring(1);
            searchCacheService.add(fileName, value, contentType, size, uid, driver.getId());
        }
        return 0;
    }

    @Override
    public boolean read(HttpServletResponse resp, String uid, String path) {
        if (resp == null || uid == null || path == null) return false;
        String fileName = Paths.get(path).getFileName().toString();
        Driver driver = null;
        long contentLength = 0;
        List<SearchCache> cacheList = searchCacheService.get(fileName, uid);
        for (SearchCache cache : cacheList) {
            if (path.equals(cache.getValue())) {
                driver = driverService.get(cache.getDriveId());
                resp.setContentType(cache.getContentType());
                contentLength = cache.getSize();
                break;
            }
        }
        Path realPath = null;
        if (driver == null) {
            //缓存获取不到时，扫描驱动器获取
            if (!exist(uid, path)) {
                return false;
            }
            for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
                driver = tu._1;
                realPath = Paths.get(driver.getValue(), tu._2, path);
                if (Files.exists(realPath)) {
                    resp.setContentType(SuffixToMediaType.getMediaTypeByName(path).toString());
                }
            }
        } else {
            //从缓存获取
            realPath = Paths.get(driver.getValue(), uid, path);
            if (!Files.exists(realPath)) {
                //缓存获取的目录是不存在的时，再扫描驱动器获取
                if (!exist(uid, path)) {
                    return false;
                }
                for (Tuple2<Driver, String> tu : userDriverService.getDriverByUid(uid)) {
                    driver = tu._1;
                    realPath = Paths.get(driver.getValue(), tu._2, path);
                    if (Files.exists(realPath)) {
                        break;
                    }
                }
            }
        }
        if (realPath == null) return false;
        if (Files.isDirectory(realPath)) {
            logger.info("用户{}下载的是文件夹({})", uid, realPath.toString().replace(File.separatorChar, '/'));
            return false;
        }
        //这里开始读取文件到输出流中
        byte[] buff = new byte[1 << 14];
        long size = 0;
        try (InputStream in = new FileInputStream(realPath.toFile()); OutputStream originOut = resp.getOutputStream()) {
            int fileSize = in.available();
            if (contentLength != fileSize) contentLength = fileSize;
            resp.setContentLengthLong(contentLength);
            while (size < contentLength) {
                int len = in.read(buff);
                if (len == -1) break;
                originOut.write(buff);
                originOut.flush();
                size += len;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean createDirectory(String path, String uid) {
        try {
            Tuple2<Driver, String> tu = userDriverService.getDriverByUid(uid).get(0);
            if (path.startsWith("/")) path = path.substring(1);
            Path realPath = Paths.get(tu._1.getValue(), tu._2, path);
            Files.createDirectories(realPath);
        } catch (IndexOutOfBoundsException | IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
