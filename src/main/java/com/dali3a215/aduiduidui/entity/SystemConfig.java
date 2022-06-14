package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (SystemConfig)实体类
 *
 * @author makejava
 * @since 2022-06-14 23:12:05
 */
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = -75581694194357130L;
    
    private Integer id;
    
    private String key;
    
    private String value;
    
    private String remark;


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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

