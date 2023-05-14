package com.example.demo.features.reposity;

import com.example.demo.features.entity.FN2619;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FN2619Repository extends JpaRepository<FN2619, Integer>, FNRepository {
}
