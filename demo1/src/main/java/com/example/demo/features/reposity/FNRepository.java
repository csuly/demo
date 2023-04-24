package com.example.demo.features.reposity;

import com.example.demo.features.entity.Features;

public interface FNRepository {
    Features findByBatchAndSource(int a, int b);
}
