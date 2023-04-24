package com.example.demo.features.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFeaturesResult {

    private String status;
    private String message;
    private Object result;


    public GetFeaturesResult() {
    }

    public GetFeaturesResult(Object result) {
        this.result=result;
    }

    public static GetFeaturesResult success(Object result) {
        GetFeaturesResult getPointsResult = new GetFeaturesResult(result);
        getPointsResult.setStatus("success");
        getPointsResult.setMessage("获取成功");
        return getPointsResult;
    }

    public static GetFeaturesResult error() {
        GetFeaturesResult getPointsResult = new GetFeaturesResult();
        getPointsResult.setStatus("fail");
        getPointsResult.setMessage("获取失败");
        return getPointsResult;
    }

}
