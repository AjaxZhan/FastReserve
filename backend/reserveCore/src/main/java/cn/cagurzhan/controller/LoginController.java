package cn.cagurzhan.controller;

import cn.cagurzhan.dto.LoginDTO;
import cn.cagurzhan.entity.LoginUser;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.LoginService;
import cn.cagurzhan.service.MenuService;
import cn.cagurzhan.service.RoleService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.utils.SecurityUtils;
import cn.cagurzhan.view.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@Api(tags = "登录相关接口")
public class LoginController {
    /** 登录服务类 */
    @Autowired
    private LoginService loginService;
    /** 权限服务类 */
    @Autowired
    private MenuService menuService;
    /** 角色服务类 */
    @Autowired
    private RoleService roleService;


    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "用户登录")
    public ResponseResult<LoginView> login(@RequestBody LoginDTO loginDTO){
        return loginService.login(loginDTO);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "退出登录",notes = "用户退出登录")
    public ResponseResult logout(){
        return loginService.logout();
    }


    @GetMapping("/getInfo")
    @ApiOperation(value = "获取用户信息和权限信息",notes = "登录成功后，获取用户信息和权限信息")
    public ResponseResult<AdminUserInfoView> getInfo(){
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据用户ID查找权限信息
        List<String> perms =  menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户ID查找角色信息
//        List<String> roleKeyList = null;
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 获取用户信息
        UserView user = BeanCopyUtils.copyBean(loginUser.getUser(), UserView.class);
        //封装返回
        AdminUserInfoView adminUserInfoView = new AdminUserInfoView(perms, roleKeyList, user);
        return ResponseResult.okResult(adminUserInfoView);
    }


    @GetMapping("/getRouters")
    @ApiOperation(value = "获取用户能访问的菜单数据",notes = "登录成功后，获取用户能访问的菜单数据")
    public ResponseResult<RoutersView> getRouter(){
        Long userId = SecurityUtils.getLoginUser().getUser().getId();
        List<MenuView> menus =  menuService.selectRouterMenuTreeByUserId(userId);
        RoutersView routersView = new RoutersView(menus);
        return ResponseResult.okResult(routersView);
    }


}
