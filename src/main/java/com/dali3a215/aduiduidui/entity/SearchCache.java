package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (SearchCache)实体类
 *
 * @author makejava
 * @since 2022-06-15 11:34:53
 */
public class SearchCache implements Serializable {
    private static final long serialVersionUID = 477955729869588195L;

    private Integer id;

    private String key;

    private String value;

    private String contentType;

    private Long size;

    private Integer driveId;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    @Override
    public String toString() {
        return "SearchCache{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", driveId=" + driveId +
                '}';
    }
}

