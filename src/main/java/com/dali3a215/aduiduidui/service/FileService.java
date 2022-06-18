package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.AduiFile;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileService {

    List<AduiFile> list(String uid, String path);

    boolean exist(String uid, String path);

    boolean remove(String uid, String path);

    boolean write(InputStream originIn, String uid, String path, long contentLength, MediaType contentType);

    boolean read(OutputStream originOut, String uid, String path);
}
