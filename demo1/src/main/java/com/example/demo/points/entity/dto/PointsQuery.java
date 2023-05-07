package com.example.demo.points.entity.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PointsQuery {
    private int id;
    private int scene;
    private int batch;
    private int source;
    private double time;
    private double lat;
    private double lon;
    private double vel;
    private double cou;
    private int sourceSize;
}
