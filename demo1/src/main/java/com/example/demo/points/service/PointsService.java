package com.example.demo.points.service;


import com.example.demo.points.entity.*;
import com.example.demo.points.entity.dto.PointsQuery;
import com.example.demo.points.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointsService {

    private final Points1054Repository points1Repository;
    private final Points2802Repository points2Repository;
    private final Points3223Repository points3Repository;
    private final Points0Repository points0Repository;
    private final Points88Repository points88Repository;
    private final Points2619Repository points2619Repository;

    public PointsService(Points1054Repository points1Repository,
                         Points2802Repository points2Repository,
                         Points3223Repository points3Repository,
                         Points0Repository points0Repository,
                         Points88Repository points88Repository,
                         Points2619Repository points2619Repository) {
        this.points1Repository = points1Repository;
        this.points2Repository = points2Repository;
        this.points3Repository = points3Repository;
        this.points0Repository = points0Repository;
        this.points88Repository = points88Repository;
        this.points2619Repository = points2619Repository;
    }

    public List<Points> findAll(int sense) {
        List<Points> ret = new ArrayList<>();
        switch (sense) {
            case 2802 -> ret = points3Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 3223 -> ret = points2Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 1054 -> ret = points1Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 0 -> ret = points0Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 88 -> ret = points88Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 2619 -> ret = points2619Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            default -> {
            }
        }
        return ret;
    }

    public List<Points> findAll(int sourceSize, int page, int pageSize){
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Points> ret = new ArrayList<>();
        switch (sourceSize) {
            case 2 -> ret = points2Repository.findAll(pageRequest).stream().map((p) -> ((Points) p)).toList();
            case 3 -> ret = points1Repository.findAll(pageRequest).stream().map((p) -> ((Points) p)).toList();
            case 5 -> ret = points3Repository.findAll(pageRequest).stream().map((p) -> ((Points) p)).toList();
            default -> {
            }
        }
        return ret;
    }

    public long count(int sourceSize){
        long ret = 0;
        switch (sourceSize) {
            case 2 -> ret = points2Repository.count();
            case 3 -> ret = points1Repository.count();
            case 5 -> ret = points3Repository.count();
            default -> {
            }
        }
        return ret;
    }

    public Boolean update(int sense, PointsQuery point) {
        Points p = new Points3223(point);
        switch (sense) {
            case 1054 -> points1Repository.save(new Points1054(point));
            case 2802 -> points2Repository.save(new Points2802(point));
            case 3223 -> points3Repository.save(new Points3223(point));
            case 0 -> points0Repository.save(new Points0(point));
            case 88 -> points88Repository.save(new Points88(point));
            case 2619 -> points2619Repository.save(new Points2619(point));
            default -> {
                return false;
            }
        }
        return true;
    }

}
