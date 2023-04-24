package com.example.demo.features.reposity;

import com.example.demo.features.entity.Features;

import java.util.ArrayList;

public interface FeaturesRepository {
    Features findByBatchAndSource(int a, int b);
}
