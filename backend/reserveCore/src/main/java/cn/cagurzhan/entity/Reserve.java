package cn.cagurzhan.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Reserve)表实体类
 *
 * @author makejava
 * @since 2023-09-14 14:39:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_reserve")
public class Reserve  {
@TableId
    private Long id;

//预约标题
    private String title;
//预约的GPU
    private Long gpuId;
//开始时间
    private Date start;
//结束时间
    private Date end;
//状态，0表示正常，1表示未审核，2表示审核通过
    private Integer status;
//预约说明
    private String comment;
//预约人
    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
//删除标志，1为删除，0为未删除
    private Integer delFlag;



}
