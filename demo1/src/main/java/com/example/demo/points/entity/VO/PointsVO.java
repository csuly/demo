package com.example.demo.points.entity.VO;

public record PointsVO(
        int id,
        int batch,
        int source,
        double time,
        double vel,
        double cou,
        double lat,
        double lon,
        double lat_len,
        double lon_len,
        double duration,
        double avg_accel,
        double avg_anguvel,
        double avg_cou,
        double sparsity,
        double avg_vel

) {
}

