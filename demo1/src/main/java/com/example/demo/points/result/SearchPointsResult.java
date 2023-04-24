package com.example.demo.points.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPointsResult {

    private String status;
    private String message;

    private Object result;



    public SearchPointsResult() {
    }

    public SearchPointsResult(Object result) {
        this.result = result;
    }

    public static SearchPointsResult success() {
        SearchPointsResult searchPointsResult = new SearchPointsResult();
        searchPointsResult.setStatus("success");
        searchPointsResult.setMessage("查询成功");
        return searchPointsResult;
    }

    public static SearchPointsResult success(Object result) {
        SearchPointsResult searchPointsResult = new SearchPointsResult(result);
        searchPointsResult.setStatus("success");
        searchPointsResult.setMessage("查询成功");
        return searchPointsResult;
    }

    public static SearchPointsResult error() {
        SearchPointsResult searchPointsResult = new SearchPointsResult();
        searchPointsResult.setStatus("fail");
        searchPointsResult.setMessage("查询失败");
        return searchPointsResult;
    }
}
