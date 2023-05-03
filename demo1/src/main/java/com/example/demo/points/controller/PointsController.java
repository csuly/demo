package com.example.demo.points.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Result;
import com.example.demo.features.entity.Features;
import com.example.demo.features.reposity.*;
import com.example.demo.points.entity.Points;
import com.example.demo.points.entity.Points1054;
import com.example.demo.points.entity.VO.PointsVO;
import com.example.demo.points.entity.dto.PointsQuery;
import com.example.demo.points.repository.Points1054Repository;
import com.example.demo.points.result.PointsResult;
import com.example.demo.points.result.SearchPointsResult;
import com.example.demo.points.entity.Points2802;
import com.example.demo.points.entity.Points3223;
import com.example.demo.points.repository.Points3223Repository;
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
    //    private Points2802Repository points2802Repository;
//    private Points3223Repository points3223Repository;
//    private Points1054Repository points1054Repository;
    private final PointsService pointsService;

    //    public PointsController(Points2802Repository points2802Repository,Points1054Repository points1054Repository,Points3223Repository points3223Repository) {
//        this.points1054Repository = points1054Repository;
//        this.points2802Repository = points2802Repository;
//        this.points3223Repository = points3223Repository;
//    }
    private Features2802Repository features2802Repository;
    private Features3223Repository features3223Repository;
    private Features1054Repository features1054Repository;
    private FN1054Repository fn1054Repository;
    private FN2802Repository fn2802Repository;
    private FN3223Repository fn3223Repository;
    private final Map<Integer, FeaturesRepository> repositoryMap = new HashMap<>();
    private final Map<Integer, FNRepository> fnRepositoryMap = new HashMap<>();

    public PointsController(Features1054Repository features1054Repository,
                            Features2802Repository features2802Repository,
                            Features3223Repository features3223Repository,
                            FN3223Repository fn3223Repository,
                            FN1054Repository fn1054Repository,
                            FN2802Repository fn2802Repository,
                            PointsService pointsService) {
        this.features1054Repository = features1054Repository;
        this.features2802Repository = features2802Repository;
        this.features3223Repository = features3223Repository;
        this.fn2802Repository = fn2802Repository;
        this.fn3223Repository = fn3223Repository;
        this.fn1054Repository = fn1054Repository;
        repositoryMap.put(2, features3223Repository);
        repositoryMap.put(3,features2802Repository);
        repositoryMap.put(5,features1054Repository);
        fnRepositoryMap.put(2,fn3223Repository);
        fnRepositoryMap.put(3, fn2802Repository);
        fnRepositoryMap.put(5, fn1054Repository);
        this.pointsService = pointsService;
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
        String sense = point.getSense();
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
        String sense = point.getSense();
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
    public Result show(@RequestBody PointsQuery queryRequest)
    {
        FeaturesRepository f = repositoryMap.get(queryRequest.getSourceSize());
        List<Points> data = pointsService.findAll(queryRequest.getSourceSize());
        List<PointsVO> res = new ArrayList<>();
        int batch = data.get(0).getBatch(),source = data.get(0).getSource();
        Features f1 = f.findByBatchAndSource(batch,source);
        double lat_len = f1.getMax_lat() - f1.getMin_lat();
        double lon_len = f1.getMax_lon() - f1.getMin_lon();
        double b = f1.getAvg_accel(), c = f1.getAvg_anguvel(), a = f1.getDuration(), e = f1.getSparsity(), d = f1.getAvg_cou(), g = f1.getAvg_vel();
        for(Points p : data)
        {
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
//                .stream()
//                .map((p) -> new PointsVO(p.getBatch(), p.getSource(), p.getLat(), p.getLon(),f.findByBatchAndSource(p.getBatch(),p.getSource())))
//                .toList();
        return Result.success(res);
    }

}
