package com.example.demo.features.reposity;

import com.example.demo.features.entity.Features1054;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Features1054Repository extends JpaRepository<Features1054, String>,FeaturesRepository {
}
