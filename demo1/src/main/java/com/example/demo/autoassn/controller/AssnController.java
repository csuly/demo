package com.example.demo.autoassn.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Result;
import com.example.demo.autoassn.entity.*;
import com.example.demo.autoassn.repository.*;
import com.example.demo.autoassn.result.GerAssnlistResult;
import com.example.demo.autoassn.entity.dto.AssnQuery;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AssnController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final Assn1054Repository assn1054Repository;
    private final Assn2802Repository assn2802Repository;
    private final Assn3223Repository assn3223Repository;
    private final Assn0Repository assn0Repository;
    private final Assn88Repository assn88Repository;
    private final Assn2619Repository assn2619Repository;

    public AssnController(Assn1054Repository assn1054Repository,
                          Assn2802Repository assn2802Repository,
                          Assn3223Repository assn3223Repository,
                          Assn0Repository assn0Repository,
                          Assn88Repository assn88Repository,
                          Assn2619Repository assn2619Repository) {
        this.assn1054Repository = assn1054Repository;
        this.assn2802Repository = assn2802Repository;
        this.assn3223Repository = assn3223Repository;
        this.assn0Repository = assn0Repository;
        this.assn88Repository = assn88Repository;
        this.assn2619Repository = assn2619Repository;
    }

    @GetMapping("/getAssn")
    public GerAssnlistResult getPoints(@RequestParam("scene") int scene,@RequestParam("weights") String weights) throws IOException, InterruptedException
    {
        JSONArray data=new JSONArray();
        List<String>weight=Arrays.asList(weights.split(","));
        System.out.println(weight);
        int length=weight.size();
        try
        {
            if(length!=0)
            {
                switch(scene)
                {
                    case 3223:data=this.runPython("3223",weight);break;
                    case 2802:data=this.runPython("2802",weight);break;
                    case 1054:data=this.runPython("1054",weight);break;
                    default: return GerAssnlistResult.error();
                }
                System.out.println(data);
                return GerAssnlistResult.success(data);
            }
            else
            {
                String tableName = "`"+scene+"-features-normalized`";
                String sql = String.format("SELECT * FROM "+tableName);
                System.out.println(sql);
                List<Map<String, Object>> result=jdbcTemplate.queryForList(sql);
                System.out.println(result);
                return GerAssnlistResult.error();
            }
        }catch (Exception e){return GerAssnlistResult.error();}
    }

    @GetMapping("/getAssn/assn1054")
    public Result get1054()
    {
        List<Assn1054> assn3s = assn1054Repository.findAll();
        ArrayList<Integer> source1 = new ArrayList<>();
        ArrayList<Integer> source2 = new ArrayList<>();
        ArrayList<Integer> source3 = new ArrayList<>();
        ArrayList<Integer> source4 = new ArrayList<>();
        ArrayList<Integer> source5 = new ArrayList<>();

        for(Assn1054 a : assn3s)
        {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
            source3.add(a.getSource3());
            source4.add(a.getSource4());
            source5.add(a.getSource5());
        }

        Map res = new HashMap<>();
        res.put("all",assn3s);
        res.put("source1",source1);
        res.put("source2",source2);
        res.put("source3",source3);
        res.put("source4",source4);
        res.put("source5",source5);
        return Result.success(res);
    }

    @GetMapping("/getAssn/assn2802")
    public Result get2802()
    {
        List<Assn2802> assn1s = assn2802Repository.findAll();
        ArrayList<Integer> source1 = new ArrayList<>();
        ArrayList<Integer> source2 = new ArrayList<>();
        ArrayList<Integer> source3 = new ArrayList<>();
        for(Assn2802 a:assn1s)
        {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
            source3.add(a.getSource3());

        }

        Map res = new HashMap<>();
        res.put("all",assn1s);
        res.put("source1",source1);
        res.put("source2",source2);
        res.put("source3",source3);
        return Result.success(res);
    }

    @GetMapping("/getAssn/assn3223")
    public Result get3223()
    {
        List<Assn3223> list = assn3223Repository.findAll();
        List<Integer> source1 = new ArrayList<>();
        List<Integer> source2 = new ArrayList<>();
        for(Assn3223 a : list) {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
        }
        Map res = new HashMap<>();
        res.put("all", list);
        res.put("source1", source1);
        res.put("source2", source2);
        return Result.success(res);
    }


    @GetMapping("/getAssn/assn0")
    public Result get0() {

        List<Assn0> list = assn0Repository.findAll();
        List<Integer> source1 = new ArrayList<>();
        List<Integer> source2 = new ArrayList<>();
        for (Assn0 a : list) {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
        }
        Map res = new HashMap<>();
        res.put("all", list);
        res.put("source1", source1);
        res.put("source2", source2);
        return Result.success(res);
    }


    @GetMapping("/getAssn/assn88")
    public Result get88() {

        List<Assn88> list = assn88Repository.findAll();
        List<Integer> source1 = new ArrayList<>();
        List<Integer> source2 = new ArrayList<>();
        for (Assn88 a : list) {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
        }
        Map res = new HashMap<>();
        res.put("all", list);
        res.put("source1", source1);
        res.put("source2", source2);
        return Result.success(res);
    }


    @GetMapping("/getAssn/assn2619")
    public Result get2619() {

        List<Assn2619> list = assn2619Repository.findAll();
        List<Integer> source1 = new ArrayList<>();
        List<Integer> source2 = new ArrayList<>();
        for (Assn2619 a : list) {
            source1.add(a.getSource1());
            source2.add(a.getSource2());
        }
        Map res = new HashMap<>();
        res.put("all", list);
        res.put("source1", source1);
        res.put("source2", source2);
        return Result.success(res);
    }

    @PostMapping("/addAssn/assn1054")
    public Result add1054(@RequestBody Assn1054 assn3) {
        Assn1054 a = assn1054Repository.findBySource1AndSource2AndSource3AndSource4AndSource5(assn3.getSource1(),
                assn3.getSource2(), assn3.getSource3(), assn3.getSource4(), assn3.getSource5());
        int s1 = assn3.getSource1();
        int s2 = assn3.getSource2();
        int s3 = assn3.getSource3();
        int s4 = assn3.getSource4();
        int s5 = assn3.getSource5();

        if(a != null)
            return Result.success(assn1054Repository.findAll());
        Assn1054 res = new Assn1054(assn3);
        Assn1054 assn = assn1054Repository.findBySource1(s1);
        if (assn!=null) {
            assn.setSource1(-1);
            if((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                assn1054Repository.delete(assn);
            else
                assn1054Repository.save(assn);
        }
        assn = assn1054Repository.findBySource2(s2);
        if (assn!=null) {
            assn.setSource2(-1);
            if((assn.getSource1()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                assn1054Repository.delete(assn);
            else
                assn1054Repository.save(assn);
        }
        assn = assn1054Repository.findBySource3(s3);
        if (assn!=null) {
            assn.setSource3(-1);
            if((assn.getSource2()==-1)&& (assn.getSource1()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                assn1054Repository.delete(assn);
            else
                assn1054Repository.save(assn);
        }
        assn = assn1054Repository.findBySource4(s4);
        if (assn!=null) {
            assn.setSource4(-1);
            if((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource1()==-1)&& (assn.getSource5()==-1))
                assn1054Repository.delete(assn);
            else
                assn1054Repository.save(assn);
        }
        assn = assn1054Repository.findBySource5(s5);
        if (assn!=null) {
            assn.setSource5(-1);
            if((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource1()==-1))
                assn1054Repository.delete(assn);
            else
                assn1054Repository.save(assn);
        }
        assn1054Repository.save(res);
        List<Assn1054> assn3s = assn1054Repository.findAll();
        List<Assn1054> data = new ArrayList<>();
        data.add(res);
        for(Assn1054 as : assn3s)
        {
            if (!as.equals(res))
                data.add(as);
        }
        return Result.success(data);
    }

    @PostMapping("/addAssn/assn2802")
    public Result add2802(@RequestBody Assn2802 assn1)
    {
        int s2 = assn1.getSource2();
        int s1 = assn1.getSource1();
        int s3 = assn1.getSource3();
        if(assn2802Repository.findAssn1BySource1AndSource2AndSource3(s1,s2,s3)!=null)
            return Result.success(assn2802Repository.findAll());
        Assn2802 a = assn2802Repository.findAssn1BySource1(s1);
        if(a!=null) {
            a.setSource1(-1);
            if ((a.getSource3()==-1) &&( a.getSource2()==-1))
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        a = assn2802Repository.findAssn1BySource2(s2);
        if (a!=null) {
            a.setSource2(-1);
            if ((a.getSource1()==-1 )&& (a.getSource3()==-1))
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        a=assn2802Repository.findAssn1BySource3(s3);
        if (a!=null) {
            a.setSource3(-1);
            if ((a.getSource1()==-1) &&( a.getSource2()==-1))
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        a = new Assn2802(assn1);
        assn2802Repository.save(a);
        List<Assn2802> assn1s = assn2802Repository.findAll();
        List<Assn2802> data = new ArrayList<>();
        data.add(a);
        for(Assn2802 as : assn1s)
        {
            if(!as.equals(a))
                data.add(as);
        }

        return Result.success(data);
    }

    @PostMapping("/addAssn/assn3223")
    public Result add3223(@RequestBody Assn3223 assn2)
    {
        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn3223 a2 = assn3223Repository.findAssn2BySource1AndAndSource2(s1,s2);
        if(a2 != null)
            return Result.success(assn3223Repository.findAll());
        Assn3223 a = new Assn3223();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn3223 assn = assn3223Repository.findAssn2BySource1(s1);
        if (assn!=null) {
            assn.setSource1(-1);
            if (assn.getSource2()==-1)
                assn3223Repository.delete(assn);
            else
                assn3223Repository.save(assn);
        }
        assn = assn3223Repository.findAssn2BySource2(s2);
        if (assn!=null) {
            assn.setSource2(-1);
            if (assn.getSource1()==-1)
                assn3223Repository.delete(assn);
            else
                assn3223Repository.save(assn);
        }
        assn3223Repository.save(a);
        List<Assn3223> data = new ArrayList<>();
        data.add(a);
        List<Assn3223> assn2s = assn3223Repository.findAll();
        for (Assn3223 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }


    @PostMapping("/addAssn/assn0")
    public Result add0(@RequestBody Assn0 assn2) {
        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn0 a2 = assn0Repository.findAssn2BySource1AndAndSource2(s1, s2);
        if (a2 != null)
            return Result.success(assn0Repository.findAll());
        Assn0 a = new Assn0();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn0 assn = assn0Repository.findAssn2BySource1(s1);
        if (assn != null) {
            assn.setSource1(-1);
            if (assn.getSource2() == -1)
                assn0Repository.delete(assn);
            else
                assn0Repository.save(assn);
        }
        assn = assn0Repository.findAssn2BySource2(s2);
        if (assn != null) {
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn0Repository.delete(assn);
            else
                assn0Repository.save(assn);
        }
        assn0Repository.save(a);
        List<Assn0> data = new ArrayList<>();
        data.add(a);
        List<Assn0> assn2s = assn0Repository.findAll();
        for (Assn0 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }


    @PostMapping("/addAssn/assn88")
    public Result add88(@RequestBody Assn88 assn2) {
        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn88 a2 = assn88Repository.findAssn2BySource1AndAndSource2(s1, s2);
        if (a2 != null)
            return Result.success(assn88Repository.findAll());
        Assn88 a = new Assn88();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn88 assn = assn88Repository.findAssn2BySource1(s1);
        if (assn != null) {
            assn.setSource1(-1);
            if (assn.getSource2() == -1)
                assn88Repository.delete(assn);
            else
                assn88Repository.save(assn);
        }
        assn = assn88Repository.findAssn2BySource2(s2);
        if (assn != null) {
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn88Repository.delete(assn);
            else
                assn88Repository.save(assn);
        }
        assn88Repository.save(a);
        List<Assn88> data = new ArrayList<>();
        data.add(a);
        List<Assn88> assn2s = assn88Repository.findAll();
        for (Assn88 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }

    @PostMapping("/addAssn/assn2619")
    public Result add2619(@RequestBody Assn2619 assn2) {
        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn2619 a2 = assn2619Repository.findAssn2BySource1AndAndSource2(s1, s2);
        if (a2 != null)
            return Result.success(assn2619Repository.findAll());
        Assn2619 a = new Assn2619();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn2619 assn = assn2619Repository.findAssn2BySource1(s1);
        if (assn != null) {
            assn.setSource1(-1);
            if (assn.getSource2() == -1)
                assn2619Repository.delete(assn);
            else
                assn2619Repository.save(assn);
        }
        assn = assn2619Repository.findAssn2BySource2(s2);
        if (assn != null) {
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn2619Repository.delete(assn);
            else
                assn2619Repository.save(assn);
        }
        assn2619Repository.save(a);
        List<Assn2619> data = new ArrayList<>();
        data.add(a);
        List<Assn2619> assn2s = assn2619Repository.findAll();
        for (Assn2619 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }


    @PostMapping("/updateAssn/assn1054")
    public Result update1054(@RequestBody Assn1054 assn3) {
        Assn1054 a = assn1054Repository.findBySource1AndSource2AndSource3AndSource4AndSource5(assn3.getSource1(), assn3.getSource2(), assn3.getSource3(), assn3.getSource4(), assn3.getSource5());
        int s1 = assn3.getSource1();
        int s2 = assn3.getSource2();
        int s3 = assn3.getSource3();
        int s4 = assn3.getSource4();
        int s5 = assn3.getSource5();

        if(a != null)
            return Result.success(assn1054Repository.findAll());
        Assn1054 res = new Assn1054(assn3);
        Assn1054 assn;
        if (s1!=-1) {
            assn = assn1054Repository.findBySource1(s1);
            if (assn != null) {
                assn.setSource1(-1);
                if ((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                    assn1054Repository.delete(assn);
                else
                    assn1054Repository.save(assn);
            } else
                return Result.error("404", "No such value");
        }
        if(s2!=-1) {
            assn = assn1054Repository.findBySource2(s2);
            if (assn != null) {
                assn.setSource2(-1);
                if ((assn.getSource1()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                    assn1054Repository.delete(assn);
                else
                    assn1054Repository.save(assn);
            } else
                return Result.error("404", "No such value");
        }
        if(s3!=-1) {
            assn = assn1054Repository.findBySource3(s3);
            if (assn != null) {
                assn.setSource3(-1);
                if ((assn.getSource2()==-1)&& (assn.getSource1()==-1)&& (assn.getSource4()==-1)&& (assn.getSource5()==-1))
                    assn1054Repository.delete(assn);
                else
                    assn1054Repository.save(assn);
            } else
                return Result.error("404", "No such value");
        }
        if(s4!=-1) {
            assn = assn1054Repository.findBySource4(s4);
            if (assn != null) {
                assn.setSource4(-1);
                if ((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource1()==-1)&& (assn.getSource5()==-1))
                    assn1054Repository.delete(assn);
                else
                    assn1054Repository.save(assn);
            } else
                return Result.error("404", "No such value");
        }
        if(s5!=-1) {
            assn = assn1054Repository.findBySource5(s5);
            if (assn != null) {
                assn.setSource5(-1);
                if ((assn.getSource2()==-1)&& (assn.getSource3()==-1)&& (assn.getSource4()==-1)&& (assn.getSource1()==-1))
                    assn1054Repository.delete(assn);
                else
                    assn1054Repository.save(assn);
            } else
                return Result.error("404", "No such value");
        }
        assn1054Repository.save(res);
        List<Assn1054> assn3s = assn1054Repository.findAll();
        List<Assn1054> data = new ArrayList<>();
        data.add(res);
        for(Assn1054 as : assn3s)
        {
            if (!as.equals(res))
                data.add(as);
        }
        return Result.success(data);
    }

    @PostMapping("/updateAssn/assn2802")
    public Result update2802(@RequestBody Assn2802 assn1)
    {
        int s2 = assn1.getSource2();
        int s1 = assn1.getSource1();
        int s3 = assn1.getSource3();
        Assn2802 a;
        if(s1!=-1) {
            a = assn2802Repository.findAssn1BySource1(s1);
            if(a==null)
                return Result.error("404","没有这条记录！");
            a.setSource1(-1);

            if ((a.getSource3()==-1)&&(a.getSource2()==-1))
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        if(s2!=-1) {
            a = assn2802Repository.findAssn1BySource2(s2);
            if(a==null)
                return Result.error("404","没有这条记录！");
            a.setSource2(-1);

            if ((a.getSource1()==-1) && (a.getSource3()==-1))
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        if(s3!=-1) {
            a = assn2802Repository.findAssn1BySource3(s3);
            if(a==null)
                return Result.error("404","没有这条记录！");
            a.setSource3(-1);

            if (a.getSource1()==-1 && a.getSource2()==-1)
                assn2802Repository.delete(a);
            else
                assn2802Repository.save(a);
        }
        a = new Assn2802(assn1);
        assn2802Repository.save(a);
        List<Assn2802> assn1s = assn2802Repository.findAll();
        List<Assn2802> data = new ArrayList<>();
        data.add(a);
        for(Assn2802 as : assn1s)
        {
            if(!as.equals(a))
                data.add(as);
        }

        return Result.success(data);
    }

    @PostMapping("/updateAssn/assn3223")
    public Result update3223(@RequestBody Assn3223 assn2)
    {

        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn3223 a = new Assn3223();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn3223 assn;
        if (s1!=-1) {
            assn = assn3223Repository.findAssn2BySource1(s1);
            if(assn==null)
                return Result.error("404","没有这条记录！");
            assn.setSource1(-1);

            if (assn.getSource2()==-1)
                assn3223Repository.delete(assn);
            else
                assn3223Repository.save(assn);
        }
        if(s2!=-1) {
            assn = assn3223Repository.findAssn2BySource2(s2);
            if(assn==null)
                return Result.error("404","没有这条记录！");
            assn.setSource2(-1);
            if (assn.getSource1()==-1)
                assn3223Repository.delete(assn);
            else
                assn3223Repository.save(assn);
        }
        assn3223Repository.save(a);

        List<Assn3223> data = new ArrayList<>();
        data.add(a);
        List<Assn3223> assn2s = assn3223Repository.findAll();
        for (Assn3223 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }

    @PostMapping("/updateAssn/assn0")
    public Result update0(@RequestBody Assn0 assn2) {

        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn0 a = new Assn0();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn0 assn;
        if (s1 != -1) {
            assn = assn0Repository.findAssn2BySource1(s1);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource1(-1);

            if (assn.getSource2() == -1)
                assn0Repository.delete(assn);
            else
                assn0Repository.save(assn);
        }
        if (s2 != -1) {
            assn = assn0Repository.findAssn2BySource2(s2);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn0Repository.delete(assn);
            else
                assn0Repository.save(assn);
        }
        assn0Repository.save(a);

        List<Assn0> data = new ArrayList<>();
        data.add(a);
        List<Assn0> assn2s = assn0Repository.findAll();
        for (Assn0 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }


    @PostMapping("/updateAssn/assn88")
    public Result update88(@RequestBody Assn88 assn2) {

        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn88 a = new Assn88();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn88 assn;
        if (s1 != -1) {
            assn = assn88Repository.findAssn2BySource1(s1);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource1(-1);

            if (assn.getSource2() == -1)
                assn88Repository.delete(assn);
            else
                assn88Repository.save(assn);
        }
        if (s2 != -1) {
            assn = assn88Repository.findAssn2BySource2(s2);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn88Repository.delete(assn);
            else
                assn88Repository.save(assn);
        }
        assn88Repository.save(a);

        List<Assn88> data = new ArrayList<>();
        data.add(a);
        List<Assn88> assn2s = assn88Repository.findAll();
        for (Assn88 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }

    @PostMapping("/updateAssn/assn2619")
    public Result update2619(@RequestBody Assn2619 assn2) {

        int s1 = assn2.getSource1();
        int s2 = assn2.getSource2();
        Assn2619 a = new Assn2619();
        a.setSource1(assn2.getSource1());
        a.setSource2(assn2.getSource2());
        Assn2619 assn;
        if (s1 != -1) {
            assn = assn2619Repository.findAssn2BySource1(s1);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource1(-1);

            if (assn.getSource2() == -1)
                assn2619Repository.delete(assn);
            else
                assn2619Repository.save(assn);
        }
        if (s2 != -1) {
            assn = assn2619Repository.findAssn2BySource2(s2);
            if (assn == null)
                return Result.error("404", "没有这条记录！");
            assn.setSource2(-1);
            if (assn.getSource1() == -1)
                assn2619Repository.delete(assn);
            else
                assn2619Repository.save(assn);
        }
        assn2619Repository.save(a);

        List<Assn2619> data = new ArrayList<>();
        data.add(a);
        List<Assn2619> assn2s = assn2619Repository.findAll();
        for (Assn2619 as : assn2s) {
            if (!as.equals(a))
                data.add(as);

        }
        return Result.success(data);
    }

    @PostMapping("/delAssn/assn1054")
    public Result del1054(@RequestBody Assn1054 assn3) {
        int s1 = assn3.getSource1();
        int s2 = assn3.getSource2();
        int s3 = assn3.getSource3();
        int s4 = assn3.getSource4();
        int s5 = assn3.getSource5();
        Boolean res = assn1054Repository.existsById(assn3.getId());
        Assn1054 assn = assn1054Repository.findById(assn3.getId());
        if(!res)
            return Result.error("404","No such value!");

        if(assn.getSource1()==-1||assn.getSource2()==-1|| assn.getSource3()==-1|| assn.getSource4()==-1|| assn.getSource5()==-1)
            return Result.success(assn1054Repository.findAll()
                    .stream()
                    .sorted(Comparator.comparingInt(Assn1054::getSource1).reversed())
                    .toList());

        assn.setAssn3(s1,-1,-1,-1,-1);
        assn1054Repository.save(assn);
        assn = new Assn1054();
        assn.setAssn3(-1,s2,-1,-1,-1);
        assn1054Repository.save(assn);
        assn = new Assn1054();
        assn.setAssn3(-1,-1,s3,-1,-1);
        assn1054Repository.save(assn);
        assn = new Assn1054();
        assn.setAssn3(-1,-1,-1,s4,-1);
        assn1054Repository.save(assn);
        assn = new Assn1054();
        assn.setAssn3(-1,-1,-1,-1,s5);
        assn1054Repository.save(assn);
        List<Assn1054> ret = assn1054Repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Assn1054::getSource1).reversed())
                .toList();
        return Result.success(ret);
    }

    @PostMapping("/delAssn/assn2802")
    public Result del2802(@RequestBody Assn2802 assn1)
    {
        int id = assn1.getId();
        System.out.println(id);
        Boolean res = assn2802Repository.existsById(id);
        if(!res)
            return Result.error("404","该记录不存在！");
        else
        {
            Assn2802 assn = assn2802Repository.findById(id).get();
            if(assn.getSource3() == -1 || assn.getSource1() == -1 || assn.getSource2() == -1)
            {
                return Result.success(assn2802Repository.findAll()
                        .stream().
                        sorted(Comparator.comparingInt(Assn2802::getSource1).reversed())
                        .collect(Collectors.toList()));
            }
            assn = new Assn2802();
            assn.setS1andS2andS3(-1,assn1.getSource2(),-1);
            assn2802Repository.save(assn);
            assn = new Assn2802();
            assn.setS1andS2andS3(-1,-1,assn1.getSource3());
            assn2802Repository.save(assn);
            assn1.setSource2(-1);
            assn1.setSource3(-1);
            assn2802Repository.save(assn1);
        }
        List<Assn2802> ret = assn2802Repository.findAll()
                .stream().
                sorted(Comparator.comparingInt(Assn2802::getSource1).reversed())
                .collect(Collectors.toList());
        return Result.success(ret);
    }

    @PostMapping("/delAssn/assn3223")
    public Result del3223(@RequestBody Assn3223 assn2)
    {
        int id = assn2.getId();
        System.out.println(id);
        Boolean res = assn3223Repository.existsById(id);
        if(!res)
            return Result.error("404","该记录不存在！");
        else
        {
            Assn3223 assn = assn3223Repository.findById(id).get();
            if((assn.getSource1()==-1) || (assn.getSource2()==-1))
            {
                return Result.success( assn3223Repository.findAll()
                        .stream()
                        .sorted(Comparator.comparingInt(Assn3223::getSource1).reversed())
                        .toList());
            }
            int source2 = assn.getSource2();
            assn.setSource2(-1);
            assn3223Repository.save(assn);
            assn = new Assn3223();
            assn.setSource2(source2);
            assn.setSource1(-1);
            assn3223Repository.save(assn);

        }
        List<Assn3223> ret = assn3223Repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Assn3223::getSource1).reversed())
                .toList();
        return Result.success(ret);
    }

    @PostMapping("/delAssn/assn0")
    public Result del0(@RequestBody Assn0 assn2) {
        int id = assn2.getId();
        System.out.println(id);
        Boolean res = assn0Repository.existsById(id);
        if (!res)
            return Result.error("404", "该记录不存在！");
        else {
            Assn0 assn = assn0Repository.findById(id).get();
            if ((assn.getSource1() == -1) || (assn.getSource2() == -1)) {
                return Result.success(assn0Repository.findAll()
                        .stream()
                        .sorted(Comparator.comparingInt(Assn0::getSource1).reversed())
                        .toList());
            }
            int source2 = assn.getSource2();
            assn.setSource2(-1);
            assn0Repository.save(assn);
            assn = new Assn0();
            assn.setSource2(source2);
            assn.setSource1(-1);
            assn0Repository.save(assn);

        }
        List<Assn0> ret = assn0Repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Assn0::getSource1).reversed())
                .toList();
        return Result.success(ret);
    }

    @PostMapping("/delAssn/assn88")
    public Result del88(@RequestBody Assn88 assn2) {
        int id = assn2.getId();
        System.out.println(id);
        Boolean res = assn88Repository.existsById(id);
        if (!res)
            return Result.error("404", "该记录不存在！");
        else {
            Assn88 assn = assn88Repository.findById(id).get();
            if ((assn.getSource1() == -1) || (assn.getSource2() == -1)) {
                return Result.success(assn88Repository.findAll()
                        .stream()
                        .sorted(Comparator.comparingInt(Assn88::getSource1).reversed())
                        .toList());
            }
            int source2 = assn.getSource2();
            assn.setSource2(-1);
            assn88Repository.save(assn);
            assn = new Assn88();
            assn.setSource2(source2);
            assn.setSource1(-1);
            assn88Repository.save(assn);

        }
        List<Assn88> ret = assn88Repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Assn88::getSource1).reversed())
                .toList();
        return Result.success(ret);
    }

    @PostMapping("/delAssn/assn2619")
    public Result del2619(@RequestBody Assn2619 assn2) {
        int id = assn2.getId();
        System.out.println(id);
        Boolean res = assn2619Repository.existsById(id);
        if (!res)
            return Result.error("404", "该记录不存在！");
        else {
            Assn2619 assn = assn2619Repository.findById(id).get();
            if ((assn.getSource1() == -1) || (assn.getSource2() == -1)) {
                return Result.success(assn2619Repository.findAll()
                        .stream()
                        .sorted(Comparator.comparingInt(Assn2619::getSource1).reversed())
                        .toList());
            }
            int source2 = assn.getSource2();
            assn.setSource2(-1);
            assn2619Repository.save(assn);
            assn = new Assn2619();
            assn.setSource2(source2);
            assn.setSource1(-1);
            assn2619Repository.save(assn);

        }
        List<Assn2619> ret = assn2619Repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Assn2619::getSource1).reversed())
                .toList();
        return Result.success(ret);
    }


    public JSONArray runPython(String table, List<String> weights) throws IOException, InterruptedException {
        //String scriptPath = "/root/python/4/assn-"+table+".py";
        String scriptPath = "/home/ubuntu/python/assn-" + table + ".py";
        String[] argument = new String[]{"/home/ubuntu/anaconda3/envs/myenv/bin/python", scriptPath, weights.get(0), weights.get(1), weights.get(2), weights.get(3), weights.get(4), weights.get(5), weights.get(6), weights.get(7), weights.get(8), weights.get(9), weights.get(10)};

        JSONArray result = new JSONArray();
        try {
            //运行python文件
            Process process = Runtime.getRuntime().exec(argument);
            //获取python输出信息
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            String line = null;
            int i = 1;
            while((line=in.readLine())!=null)
            {
                String[] row =line.split(",");
                JSONObject temp=new JSONObject(true);
                temp.put("id",i);
                temp.put("9001",Float.parseFloat(row[0]));
                temp.put("9002",Float.parseFloat(row[1]));
                result.add(temp);
                i+=1;
            }
            in.close();
            //等待当前python程序执行完毕
            System.out.println(process.waitFor());
        }catch (IOException e){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();}
        return result;
    }
}
