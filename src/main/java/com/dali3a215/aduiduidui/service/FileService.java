package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.AduiFile;
import com.dali3a215.aduiduidui.entity.Driver;

import java.util.List;

public interface FileService {

    List<AduiFile> list(Driver driver, String physicalPath, String path);
}
