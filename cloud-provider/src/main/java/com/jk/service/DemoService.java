package com.jk.service;


import com.jk.entity.DemoBean;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:20
 **/
public interface DemoService {

    /**
     * TODO
     * 
     * @Description 获取列表
     * @Return java.util.List<com.jk.pojo.DemoBean>
     * @Author 杨帅
     * @Date 2022/11/15 19:24     
     **/
    List<DemoBean> queryList();

    /**
     * TODO
     *
     * @Description 新增游记
     * @Return void
     * @Author 杨帅
     * @Date 2022/11/15 19:52
     **/
    void addDemo(DemoBean demo);
}
