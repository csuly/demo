package com.example.demo.points.entity;


import com.example.demo.points.entity.dto.PointsQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "[2802-points]")
@NoArgsConstructor
@Getter
@Setter
public class Points2802 extends Points {
    public Points2802(PointsQuery p)
    {
        super(p);
    }
}
