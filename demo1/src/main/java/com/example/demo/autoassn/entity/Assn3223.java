package com.example.demo.autoassn.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "`3223-assn`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assn3223 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "`9001`")
    private int source1;

    @Column(name = "`9002`")
    private int source2;

    @Column(name = "accuracy")
    private double accuracy;
}
