package com.example.demo.features.reposity;

import com.example.demo.features.entity.Features2802;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Features2802Repository extends JpaRepository<Features2802, String>,FeaturesRepository {
}
