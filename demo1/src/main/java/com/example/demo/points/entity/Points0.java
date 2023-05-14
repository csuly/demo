package com.example.demo.points.entity;

import com.example.demo.points.entity.dto.PointsQuery;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "[0-points]")
@NoArgsConstructor
public class Points0 extends Points {
    public Points0(PointsQuery p) {
        super(p);
    }
}
