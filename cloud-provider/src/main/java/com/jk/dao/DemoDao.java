package com.jk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jk.entity.DemoBean;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:26
 **/
@CacheNamespace
@Mapper
public interface DemoDao extends BaseMapper<DemoBean> {
}
