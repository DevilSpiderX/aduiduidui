package com.dali3a215.aduiduidui.service;

public interface StatisticsService {
    void init();

    void uploadSizeAdd(String key, long size);

    void downloadSizeAdd(String key, long size);

    void fileSizeAdd(String key, long size);

    void fileSizeSubtract(String key, long size);
}
