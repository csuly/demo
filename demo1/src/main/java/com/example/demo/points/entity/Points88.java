package com.example.demo.points.entity;

import com.example.demo.points.entity.dto.PointsQuery;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "[88-points]")
@NoArgsConstructor
public class Points88 extends Points {
    public Points88(PointsQuery p) {
        super(p);
    }
}
