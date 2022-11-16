package com.jk.controller;

import com.jk.pojo.DemoBean;
import com.jk.service.DemoService;
import com.jk.utils.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:19
 **/
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    /**
     * TODO
     *
     * @Description 获取列表
     * @Return java.util.List<com.jk.pojo.DemoBean>
     * @Author 杨帅
     * @Date 2022/11/15 19:24
     **/
    @GetMapping("queryList")
    public List<DemoBean> queryList() {
        return demoService.queryList();
    }

    /**
     * TODO
     *
     * @Description 新增游记
     * @Param demo
     * @Return void
     * @Author 杨帅
     * @Date 2022/11/15 19:52
     **/
    @PostMapping("addDemo")
    public void addDemo(@RequestBody DemoBean demo) {
        demoService.addDemo(demo);
    }

    @RequestMapping("uploadImg")
    public String upload(MultipartFile file) throws Exception{
        String upload2oss = OSSUtil.upload2oss(file);
        System.out.println("upload2oss = " + upload2oss);
        return upload2oss;
    }
}
