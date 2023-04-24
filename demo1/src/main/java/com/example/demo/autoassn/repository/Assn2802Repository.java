package com.example.demo.autoassn.repository;

import com.example.demo.autoassn.entity.Assn2802;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Assn2802Repository extends JpaRepository<Assn2802,String> {
    Assn2802 findAssn1BySource1(int s);
    Assn2802 findAssn1BySource1AndSource2AndSource3(int s1,int s2,int s3);
    Assn2802 findAssn1BySource3(int s);

    Assn2802 findAssn1BySource2(int s);
    Boolean existsById(int s);

    Optional<Assn2802> findById(int id);
    Optional<Boolean> existsAssn1BySource1(int s);
    Optional<Boolean> existsAssn1BySource1AndSource2AndSource3(int s1,int s2,int s3);
}
