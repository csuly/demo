package com.example.demo.features.reposity;

import com.example.demo.features.entity.Features3223;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Features3223Repository extends JpaRepository<Features3223, String>,FeaturesRepository {
}
