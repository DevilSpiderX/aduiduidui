package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (SystemConfig)实体类
 *
 * @author makejava
 * @since 2022-06-17 20:35:42
 */
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = -38703507922663321L;
    
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

    @Override
    public String toString() {
        return "SystemConfig{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

