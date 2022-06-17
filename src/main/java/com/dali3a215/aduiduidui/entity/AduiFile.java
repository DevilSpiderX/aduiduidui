package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

public class AduiFile implements Serializable {
    private static final long serialVersionUID = 3489044084795064334L;

    private String name;

    private String path;

    private boolean directory = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "AduiFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", directory=" + directory +
                '}';
    }
}
