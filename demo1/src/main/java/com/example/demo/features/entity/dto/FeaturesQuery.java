package com.example.demo.features.entity.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeaturesQuery {
    private int scene;
    private JSONArray features;
}
