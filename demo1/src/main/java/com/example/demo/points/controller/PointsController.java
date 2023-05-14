package com.example.demo.points.controller;


import com.example.demo.Result;

import com.example.demo.features.reposity.*;
import com.example.demo.features.service.FNService;
import com.example.demo.features.service.FeaturesService;
import com.example.demo.points.entity.Points;

import com.example.demo.points.entity.VO.PointsVO;
import com.example.demo.points.entity.dto.PointsQuery;

import com.example.demo.points.result.PointsResult;
import com.example.demo.points.result.SearchPointsResult;

import com.example.demo.points.service.PointsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.demo.points.repository.Points2802Repository;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

@RestController
public class PointsController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private final PointsService pointsService;
    private final FeaturesService featuresService;

    private final FNService fnService;


    public PointsController(PointsService pointsService, FeaturesService featuresService, FNService fnService) {
        this.pointsService = pointsService;
        this.featuresService = featuresService;
        this.fnService = fnService;
    }


    /*
    @GetMapping("/getPoints")
    //public GetPointsResult getPoints(@RequestBody Points point)
    public GetPointsResult getPoints(@RequestParam("scene") String scene)
    {
        try{
            String tableName = "`"+scene+"-points`";
            String sql = String.format("SELECT * FROM "+tableName);
            List<Map<String, Object>> data = data=jdbcTemplate.queryForList(sql);
            sql = String.format("SELECT DISTINCT source FROM %s;", tableName);
            List<Map<String, Object>> sourceList=jdbcTemplate.queryForList(sql);
            return GetPointsResult.success(data,sourceList);
        }catch(Exception e) {return GetPointsResult.error();}
    }
    */

    @GetMapping("/getPoints")
    public SearchPointsResult searchPoints(@RequestParam("scene") int scene,@RequestParam("source") int source)
    {
        try{
            String tableName = "`"+scene+"-points`";
            String sql = String.format("SELECT * FROM "+tableName+" WHERE source="+source);
            List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
            return SearchPointsResult.success(data);
        }catch(Exception e) {return SearchPointsResult.error();}
    }

    @PostMapping("/addPoint")
    public PointsResult postPoints(@RequestBody PointsQuery point)
    {
        int sense = point.getScene();
        System.out.println(sense);
        try{
//            if(point.getScene().equals("2802"))
//            {
//                Points2802 p=new Points2802(point);
//                points2802Repository.save(p);
//            }
//            else if(point.getScene().equals("3223"))
//            {
//                Points3223 p=new Points3223(point);
//                points3223Repository.save(p);
//            }
//            else if(point.getScene().equals("1054"))
//            {
//                Points1054 p=new Points1054(point);
//                points1054Repository.save(p);
//            }
//            else
//            {
//                return PointsResult.error("添加失败");
//            }
            if(pointsService.update(sense,point))
                return PointsResult.success("添加成功");
            else  return PointsResult.error("添加失败");
        }catch(Exception e) {
            System.out.println(e);
            return PointsResult.error("添加失败");}
    }

    //    @PutMapping("/updatePoint")
//    public PointsResult putPoints(@RequestBody PointsQuery point)
//    {
//        try{
//            if(point.getScene().equals("2802"))
//            {
//                Points2802 p=new Points2802(point);
//                points2802Repository.save(p);
//            }
//            else if(point.getScene().equals("3223"))
//            {
//                Points3223 p=new Points3223(point);
//                points3223Repository.save(p);
//            }
//            else if(point.getScene().equals("1054"))
//            {
//                Points1054 p=new Points1054(point);
//                points1054Repository.save(p);
//            }
//            else
//            {
//                return PointsResult.error("修改失败");
//            }
//            return PointsResult.success("修改成功");
//        }catch(Exception e) {return PointsResult.error("修改失败");}
//    }
    @PutMapping("/updatePoint")
    public PointsResult putPoints(@RequestBody PointsQuery point){
        int sense = point.getScene();
        try {

            if(pointsService.update(sense,point))
                return PointsResult.success("修改成功");
            return PointsResult.error("修改失败");

        }catch(Exception e) {return PointsResult.error("修改失败");}
    }

    @DeleteMapping("/deletePoints")
    public PointsResult deletePoints(@RequestParam("scene") int scene,@RequestParam("id") String id)
    {
        try{
            String tableName = "`"+scene+"-points`";
            List<String>idList= Arrays.asList(id.split(","));
            for (int i=0;i<idList.size();i++)
            {
                String sql = String.format("DELETE FROM " + tableName + " WHERE id=" + idList.get(i));
                jdbcTemplate.update(sql);
            }
            return PointsResult.success("删除成功");
        }catch(Exception e) {return PointsResult.error("删除失败");}
    }
    @PostMapping("/getPoints")
    public Result show(@RequestBody PointsQuery queryRequest) {
        List<Points> data = pointsService.findAll(queryRequest.getScene());
        List<PointsVO> res = featuresService.getRes(data, queryRequest.getScene());

//                .stream()
//                .map((p) -> new PointsVO(p.getBatch(), p.getSource(), p.getLat(), p.getLon(),f.findByBatchAndSource(p.getBatch(),p.getSource())))
//                .toList();
        return Result.success(res);
    }

}
