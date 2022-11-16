package com.jk.feign;

import com.jk.pojo.DemoBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:37
 **/
@FeignClient(name = "cloud-provider")
public interface DemoFeign {

    @GetMapping("demo/queryList")
    List<DemoBean> queryList();

    @PostMapping("demo/addDemo")
    void addDemo(@RequestBody DemoBean demo);
}
