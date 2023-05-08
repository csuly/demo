package com.example.demo.features.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Result;
import com.example.demo.features.entity.Features;
import com.example.demo.features.entity.dto.QueryItem;
import com.example.demo.features.entity.dto.Request;
import com.example.demo.features.reposity.*;
import com.example.demo.features.result.GetFeaturesResult;
import com.example.demo.features.result.PutFeaturesResult;
import com.example.demo.points.result.PointsResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@RestController
public class FeaturesController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private Features2802Repository features2802Repository;
    private Features3223Repository features3223Repository;
    private Features1054Repository features1054Repository;
    private FN1054Repository fn1054Repository;
    private FN2802Repository fn2802Repository;
    private FN3223Repository fn3223Repository;
    private final Map<Integer, FeaturesRepository> repositoryMap = new HashMap<>();
    private final Map<Integer, FNRepository> fnRepositoryMap = new HashMap<>();

    public FeaturesController(Features1054Repository features1054Repository,
                              Features2802Repository features2802Repository,
                              Features3223Repository features3223Repository,
                              FN3223Repository fn3223Repository,
                              FN1054Repository fn1054Repository,
                              FN2802Repository fn2802Repository) {
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
    }

    //获取场景特征数据接口
    @GetMapping("/getFeatures")
    public GetFeaturesResult getPoints(@RequestParam("scene") int scene,@RequestParam("list") String list)
    {
        try{
            List<String> tagList = Arrays.asList(list.split(","));
            if(initFeature(String.valueOf(scene)))
            {
                System.out.println("初始化成功");
                if (scene==2802 || scene==3223 || scene==1054)
                {
                    String tableName = "`"+scene+"-features`";
                    String temp="SELECT id,batch,source,";
                    for(int i=0;i<tagList.size();i++)
                    {
                        temp=temp+tagList.get(i)+',';
                    }
                    temp=temp.substring(0,temp.length()-1);
                    String sql = String.format(temp+" FROM "+tableName);
                    System.out.println(sql);
                    List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
                    return GetFeaturesResult.success(data);
                }
                else    //目标表不存在
                {
                    System.out.println("目标表不存在");
                    return GetFeaturesResult.error();
                }
            }
            else
            {
                System.out.println("初始化失败");
                return GetFeaturesResult.error();
            }
        }
        catch(Exception e)
        {
            System.out.println("系统异常");
            return GetFeaturesResult.error();
        }
    }

    @DeleteMapping("/deleteFeatures")
    public PointsResult deletePoints(@RequestParam("scene") int scene,@RequestParam("id") String id)
    {
        try{
            String tableName = "`"+scene+"-features`";
            List<String>idList=Arrays.asList(id.split(","));
            for (int i=0;i<idList.size();i++)
            {
                String sql = String.format("DELETE FROM " + tableName + " WHERE id=" + idList.get(i));
                jdbcTemplate.update(sql);
            }
            return PointsResult.success("删除成功");
        }
        catch(Exception e)
        {
            System.out.println("系统错误");
            return PointsResult.error("删除失败");
        }
    }

    @PostMapping ("/saveFeatures")
    public PutFeaturesResult saveFeatures(@RequestBody JSONObject obj)
    {
        int scene=obj.getInteger("scene");
        try
        {
            System.out.println("开始归一化,请等待");
            featuresNormalized();
            System.out.println("结束归一化");
            switch(scene)
            {
                case 1054:runPython("1054");break;
                case 2802:runPython("2802");break;
                case 3223:runPython("3223");break;
                default:
                {
                    System.out.println("表不存在");
                    return PutFeaturesResult.error("计算失败");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("异常");
            return PutFeaturesResult.error("计算失败");
        }
        return PutFeaturesResult.success("计算完毕，保存成功");
    }

    @GetMapping("/getFeaturesNormalized")
    public GetFeaturesResult getFeaturesNormalized(@RequestParam("scene") int scene)
    {
        try
        {
            if (scene==2802 || scene==3223 || scene==1054)
            {
                String tableName = "`"+scene+"-features-normalized`";
                String sql = String.format("SELECT * FROM "+tableName);
                System.out.println(sql);
                List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
                return GetFeaturesResult.success(data);
            }
            else    //目标表不存在
            {
                System.out.println("目标表不存在");
                return GetFeaturesResult.error();
            }
        }
        catch (Exception e)
        {
            System.out.println("系统异常");
            return GetFeaturesResult.error();
        }
    }

    @GetMapping("/getProjection")
    public GetFeaturesResult getProjection(@RequestParam("scene") int scene)
    {
        try
        {
            if (scene==2802 || scene==3223 || scene==1054)
            {
                String tableName = "`"+scene+"-features-projection`";
                String sql = String.format("SELECT * FROM "+tableName);
                System.out.println(sql);
                List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
                return GetFeaturesResult.success(data);
            }
            else    //目标表不存在
            {
                System.out.println("目标表不存在");
                return GetFeaturesResult.error();
            }
        }
        catch (Exception e)
        {
            System.out.println("系统异常");
            return GetFeaturesResult.error();
        }
    }

    @PostMapping("/getFeatureNormalizedData")
    public Result GetFnData(@RequestBody Request request)
    {
        List<Features> data = new ArrayList<>();
        FNRepository fnRepository = fnRepositoryMap.get(request.getSourceSize());
        if(fnRepository == null)
            return Result.error("400","No such table");
        for(QueryItem q : request.getSources())
        {
            data.add(fnRepository.findByBatchAndSource(q.batch(), q.source()));
        }
        return Result.success(data);
    }

    @PostMapping("/searchFeatures")
    public Result search(@RequestBody Request request)
    {
        List<Features> data = new ArrayList<>();
        FeaturesRepository featuresRepository = repositoryMap.get(request.getSourceSize());
        for(QueryItem q:request.getSources())
            data.add(featuresRepository.findByBatchAndSource(q.batch(), q.source()));
        return Result.success(data);
    }
    @PostMapping("/getNewProjection")
    public GetFeaturesResult getNewProjection(@RequestBody JSONObject obj)
    {
        try
        {
            int scene = obj.getInteger("scene");
            String alg = obj.getString("pro_algo");
            List<String> fList = (List<String>) obj.get("features");
//            for(Object s : fList.toArray())
//                System.out.println(s);
            if (fList.toArray().length <= 2) {
                System.out.println("维数过低，无法投影");
                return GetFeaturesResult.error();
            }
            if (scene == 2802 || scene == 3223 || scene == 1054) {
                String tableName = "`" + scene + "-features-projection`";
                String target = fList.get(0);
                for (int i = 1; i < fList.size(); i++) {
                    target=target+","+fList.get(i);
                }
                System.out.println(target);
                partProjection(scene,target);
                target = "SELECT id,batch,source,time_min,time_max,duration,min_lon,max_lon,min_lat,max_lat,avg_vel,avg_accel,avg_cou,avg_anguvel,points,sparsity,projection_x_" + alg + ", " + " projection_y_" + alg + " FROM " + tableName;
                String sql= String.format(target);
                System.out.println(sql);
                List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
                return GetFeaturesResult.success(data);
            }
            else    //目标表不存在
            {
                System.out.println("目标表不存在");
                return GetFeaturesResult.error();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return GetFeaturesResult.error();
        }
    }
    //调用python文件函数
    public void runPython(String table) throws IOException, InterruptedException {
        //指定路径
        //arguement的第一个参数是anaconda环境的python地址，第二个参数是python文件的位置
        System.out.println("开始！");

        String [] argument=new String[]{"/home/ubuntu/anaconda3/envs/myenv/bin/python","/home/ubuntu/python/touyin.py",table};

        //String [] argument=new String[]{"D:\\Anaconda3\\envs\\pytorch\\python","E:\\GitHub\\demo\\demo\\demo1\\src\\main\\resources\\touyin.py",table};
        try
        {
            //运行python文件
            Process process=Runtime.getRuntime().exec(argument);
            //获取python输出信息
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"));
            String line=null;
            while((line=in.readLine())!=null)
            {
                System.out.println(line);
            }
            in.close();
            //等待当前python程序执行完毕
            System.out.println(process.waitFor());
        }catch (IOException e){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();}
    }

    public void partProjection(int table,String fList) throws IOException, InterruptedException {
        //指定路径
        //arguement的第一个参数是anaconda环境的python地址，第二个参数是python文件的位置
        System.out.println("开始！");

        String pythonPath = "/home/ubuntu/anaconda3/envs/myenv/bin/python";
        String filePath = "/home/ubuntu/python/partProjection.py";
        String[] argument = new String[]{pythonPath, filePath, String.valueOf(table), fList};

//        String pythonPath="D:\\Anaconda3\\envs\\pytorch\\python";
//        String filePath="E:\\GitHub\\demo\\demo\\demo1\\src\\main\\resources\\partProjection.py";
//        String [] argument=new String[]{pythonPath,filePath,String.valueOf(table),fList};
        System.out.println(argument[1]+"\t"+argument[2]+"\t"+argument[3]);
        try
        {
            //运行python文件
            Process process=Runtime.getRuntime().exec(argument);
            //获取python输出信息
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"));
            String line=null;
            while((line=in.readLine())!=null)
            {
                System.out.println(line);
            }
            in.close();
            //等待当前python程序执行完毕
            System.out.println(process.waitFor());
        }catch (IOException e){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();}
    }

    public boolean featuresNormalized() throws Exception {
        String scriptPath = "/home/ubuntu/python/feature_normalized.py";
        String [] argument=new String[]{"/home/ubuntu/anaconda3/envs/myenv/bin/python",scriptPath};

//        String scriptPath = "E:\\GitHub\\demo\\demo\\demo1\\src\\main\\resources\\feature_normalized.py";
//        String [] argument=new String[]{"D:\\Anaconda3\\envs\\pytorch\\python",scriptPath};

        try
        {
            //运行python文件
            Process process=Runtime.getRuntime().exec(argument);
            //等待当前python程序执行完毕
            System.out.println(process.waitFor());
        }catch (IOException e){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();}
        return true;
    }

    public boolean initFeature(String table) throws Exception {
        String scriptPath = "/home/ubuntu/python/feature_model.py";
        String [] argument=new String[]{"python",scriptPath,table};

//        String scriptPath = "E:\\GitHub\\demo\\demo\\demo1\\src\\main\\resources\\feature_model.py";
//        String [] argument=new String[]{"D:\\Anaconda3\\envs\\pytorch\\python",scriptPath,table};

        try
        {
            //运行python文件
            Process process=Runtime.getRuntime().exec(argument);
            //等待当前python程序执行完毕
            System.out.println(process.waitFor());
        }catch (IOException e){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();}
        return true;
    }
}
