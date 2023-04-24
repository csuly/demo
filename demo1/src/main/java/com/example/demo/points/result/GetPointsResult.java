package com.example.demo.points.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPointsResult {

    private String status;
    private String message;

    private Object result;
    private Object data;


    public GetPointsResult() {
    }

    public GetPointsResult(Object data, Object result) {
        this.result=result;
        this.data = data;
    }

    public static GetPointsResult success() {
        GetPointsResult getPointsResult = new GetPointsResult();
        getPointsResult.setStatus("success");
        getPointsResult.setMessage("获取成功");
        return getPointsResult;
    }

    public static GetPointsResult success(Object data, Object result) {
        GetPointsResult getPointsResult = new GetPointsResult(data,result);
        getPointsResult.setStatus("success");
        getPointsResult.setMessage("获取成功");
        return getPointsResult;
    }

    public static GetPointsResult error() {
        GetPointsResult getPointsResult = new GetPointsResult();
        getPointsResult.setStatus("fail");
        getPointsResult.setMessage("获取失败");
        return getPointsResult;
    }

}
