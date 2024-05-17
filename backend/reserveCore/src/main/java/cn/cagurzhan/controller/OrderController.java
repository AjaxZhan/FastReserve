package cn.cagurzhan.controller;

import cn.cagurzhan.dto.ReserveDTO;
import cn.cagurzhan.dto.ReserveDTOBatch;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.ReserveService;
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
@RequestMapping("/order")
@Api(tags = "工单相关接口")
public class OrderController {

    /** 预约服务 */
    @Autowired
    private ReserveService reserveService;

    @PostMapping("/addBatch")
    @ApiOperation(value = "批量添加工单",notes = "批量添加工单")
    public ResponseResult addBatch(@RequestBody ReserveDTOBatch reserveDTOBatch){
        return reserveService.addOrderBatch(reserveDTOBatch);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加工单",notes = "用户添加工单")
    public ResponseResult add(@RequestBody ReserveDTO reserveDTO){
        return reserveService.addOrder(reserveDTO);
    }


    @PostMapping("/verify")
    @PreAuthorize("@ps.hasPermission('system:admin:order')")
    @ApiOperation(value = "审核工单",notes = "管理员审核工单")
    public ResponseResult verify(@RequestBody ReserveDTO reserveDTO){
        return reserveService.verify(reserveDTO);
    }


    @PostMapping("/fail")
    @PreAuthorize("@ps.hasPermission('system:admin:order')")
    @ApiOperation(value = "驳回工单",notes = "管理员驳回工单")
    public ResponseResult fail(@RequestBody ReserveDTO reserveDTO){
        return reserveService.fail(reserveDTO);
    }


    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:admin:order')")
    @ApiOperation(value = "分页获取工单信息",notes = "管理员后台分页获取工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult list(Integer pageNum, Integer pageSize){
        return reserveService.listOrder(pageNum,pageSize);
    }


    @GetMapping("/listMy")
    @ApiOperation(value = "分页获取某个用户工单信息",notes = "分页获取某个用户工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult listMy(Long userId,Integer pageNum, Integer pageSize){
        return reserveService.listMyOrder(userId, pageNum,pageSize);
    }
}
