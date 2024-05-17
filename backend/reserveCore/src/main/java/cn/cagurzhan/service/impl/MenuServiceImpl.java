package cn.cagurzhan.service.impl;

import cn.cagurzhan.entity.Menu;
import cn.cagurzhan.mapper.MenuMapper;
import cn.cagurzhan.service.MenuService;
import cn.cagurzhan.service.RoleService;
import cn.cagurzhan.service.UserService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.view.MenuView;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-09-24 11:48:20
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /** 角色服务类*/
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * 通过用户ID查找权限信息。
     * 要求：
     *      管理员返回所有权限；用户返回具有的权限。
     *      菜单类型为C或F（菜单/按钮）
     * */
    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 判断是否为管理员
        if (userService.getById(id).getType().equals("1")) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,"C","M");
            List<Menu> list = list(wrapper);
            List<String> perms = list.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        // 复杂SQL，使用XML方式
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<MenuView> selectRouterMenuTreeByUserId(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        List<MenuView> menuViews = null;

        // 判断是否是管理员
        if (userService.getById(id).getType().equals("1")) {
            menus = list();
            menuViews = BeanCopyUtils.copyBeanList(menus, MenuView.class);
        }else{
            // 查询用户具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(id);
            menuViews = BeanCopyUtils.copyBeanList(menus, MenuView.class);
        }
        // 构建父子关系
        List<MenuView> menuTree = buildMenuViewTree(menuViews,0L);
        return menuTree;
    }
    /**
     * Help function
     * 构建菜单的父子层级关系
     * */
    private List<MenuView> buildMenuViewTree(List<MenuView> menuViews,Long parentId) {
        // 找出第一层菜单
         return menuViews.stream()
                .filter(menuView -> menuView.getParentId().equals(parentId))
                .map(menuView -> menuView.setChildren(getChildren(menuViews, menuView)))
                .collect(Collectors.toList());
    }

    /**
     * Helper function
     * 寻找子菜单
     */
    private List<MenuView> getChildren(List<MenuView> menuViews,MenuView menuView) {
        return menuViews.stream()
                .filter(menuView1 -> menuView1.getParentId().equals(menuView.getId()))
                .collect(Collectors.toList());
    }
}

