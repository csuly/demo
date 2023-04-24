package com.example.demo.projection.repository;


import com.example.demo.projection.entity.Projection;

import java.util.ArrayList;
import java.util.List;

public interface ProjectionRepository {
    List<Projection> findAllByBatchAndSource(int a, int b);



}
