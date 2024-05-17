package cn.cagurzhan.controller;

import cn.cagurzhan.dto.GpuDTO;
import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.GpuService;
import cn.cagurzhan.service.impl.GpuStatusServiceImpl;
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
@RequestMapping("/gpu")
@Api(tags = "GPU信息相关接口")
public class GpuController {

    /** GPU相关服务 */
    @Autowired
    private GpuService gpuService;

    /** GPU状态相关服务 */
    @Autowired
    private GpuStatusServiceImpl gpuStatusService;


    @GetMapping("/getGPUStatus")
    @ApiOperation(value = "通过ID查询GPU状态信息",notes = "根据ID，查询GPU状态信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "GPU的ID"),
    })
    public ResponseResult getGPUStatus(Integer id){
        return gpuStatusService.getFromRedis(id);
    }


    @GetMapping("/getGPU")
    @ApiOperation(value = "查询所有GPU",notes = "查询所有GPU")
    public ResponseResult getGPU(){
        return gpuService.getGPU();
    }



    @Deprecated
    @GetMapping("/getGPUByServerId")
    @ApiOperation(value = "根据服务器ID查询GPU",notes = "根据服务器ID查询GPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务器ID"),
    })
    public ResponseResult getGPUByServerId(Long serverId){
        return gpuService.getGPUByServerId(serverId);
    }


    @PostMapping("/getGPUByServerIds")
    @ApiOperation(value = "根据多个服务器ID查询GPU",notes = "根据多个服务器ID查询GPU")
    public ResponseResult getGPUByServerIds(@RequestBody Ids serverIds){
        System.out.println("serverIds = " + serverIds);
        return gpuService.getGPUByServerIds(serverIds);
    }


    @GetMapping("/getGPUPage")
    @PreAuthorize("@ps.hasPermission('system:admin:gpu')")
    @ApiOperation(value = "分页查询GPU信息",notes = "管理员后台分页查询GPU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小"),
    })
    public ResponseResult getGPUPage(Integer pageNum, Integer pageSize){
        return gpuService.getGPUPage(pageNum, pageSize);
    }


    @PostMapping("/addGPU")
    @PreAuthorize("@ps.hasPermission('system:admin:gpu')")
    @ApiOperation(value = "添加GPU信息",notes = "管理员后台添加GPU信息")
    public ResponseResult addGPU(@RequestBody GpuDTO gpuDTO){
        return gpuService.addGPU(gpuDTO);
    }


    @PostMapping("/deleteGPU")
    @PreAuthorize("@ps.hasPermission('system:admin:gpu')")
    @ApiOperation(value = "删除GPU信息",notes = "管理员后台删除GPU信息")
    public ResponseResult deleteGPU(@RequestBody GpuDTO gpuDTO){
        return gpuService.deleteGPU(gpuDTO);
    }


    @PostMapping("/updateGPU")
    @PreAuthorize("@ps.hasPermission('system:admin:gpu')")
    @ApiOperation(value = "更新GPU信息",notes = "管理员后台更新GPU信息")
    public ResponseResult updateGPU(@RequestBody GpuDTO gpuDTO){
        return gpuService.updateGPU(gpuDTO);
    }

}
