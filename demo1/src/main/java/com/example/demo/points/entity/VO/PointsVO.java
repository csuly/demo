package com.example.demo.points.entity.VO;

public record PointsVO(
        int batch,
        int source,
        double lat,
        double lon,
        double time,
        double vel,
        double cou,
        boolean highlight
) {
}

