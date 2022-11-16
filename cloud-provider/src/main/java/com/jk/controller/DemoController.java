package com.jk.controller;

import com.jk.api.DemoServiceApi;
import com.jk.entity.DemoBean;
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
public class DemoController implements DemoServiceApi {

    @Autowired
    private DemoService demoService;


    @Override
    public List<DemoBean> queryList() {
        return demoService.queryList();
    }

    @Override
    public void addDemo(DemoBean demo) {
        demoService.addDemo(demo);
    }
}
