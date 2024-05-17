package cn.cagurzhan.controller;

import cn.cagurzhan.dto.BulletinDTO;
import cn.cagurzhan.entity.Bulletin;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.service.BulletinService;
import cn.cagurzhan.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@RequestMapping("/bulletin")
@Api(tags = "网站公告相关接口")
public class BulletinController {

    /** 公告服务 */
    @Autowired
    private BulletinService bulletinService;


    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有公告",notes = "网站用户获取所有公告")
    public ResponseResult getAll(){
        LambdaQueryWrapper<Bulletin> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Bulletin::getUpdateTime);
        List<BulletinDTO> bulletinDTOS = BeanCopyUtils.copyBeanList(bulletinService.list(wrapper), BulletinDTO.class);
        return ResponseResult.okResult(bulletinDTOS);
    }


    @ApiOperation(value = "分页获取公告",notes = "管理员后台分页获取所有公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    @GetMapping("/getPage")
    public ResponseResult getPage(Integer pageNum, Integer pageSize){
        return bulletinService.getPage(pageNum,pageSize);
    }


    @PostMapping("/add")
    @PreAuthorize("@ps.hasPermission('system:admin:bulletin')")
    @ApiOperation(value = "添加公告",notes = "管理员添加公告")
    public ResponseResult add(@RequestBody BulletinDTO bulletinDTO){
        bulletinDTO.setCreateTime(new Date());
        bulletinDTO.setUpdateTime(new Date());
        Bulletin bulletin = BeanCopyUtils.copyBean(bulletinDTO, Bulletin.class);
        bulletin.setDelFlag(0);
        bulletinService.save(bulletin);
        return ResponseResult.okResult();
    }


    @PostMapping("/delete")
    @PreAuthorize("@ps.hasPermission('system:admin:bulletin')")
    @ApiOperation(value = "删除公告",notes = "管理员删除公告")
    public ResponseResult delete(@RequestBody BulletinDTO bulletinDTO){
        Bulletin byId = bulletinService.getById(bulletinDTO.getId());
        if(byId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"查无此公告");
        }
        bulletinService.removeById(bulletinDTO.getId());
        return ResponseResult.okResult();
    }


    @PostMapping("/update")
    @PreAuthorize("@ps.hasPermission('system:admin:bulletin')")
    @ApiOperation(value = "更新公告",notes = "管理员更新公告")
    public ResponseResult update(@RequestBody BulletinDTO bulletinDTO){
        Bulletin byId = bulletinService.getById(bulletinDTO.getId());
        if(byId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"查无此公告");
        }
        bulletinDTO.setUpdateTime(new Date());
        Bulletin bulletin = BeanCopyUtils.copyBean(bulletinDTO, Bulletin.class);
        bulletinService.updateById(bulletin);
        return ResponseResult.okResult();
    }

}
