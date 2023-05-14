package com.example.demo.features.service;

import com.example.demo.features.entity.Features;
import com.example.demo.features.entity.dto.QueryItem;
import com.example.demo.features.reposity.*;
import com.example.demo.points.entity.Points;
import com.example.demo.points.entity.VO.PointsVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeaturesService {

    private final Features2802Repository features2802Repository;
    private final Features3223Repository features3223Repository;
    private final Features1054Repository features1054Repository;

    private final Features0Repository features0Repository;
    private final Features88Repository features88Repository;
    private final Features2619Repository features2619Repository;
    private final Map<Integer, FeaturesRepository> repositoryMap = new HashMap<>();

    public FeaturesService(Features2802Repository features2802Repository,
                           Features3223Repository features3223Repository,
                           Features1054Repository features1054Repository,
                           Features0Repository features0Repository,
                           Features88Repository features88Repository,
                           Features2619Repository features2619Repository) {
        this.features3223Repository = features3223Repository;
        this.features0Repository = features0Repository;
        this.features2802Repository = features2802Repository;
        this.features1054Repository = features1054Repository;
        this.features88Repository = features88Repository;
        this.features2619Repository = features2619Repository;
        repositoryMap.put(3223, features3223Repository);
        repositoryMap.put(2802, features2802Repository);
        repositoryMap.put(1054, features1054Repository);
        repositoryMap.put(0, features0Repository);
        repositoryMap.put(88, features88Repository);
        repositoryMap.put(2619, features2619Repository);
    }

    public List<PointsVO> getRes(List<Points> data, int scene) {
        List<PointsVO> res = new ArrayList<>();
        FeaturesRepository f = repositoryMap.get(scene);
        get_Data(data, res, f);
        return res;
    }

    public List<Features> getData(int sence, List<QueryItem> sources) {
        List<Features> data = new ArrayList<>();
        FeaturesRepository featuresRepository = repositoryMap.get(sence);
        System.out.println(sence);
        if (featuresRepository == null)
            return null;
        for (QueryItem q : sources) {
            data.add(featuresRepository.findByBatchAndSource(q.batch(), q.source()));
        }
        return data;
    }

    public static void get_Data(List<Points> data, List<PointsVO> res, FeaturesRepository f) {
        int batch = data.get(0).getBatch(), source = data.get(0).getSource();
        Features f1 = f.findByBatchAndSource(batch, source);
        double lat_len = f1.getMax_lat() - f1.getMin_lat();
        double lon_len = f1.getMax_lon() - f1.getMin_lon();
        double b = f1.getAvg_accel(), c = f1.getAvg_anguvel(), a = f1.getDuration(), e = f1.getSparsity(), d = f1.getAvg_cou(), g = f1.getAvg_vel();
        for (Points p : data) {
            if (p.getBatch() != batch || p.getSource() != source) {
                batch = p.getBatch();
                source = p.getSource();
                f1 = f.findByBatchAndSource(batch, source);
                if (f1 == null)
                    continue;
                lat_len = f1.getMax_lat() - f1.getMin_lat();
                lon_len = f1.getMax_lon() - f1.getMin_lon();
                b = f1.getAvg_accel();
                c = f1.getAvg_anguvel();
                a = f1.getDuration();
                e = f1.getSparsity();
                d = f1.getAvg_cou();
                g = f1.getAvg_vel();

            }


            res.add(new PointsVO(p.getId(), p.getBatch(), p.getSource(), p.getTime(), p.getVel(), p.getCou(), p.getLat(), p.getLon(), lat_len, lon_len, a, b, c, d, e, g));
        }
    }


}
