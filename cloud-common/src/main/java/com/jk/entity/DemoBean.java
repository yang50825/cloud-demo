package com.jk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @Description
 * @Author 杨帅
 * @Date 2022/11/15 19:21
 **/
@TableName("demo")
@Data
public class DemoBean implements Serializable {

    @TableId(value = "demo_id", type = IdType.AUTO)
    private Integer demoId;
    @TableField("demo_name")
    private String demoName;
    @TableField("demo_author")
    private String demoAuthor;
    @TableField("demo_desc")
    private String demoDesc;
    @TableField("demo_img_url")
    private String demoImgUrl;
    @TableField("demo_status")
    private String demoStatus;

}
