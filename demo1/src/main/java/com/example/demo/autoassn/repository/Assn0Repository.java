package com.example.demo.autoassn.repository;

import com.example.demo.autoassn.entity.Assn0;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Assn0Repository extends JpaRepository<Assn0, Integer> {
    Assn0 findAssn2BySource1(int s);

    Assn0 findAssn2BySource2(int s);

    Assn0 findAssn2BySource1AndAndSource2(int s1, int s2);

    Optional<Boolean> existsAssn2BySource1(int s);

    Optional<Boolean> existsAssn2BySource1AndSource2(int s1, int s2);

    Boolean existsById(int s);

    Optional<Assn0> findById(int id);
}
