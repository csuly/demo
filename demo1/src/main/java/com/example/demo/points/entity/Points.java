package com.example.demo.points.entity;

import com.example.demo.points.entity.dto.PointsQuery;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class Points {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "batch")
    private int batch;

    @Column(name = "source")
    private int source;

    @Column(name = "time")
    private double time;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    @Column(name = "vel")
    private double vel;

    @Column(name = "cou")
    private double cou;

    public Points(PointsQuery p)
    {
        id=p.getId();
        batch=p.getBatch();
        source=p.getSource();
        time=p.getTime();
        lat=p.getLat();
        lon=p.getLon();
        cou=p.getCou();
    }
}

