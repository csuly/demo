package com.example.demo.points.entity;

import com.example.demo.points.entity.dto.PointsQuery;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "[2619-points]")
@NoArgsConstructor
public class Points2619 extends Points {
    public Points2619(PointsQuery p) {
        super(p);
    }
}
