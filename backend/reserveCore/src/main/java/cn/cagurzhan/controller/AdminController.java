package cn.cagurzhan.controller;

import cn.cagurzhan.dto.AdminUserUpdateDTO;
import cn.cagurzhan.entity.Global;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.AdminService;
import cn.cagurzhan.service.GlobalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员相关接口")
public class AdminController {

    /** 负责管理后台的服务 */
    @Autowired
    private AdminService adminService;

    /** 负责管理网站基本信息的服务 */
    @Autowired
    private GlobalService globalService;

    @GetMapping("/getUser")
    @PreAuthorize("@ps.hasPermission('system:admin:user')")
    @ApiOperation(value = "获取用户列表",notes = "在管理员后台分页查询所有用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult getUser(Integer pageNum, Integer pageSize){
        return adminService.getUser(pageNum,pageSize);
    }


    @PostMapping("/updateUser")
    @PreAuthorize("@ps.hasPermission('system:admin:user')")
    @ApiOperation(value = "更新用户信息",notes = "管理员更新用户信息")
    public ResponseResult updateUser(@RequestBody AdminUserUpdateDTO adminUserUpdateDTO){
        return adminService.updateUser(adminUserUpdateDTO);
    }


    @PostMapping("/toggleStatus")
    @PreAuthorize("@ps.hasPermission('system:admin:user')")
    @ApiOperation(value = "更改用户状态",notes = "修改用户状态为冻结或正常")
    public ResponseResult toggleStatus(@RequestBody AdminUserUpdateDTO adminUserUpdateDTO){
        return adminService.toggleStatus(adminUserUpdateDTO);
    }


    @PostMapping("/deleteUser")
    @PreAuthorize("@ps.hasPermission('system:admin:user')")
    @ApiOperation(value = "删除用户",notes = "管理员删除某个用户")
    public ResponseResult deleteUser(@RequestBody AdminUserUpdateDTO adminUserUpdateDTO){
        return adminService.deleteUser(adminUserUpdateDTO);
    }


    @PostMapping("/addUser")
    @PreAuthorize("@ps.hasPermission('system:admin:user')")
    @ApiOperation(value = "添加用户",notes = "管理员手动添加某个用户")
    public ResponseResult addUser(@RequestBody AdminUserUpdateDTO adminUserUpdateDTO){
        return adminService.addUser(adminUserUpdateDTO);
    }


    @PostMapping("/updateGlobal")
    @PreAuthorize("@ps.hasPermission('system:admin:global')")
    @ApiOperation(value = "设置网站基本信息",notes = "管理员设置网站基本信息")
    public ResponseResult updateGlobal(@RequestBody Global global){
        return globalService.updateGlobal(global);
    }

    @GetMapping("/getGlobal")
    @ApiOperation(value = "获取网站基本信息",notes = "获取网站基本信息")
    public ResponseResult getGlobal(){
        return ResponseResult.okResult(globalService.getById(1));
    }


}
