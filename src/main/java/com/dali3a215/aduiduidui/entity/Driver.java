package com.dali3a215.aduiduidui.entity;

import java.io.Serializable;

/**
 * (Driver)实体类
 *
 * @author makejava
 * @since 2022-06-15 11:34:53
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = -39219902576635890L;

    private Integer id;

    private String name;

    private Boolean enableCache;

    private Boolean autoRefreshCache;

    private Boolean enableSearch;

    private Boolean searchIgnoreCase;

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

    public Boolean getEnableCache() {
        return enableCache;
    }

    public void setEnableCache(Boolean enableCache) {
        this.enableCache = enableCache;
    }

    public Boolean getAutoRefreshCache() {
        return autoRefreshCache;
    }

    public void setAutoRefreshCache(Boolean autoRefreshCache) {
        this.autoRefreshCache = autoRefreshCache;
    }

    public Boolean getEnableSearch() {
        return enableSearch;
    }

    public void setEnableSearch(Boolean enableSearch) {
        this.enableSearch = enableSearch;
    }

    public Boolean getSearchIgnoreCase() {
        return searchIgnoreCase;
    }

    public void setSearchIgnoreCase(Boolean searchIgnoreCase) {
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

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enableCache=" + enableCache +
                ", autoRefreshCache=" + autoRefreshCache +
                ", enableSearch=" + enableSearch +
                ", searchIgnoreCase=" + searchIgnoreCase +
                ", maxSize=" + maxSize +
                ", usedSize=" + usedSize +
                ", title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

