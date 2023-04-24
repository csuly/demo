package com.example.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    private String code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(Object data) {
        this.data = data;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result(data);
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
