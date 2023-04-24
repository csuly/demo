package com.example.demo.autoassn.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssnQuery {
    private int scene;
    private JSONObject weights;
}
