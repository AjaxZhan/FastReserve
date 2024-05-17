package cn.cagurzhan.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // 链接编程注解，set方法会返回当前对象。
public class MenuView {
    private Long id;
    private List<MenuView> children;
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
}
