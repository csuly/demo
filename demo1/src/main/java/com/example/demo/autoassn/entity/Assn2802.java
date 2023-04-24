package com.example.demo.autoassn.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "`2802-assn`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assn2802 {
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

    public void setS1andS2andS3(int s1,int s2,int s3)
    {
        source1 = s1;
        source2 = s2;
        source3 = s3;
    }

    public Assn2802(Assn2802 assn1)
    {
        this.source1 = assn1.getSource1();
        this.source2 = assn1.getSource2();
        this.source3 = assn1.getSource3();
    }
}
