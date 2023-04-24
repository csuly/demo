package com.example.demo.autoassn.result;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GerAssnlistResult {

    private String status;
    private String message;
    private JSONObject result;


    public GerAssnlistResult() {}

    public GerAssnlistResult(JSONArray data) {
        this.result=new JSONObject();
        this.result.put("data",data);
    }


    public static GerAssnlistResult success(JSONArray data) {
        GerAssnlistResult getPointsResult = new GerAssnlistResult(data);
        getPointsResult.setStatus("success");
        getPointsResult.setMessage("关联成功");
        return getPointsResult;
    }

    public static GerAssnlistResult error() {
        GerAssnlistResult gerAssnlistResult = new GerAssnlistResult();
        gerAssnlistResult.setStatus("fail");
        gerAssnlistResult.setMessage("关联失败");
        return gerAssnlistResult;
    }
}
