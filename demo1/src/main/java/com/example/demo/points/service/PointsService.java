package com.example.demo.points.service;


import com.example.demo.points.entity.Points;
import com.example.demo.points.entity.Points1054;
import com.example.demo.points.entity.Points2802;
import com.example.demo.points.entity.Points3223;
import com.example.demo.points.entity.dto.PointsQuery;
import com.example.demo.points.repository.Points1054Repository;
import com.example.demo.points.repository.Points2802Repository;
import com.example.demo.points.repository.Points3223Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointsService {

    private final Points1054Repository points1Repository;
    private final Points2802Repository points2Repository;
    private final Points3223Repository points3Repository;

    public PointsService(Points1054Repository points1Repository, Points2802Repository points2Repository, Points3223Repository points3Repository) {
        this.points1Repository = points1Repository;
        this.points2Repository = points2Repository;
        this.points3Repository = points3Repository;
    }

    public List<Points> findAll(int sourceSize){
        List<Points> ret = new ArrayList<>();
        switch (sourceSize) {
            case 2 -> ret = points2Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 3 -> ret = points1Repository.findAll().stream().map((p) -> ((Points) p)).toList();
            case 5 -> ret = points3Repository.findAll().stream().map((p) -> ((Points) p)).toList();
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

    public Boolean update(String sense, PointsQuery point)
    {
        Points p = new Points3223(point);
        switch(sense) {
            case "1054" -> points1Repository.save(new Points1054(point));
            case "2802" -> points2Repository.save(new Points2802(point));
            case "3223" -> points3Repository.save(new Points3223(point));
            default -> {
                    return false;
            }
        }
        return true;
    }

}
