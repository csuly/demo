package com.example.demo.features.result;

import com.example.demo.points.result.PointsResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutFeaturesResult {
    private String status;
    private String message;

    public PutFeaturesResult() {
    }

    public PutFeaturesResult(String message) {
        this.message=message;
    }

    public static PutFeaturesResult success(String message) {
        PutFeaturesResult putFeaturesResult = new PutFeaturesResult(message);
        putFeaturesResult.setStatus("success");
        return putFeaturesResult;
    }

    public static PutFeaturesResult error(String message) {
        PutFeaturesResult putFeaturesResult = new PutFeaturesResult(message);
        putFeaturesResult.setStatus("fail");
        return putFeaturesResult;
    }
}
