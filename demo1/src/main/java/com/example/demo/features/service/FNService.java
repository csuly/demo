package com.example.demo.features.service;

import com.example.demo.Result;
import com.example.demo.features.entity.FN1054;
import com.example.demo.features.entity.FN88;
import com.example.demo.features.entity.Features;
import com.example.demo.features.entity.dto.QueryItem;
import com.example.demo.features.reposity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FNService {
    private final FN2802Repository fn2802Repository;
    private final FN3223Repository fn3223Repository;
    private final FN1054Repository fn1054Repository;

    private final FN0Repository fn0Repository;
    private final FN88Repository fn88Repository;
    private final FN2619Repository fn2619Repository;
    private final Map<Integer, FNRepository> repositoryMap = new HashMap<>();

    public FNService(FN2802Repository fn2802Repository,
                     FN3223Repository fn3223Repository,
                     FN1054Repository fn1054Repository,
                     FN0Repository fn0Repository,
                     FN88Repository fn88Repository,
                     FN2619Repository fn2619Repository) {
        this.fn0Repository = fn0Repository;
        this.fn88Repository = fn88Repository;
        this.fn2619Repository = fn2619Repository;
        this.fn1054Repository = fn1054Repository;
        this.fn2802Repository = fn2802Repository;
        this.fn3223Repository = fn3223Repository;
        repositoryMap.put(3223, fn3223Repository);
        repositoryMap.put(2802, fn2802Repository);
        repositoryMap.put(1054, fn1054Repository);
        repositoryMap.put(0, fn0Repository);
        repositoryMap.put(88, fn88Repository);
        repositoryMap.put(2619, fn2619Repository);
    }

    public List<Features> getData(int sence, List<QueryItem> sources) {
        List<Features> data = new ArrayList<>();
        FNRepository fnRepository = repositoryMap.get(sence);
        if (fnRepository == null)
            return null;
        for (QueryItem q : sources) {
            data.add(fnRepository.findByBatchAndSource(q.batch(), q.source()));
        }
        return data;
    }
}
