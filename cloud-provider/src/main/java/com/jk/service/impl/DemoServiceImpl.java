package com.jk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jk.dao.DemoDao;
import com.jk.entity.DemoBean;
import com.jk.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:25
 **/
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    /**
     * TODO
     *
     * @Description 获取列表
     * @Return java.util.List<com.jk.pojo.DemoBean>
     * @Author 杨帅
     * @Date 2022/11/15 19:25
     **/
    @Override
    public List<DemoBean> queryList() {
        QueryWrapper<DemoBean> wrapper = new QueryWrapper<>();
        List<DemoBean> list = demoDao.selectList(wrapper);
        return list;
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
    @Override
    public void addDemo(DemoBean demo) {
        demoDao.insert(demo);
    }
}
