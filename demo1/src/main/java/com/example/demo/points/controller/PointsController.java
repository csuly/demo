package com.example.demo.points.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Result;
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
import java.util.List;
import java.util.Map;

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
    public PointsController(PointsService pointsService)
    {
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
    public SearchPointsResult searchPoints(@RequestBody JSONObject obj)
    {
        int scene = obj.getInteger("scene");
        int source = obj.getInteger("source");
        try{
            String tableName = "`"+scene+"-points`";
            String sql = String.format("SELECT * FROM "+tableName+" WHERE source="+source);
            List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
            return SearchPointsResult.success(data);
        }catch(Exception e) {return SearchPointsResult.error();}
    }

    @PostMapping("/getTrackPoints")
    public SearchPointsResult trackPoints(@RequestBody JSONObject obj) {
        int source = obj.getInteger("sourceSize");
        String scene="";
        try{
            switch(source) {
                case 2:scene="3223";break;
                case 3:scene="2802";break;
                case 5:scene="1054";break;
                default:break;
            }
            if(scene.isEmpty()) {System.out.println("参数传递异常");return SearchPointsResult.error();}
            else {
                String pointName = "`" + scene + "-points`";
                String featureName = "`" + scene + "-features`";
                String sql = String.format("SELECT p.id, p.batch, p.source, p.lon, p.lat,(f.max_lat - f.min_lat) AS lon_lat,"+
                        " (f.max_lon - f.min_lon) AS lon_len, f.duration, f.avg_accel, f.avg_anguvel, f.avg_cou, f.avg_vel, f.sparsity"+
                        " FROM "+pointName+" AS p JOIN "+featureName+" AS f ON p.batch = f.batch AND p.source = f.source");
                System.out.println(sql);
                List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
                return SearchPointsResult.success(data);
            }
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
    public PointsResult deletePoints(@RequestBody JSONObject obj)
    {
        int scene = obj.getInteger("scene");
        JSONArray idList = obj.getJSONArray("id");
        try{
            String tableName = "`"+scene+"-points`";
            for (int i=0;i<idList.size();i++)
            {
                String sql = String.format("DELETE FROM " + tableName + " WHERE id=" + idList.getInteger(i));
                jdbcTemplate.update(sql);
            }
            return PointsResult.success("删除成功");
        }catch(Exception e) {return PointsResult.error("删除失败");}
    }
    @PostMapping("/getPoints")
    public Result show(@RequestBody PointsQuery queryRequest)
    {
        List<PointsVO> data = pointsService.findAll(queryRequest.getSourceSize())
                .stream()
                .map((p) -> new PointsVO(p.getBatch(), p.getSource(), p.getLat(), p.getLon(),p.getTime(),p.getVel(),p.getCou(), false))
                .toList();
        return Result.success(data);
    }

}
