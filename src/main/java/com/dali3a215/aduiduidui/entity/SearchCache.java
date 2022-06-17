package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (SearchCache)实体类
 *
 * @author makejava
 * @since 2022-06-17 20:35:42
 */
public class SearchCache implements Serializable {
    private static final long serialVersionUID = -63881651736920502L;
    
    private Integer id;
    
    private String key;
    
    private String value;
    
    private Long size;
    
    private Integer driveId;
    
    private String uid;
    
    private Long timestamp;
    
    private String contentType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getDriveId() {
        return driveId;
    }

    public void setDriveId(Integer driveId) {
        this.driveId = driveId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "SearchCache{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", size=" + size +
                ", driveId=" + driveId +
                ", uid='" + uid + '\'' +
                ", timestamp=" + timestamp +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}

