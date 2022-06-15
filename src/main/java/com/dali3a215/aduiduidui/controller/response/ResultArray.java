package com.dali3a215.aduiduidui.controller.response;

import java.io.Serializable;
import java.util.List;

public class ResultArray implements Serializable {
    private static final long serialVersionUID = 7798393073388894464L;
    private int code;
    private String msg;
    private List<Object> data;

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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
