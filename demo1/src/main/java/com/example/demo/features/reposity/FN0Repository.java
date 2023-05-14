package com.example.demo.features.reposity;

import com.example.demo.features.entity.FN0;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FN0Repository extends JpaRepository<FN0, Integer>, FNRepository {
}
