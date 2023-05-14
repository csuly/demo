package com.example.demo.features.reposity;

import com.example.demo.features.entity.FN88;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FN88Repository extends JpaRepository<FN88, Integer>, FNRepository {
}
