package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (UserDriver)实体类
 *
 * @author makejava
 * @since 2022-06-14 23:12:05
 */
public class UserDriver implements Serializable {
    private static final long serialVersionUID = 933917397825537548L;
    
    private Integer id;
    
    private String uid;
    
    private Integer driveId;
    
    private String virtualPath;
    
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

    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

}

