package cn.cagurzhan.service;

import cn.cagurzhan.entity.Menu;
import cn.cagurzhan.view.MenuView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * (Menu)表服务接口
 *
 * @author makejava
 * @since 2023-09-24 11:48:20
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuView> selectRouterMenuTreeByUserId(Long id);
}
