package com.example.demo.autoassn.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "`1054-assn`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assn1054 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "`9001`")
    private int source1;

    @Column(name = "`9002`")
    private int source2;

    @Column(name = "`9003`")
    private int source3;

    @Column(name = "`9004`")
    private int source4;

    @Column(name = "`9005`")
    private int source5;

    @Column(name = "accuracy")
    private double accuracy;


    public Assn1054(Assn1054 a) {
        this.source1 = a.getSource1();
        this.source2 = a.getSource2();
        this.source3 = a.getSource3();
        this.source4 = a.getSource4();
        this.source5 = a.getSource5();
    }

    public void setAssn3(int a, int b, int c, int d, int e)
    {
        source1 = a;
        source2 = b;
        source3 = c;
        source4 = d;
        source5 = e;
    }
}
