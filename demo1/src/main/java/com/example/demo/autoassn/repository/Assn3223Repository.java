package com.example.demo.autoassn.repository;

import com.example.demo.autoassn.entity.Assn3223;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Assn3223Repository extends JpaRepository<Assn3223,String> {
    Assn3223 findAssn2BySource1(int s);
    Assn3223 findAssn2BySource2(int s);

    Assn3223 findAssn2BySource1AndAndSource2(int s1,int s2);

    Optional<Boolean> existsAssn2BySource1(int s);
    Optional<Boolean> existsAssn2BySource1AndSource2(int s1,int s2);

    Boolean existsById(int s);

    Optional<Assn3223> findById(int id);
}
