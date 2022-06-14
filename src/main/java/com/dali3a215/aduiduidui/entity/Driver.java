package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (Driver)实体类
 *
 * @author makejava
 * @since 2022-06-14 23:11:03
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = 889742212169604540L;
    
    private Integer id;
    
    private String name;
    
    private Integer enableCache;
    
    private Integer autoRefreshCache;
    
    private Integer enableSearch;
    
    private Integer searchIgnoreCase;
    
    private Long maxSize;
    
    private Long usedSize;
    
    private String title;
    
    private String value;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEnableCache() {
        return enableCache;
    }

    public void setEnableCache(Integer enableCache) {
        this.enableCache = enableCache;
    }

    public Integer getAutoRefreshCache() {
        return autoRefreshCache;
    }

    public void setAutoRefreshCache(Integer autoRefreshCache) {
        this.autoRefreshCache = autoRefreshCache;
    }

    public Integer getEnableSearch() {
        return enableSearch;
    }

    public void setEnableSearch(Integer enableSearch) {
        this.enableSearch = enableSearch;
    }

    public Integer getSearchIgnoreCase() {
        return searchIgnoreCase;
    }

    public void setSearchIgnoreCase(Integer searchIgnoreCase) {
        this.searchIgnoreCase = searchIgnoreCase;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

