package com.example.demo.points.entity;


import com.example.demo.points.entity.dto.PointsQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "[3223-points]")
@NoArgsConstructor
@Getter
@Setter
public class Points3223 extends Points{
    public Points3223(PointsQuery p)
    {
        super(p);
    }
}
