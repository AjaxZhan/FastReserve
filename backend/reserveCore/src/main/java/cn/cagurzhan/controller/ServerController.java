package cn.cagurzhan.controller;

import cn.cagurzhan.dto.ServerDAO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.ServerService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.view.ServerView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@RequestMapping("/server")
@Api(tags = "服务器相关接口")
public class ServerController {

    /** 服务服务类 */
    @Autowired
    private ServerService serverService;


    @GetMapping("/list")
    @ApiOperation(value = "获取所有服务器信息",notes = "获取所有服务器信息")
    public ResponseResult list(){
        List<ServerView> serverViews = BeanCopyUtils.copyBeanList(serverService.list(), ServerView.class);
        return ResponseResult.okResult(serverViews);
    }


    @GetMapping("/getAll")
    @PreAuthorize("@ps.hasPermission('system:admin:server')")
    @ApiOperation(value = "分页获取所有服务器信息",notes = "管理员后台分页获取所有服务器信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小"),
    })
    public ResponseResult getAll(Integer pageNum , Integer pageSize){
        return serverService.getAll(pageNum, pageSize);
    }


    @PostMapping("/add")
    @PreAuthorize("@ps.hasPermission('system:admin:server')")
    @ApiOperation(value = "新增服务器",notes = "管理员后台新增服务器")
    public ResponseResult add(@RequestBody ServerDAO serverDAO){
        return serverService.add(serverDAO);
    }


    @PostMapping("/delete")
    @PreAuthorize("@ps.hasPermission('system:admin:server')")
    @ApiOperation(value = "删除服务器",notes = "管理员后台删除服务器")
    public ResponseResult delete(@RequestBody ServerView serverView){
        return serverService.delete(serverView);
    }


    @PostMapping("/update")
    @PreAuthorize("@ps.hasPermission('system:admin:server')")
    @ApiOperation(value = "更新服务器信息",notes = "管理员后台更新服务器信息")
    public ResponseResult update(@RequestBody ServerView serverView){
        return serverService.updateServer(serverView);
    }

}
