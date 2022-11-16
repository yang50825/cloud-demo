package com.jk.api;

import com.jk.entity.DemoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/16 15:39
 **/
public interface DemoServiceApi {
    @GetMapping("demo/queryList")
    List<DemoBean> queryList();

    @PostMapping("demo/addDemo")
    void addDemo(@RequestBody DemoBean demo);
}
