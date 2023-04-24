package com.example.demo.points.entity;


import com.example.demo.points.entity.dto.PointsQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "[1054-points]")
@NoArgsConstructor
@Getter
@Setter
public class Points1054 extends Points {
    public Points1054(PointsQuery p) {super(p);}
}
