package cn.cagurzhan.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Gpu)表实体类
 *
 * @author makejava
 * @since 2023-09-14 14:34:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_gpu")
public class Gpu  {
@TableId
    private Long id;

    //服务器编号
    private Long serverId;
    //GPU型号
    private String model;
    //GPU编号
    private Integer number;
    //0正常，1停用
    private String status;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer delFlag;
}
