package com.dali3a215.aduiduidui.entity;

import org.springframework.http.MediaType;

import java.io.Serializable;
import java.nio.file.Path;

public class AduiFile implements Serializable {
    private static final long serialVersionUID = 3489044084795064334L;

    private String name;

    private Path path;

    private Driver driver;

    private boolean directory;

    private MediaType contentType;

    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path.toString().replace("\\", "/");
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDriver() {
        return driver.getId();
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getContentType() {
        return contentType.toString();
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
