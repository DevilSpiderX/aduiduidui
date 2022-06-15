package com.dali3a215.aduiduidui.controller.response;

import java.io.Serializable;
import java.util.Map;

public class ResultMap implements Serializable {
    private static final long serialVersionUID = -6491814391528291642L;
    private int code;
    private String msg;
    private Map<String, Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
