package cn.cagurzhan.controller;

import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.dto.ReserveDTO;
import cn.cagurzhan.dto.ReserveDTOBatch;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.ReserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@RequestMapping("/reserve")
@Api(tags = "预约服务相关接口")
public class ReserveController {

    /** 预约服务类 */
    @Autowired
    private ReserveService reserveService;


    @Deprecated
    @GetMapping("/getByGPU")
    @ApiOperation(value = "通过GPU的id查询预约信息",notes = "通过GPU的id查询预约信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gpuId", value = "GPU的ID"),
    })
    public ResponseResult getByGPU(Integer gpuId){
        return reserveService.getByGPU(gpuId);
    }


    @PostMapping("/getByGPUs")
    @ApiOperation(value = "通过多个GPU的id查询预约信息",notes = "通过多个GPU的id查询预约信息")
    public ResponseResult getByGPUs(@RequestBody Ids gpuIds){
        return reserveService.getByGPUs(gpuIds);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有预约信息",notes = "获取所有预约信息")
    public ResponseResult list(){
        return reserveService.listAll();
    }


    @GetMapping("/getMyCount")
    @ApiOperation(value = "查询我的预约数量",notes = "查询我的预约数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID"),
    })
    public ResponseResult getMyCount(Long userId){
        return reserveService.getMyCount(userId);
    }


    @GetMapping("/getMy")
    @ApiOperation(value = "通过用户ID查询我的预约",notes = "通过用户ID查询我的预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID"),
    })
    public ResponseResult getMy(Long userId){
        return reserveService.getMy(userId);
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增预约记录",notes = "用户新增预约记录")
    public ResponseResult add(@RequestBody ReserveDTO reserveDTO){
        return reserveService.add(reserveDTO);
    }

    @PostMapping("/addBatch")
    @ApiOperation(value = "批量新增预约记录",notes = "用户批量新增预约记录")
    public ResponseResult addBatch(@RequestBody ReserveDTOBatch reserveDTOBatch){
        return reserveService.addBatch(reserveDTOBatch);
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新预约记录",notes = "用户更新预约记录")
    public ResponseResult updateReserve(@RequestBody ReserveDTO reserveDTO){
        return reserveService.updateReserve(reserveDTO);
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除预约记录",notes = "用户删除预约记录")
    public ResponseResult deleteReserve(@RequestBody ReserveDTO reserveDTO){
        return reserveService.deleteReserve(reserveDTO);
    }
}
