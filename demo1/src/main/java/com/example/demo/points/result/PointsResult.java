package com.example.demo.points.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsResult {
    private String status;
    private String message;

    public PointsResult() {
    }

    public PointsResult(String message) {
        this.message=message;
    }

    public static PointsResult success(String message) {
        PointsResult pointsResult = new PointsResult(message);
        pointsResult.setStatus("success");
        return pointsResult;
    }

    public static PointsResult error(String message) {
        PointsResult pointsResult = new PointsResult(message);
        pointsResult.setStatus("fail");
        return pointsResult;
    }
}
