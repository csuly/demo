package com.example.demo.features.reposity;

import com.example.demo.features.entity.FN2802;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FN2802Repository extends JpaRepository<FN2802,String>, FNRepository {
}
