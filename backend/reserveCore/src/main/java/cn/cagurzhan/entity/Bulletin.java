package cn.cagurzhan.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Bulletin)表实体类
 *
 * @author makejava
 * @since 2023-10-01 16:46:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_bulletin")
public class Bulletin  {
    @TableId
    private Long id;

//公告标题
    private String title;
//内容
    private String content;
//创建者
    private Long createBy;
//创建时间
    private Date createTime;
//更新时间
    private Date updateTime;
//删除标记
    private Integer delFlag;



}
