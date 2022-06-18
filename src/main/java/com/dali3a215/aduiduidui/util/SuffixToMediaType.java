package com.dali3a215.aduiduidui.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SuffixToMediaType {
    private static final JSONObject data;

    static {
        InputStream in = ClassLoader.getSystemResourceAsStream("suffixToMediaType.json");
        JSONObject object = JSON.parseObject(in, StandardCharsets.UTF_8);
        data = object == null ? new JSONObject() : object;
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MediaType getMediaType(String suffix) {
        String typeStr = data.getString(suffix);
        if (typeStr == null) return MediaType.APPLICATION_OCTET_STREAM;
        try {
            return MediaType.parseMediaType(typeStr);
        } catch (InvalidMediaTypeException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
