package com.jk.controller;

import com.jk.feign.DemoFeign;
import com.jk.pojo.DemoBean;
import com.jk.util.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:37
 **/
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoFeign demoFeign;
    
    /**
     * TODO
     * 
     * @Description 获取列表
     * @Return java.util.List<com.jk.pojo.DemoBean>
     * @Author 杨帅
     * @Date 2022/11/15 19:41     
     **/
    @RequestMapping("queryList")
    public List<DemoBean> queryList() {
        return demoFeign.queryList();
    }

    /**
     * TODO
     *
     * @Description 新增游记
     * @Return void
     * @Author 杨帅
     * @Date 2022/11/15 19:54
     **/
    @RequestMapping("addDemo")
    public void addDemo(@RequestBody DemoBean demo) {
        demoFeign.addDemo(demo);
    }

    @RequestMapping("uploadImg")
    public String upload(MultipartFile file) throws Exception{
        String upload2oss = OSSUtil.upload2oss(file);
        System.out.println("upload2oss = " + upload2oss);
        return upload2oss;
    }
}
