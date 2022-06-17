package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (UserDriver)实体类
 *
 * @author makejava
 * @since 2022-06-17 20:35:42
 */
public class UserDriver implements Serializable {
    private static final long serialVersionUID = -15914693773580247L;

    private Integer id;

    private String uid;

    private Integer driveId;

    private String physicalPath;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getDriveId() {
        return driveId;
    }

    public void setDriveId(Integer driveId) {
        this.driveId = driveId;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    @Override
    public String toString() {
        return "UserDriver{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", driveId=" + driveId +
                ", physicalPath='" + physicalPath + '\'' +
                '}';
    }
}

