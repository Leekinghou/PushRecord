package com.example.pushdocument.result;

import java.io.Serializable;

public class Result implements Serializable {
    private Integer code;
    private String message;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result ok() {
        return new Result(200, "推送成功");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}