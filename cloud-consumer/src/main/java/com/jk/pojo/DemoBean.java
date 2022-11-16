package com.jk.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:21
 **/
@Data
public class DemoBean implements Serializable {

    private Integer demoId;
    private String demoName;
    private String demoAuthor;
    private String demoDesc;
    private String demoImgUrl;
    private String demoStatus;

}
