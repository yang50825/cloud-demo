package com.jk.controller;

import com.jk.entity.DemoBean;
import com.jk.feign.DemoFeign;
import com.jk.util.OSSUtil;
import com.jk.util.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    /**
     * TODO
     * 
     * @Description 上传图片 
     * @Return java.lang.String
     * @Author 杨帅
     * @Date 2022/11/16 11:25     
     **/
    @RequestMapping("uploadImg")
    public String upload(MultipartFile file) throws Exception{
        String upload2oss = OSSUtil.upload2oss(file);
        System.out.println("upload2oss = " + upload2oss);
        return upload2oss;
    }

    /**
     * 返回图片上传后存储的地址
     * @param multipartFile
     * @return
     */
    @PostMapping("upload/image")
    public String uploadImage(@RequestParam(value = "file") MultipartFile multipartFile){
        if (multipartFile.isEmpty()){
            return "文件有误！";
        }
        return UploadUtils.uploadImage(multipartFile);
    }

}
