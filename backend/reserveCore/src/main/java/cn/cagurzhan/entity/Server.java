package cn.cagurzhan.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Server)表实体类
 *
 * @author makejava
 * @since 2023-09-14 14:40:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_server")
public class Server  {
@TableId
    private Long id;

//附带多少张GPU卡
    private Integer volume;
//服务器名字
    private String name;
//0正常，1停用
    private String status;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String ip;

    private Integer delFlag;



}
