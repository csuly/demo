package com.example.demo.features.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Features {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "batch")
    private int batch;
    @Column(name = "source")
    private int source;
    @Column(name = "time_min")
    private Double time_min;
    @Column(name = "time_max")
    private Double time_max;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "min_lon")
    private Double min_lon;

    @Column(name = "max_lon")
    private Double max_lon;

    @Column(name = "min_lat")
    private Double min_lat;
    @Column(name = "max_lat")
    private Double max_lat;
    @Column(name = "avg_vel")
    private Double avg_vel;
    @Column(name = "avg_accel")
    private Double avg_accel;
    @Column(name = "avg_cou")
    private Double avg_cou;
    @Column(name = "avg_anguvel")
    private Double avg_anguvel;
    @Column(name = "points")
    private Double points;
    @Column(name = "sparsity")
    private Double sparsity;
}
