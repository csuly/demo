package com.example.demo.features.reposity;

import com.example.demo.features.entity.FN1054;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FN1054Repository extends JpaRepository<FN1054,String>, FNRepository {
}
