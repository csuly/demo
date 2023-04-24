package com.example.demo.autoassn.repository;

import com.example.demo.autoassn.entity.Assn1054;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Assn1054Repository extends JpaRepository<Assn1054,String> {
    public Assn1054 findBySource1AndSource2AndSource3AndSource4AndSource5(int a,
                                                                       int b,
                                                                       int c,
                                                                       int d,
                                                                       int e);
    public Assn1054 findBySource1(int a);
    public Assn1054 findBySource2(int a);
    public Assn1054 findBySource3(int a);
    public Assn1054 findBySource4(int a);
    public Assn1054 findBySource5(int a);
    public Boolean existsById(int s);

    public Assn1054 findById(int id);
}
