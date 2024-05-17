package cn.cagurzhan.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-09-14 14:40:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class User  {
    @TableId
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String type; // 0 用户 1管理员
    private String status; //0正常，1停用
    private String sex; //0男，1女
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer delFlag;
}
