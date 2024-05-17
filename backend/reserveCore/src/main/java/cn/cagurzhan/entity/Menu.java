package cn.cagurzhan.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2023-09-24 11:48:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_menu")
public class Menu  {
    @TableId
    private Long id;
    //菜单名字
    private String menuName;
    //父菜单ID
    private Long parentId;
    //展示顺序
    private Integer orderNum;
    //路由地址
    private String path;
    //路由组件
    private String component;
    //菜单类型(M目录 C菜单 F按钮)
    private String menuType;
    //权限标识
    private String perms;
    //菜单图标
    private String icon;
    //删除标识
    private Integer delFlag;
}
