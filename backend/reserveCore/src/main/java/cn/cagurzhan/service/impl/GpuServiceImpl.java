package cn.cagurzhan.service.impl;

import cn.cagurzhan.service.ReserveService;
import cn.cagurzhan.dto.GpuDTO;
import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.entity.Gpu;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.Server;
import cn.cagurzhan.mapper.GpuMapper;
import cn.cagurzhan.service.GpuService;
import cn.cagurzhan.service.ServerService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.view.GpuView;
import cn.cagurzhan.view.PageView;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class GpuServiceImpl extends ServiceImpl<GpuMapper, Gpu> implements GpuService {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private GpuMapper gpuMapper;



    @Override
    public ResponseResult getGPU() {
        // 查询GPU，返回id、model、number、status
        List<Gpu> gpuList = list();
        if(gpuList.isEmpty()){
            return ResponseResult.errorResult(400,"GPU为空");
        }
        List<GpuView> gpuViews = BeanCopyUtils.copyBeanList(gpuList, GpuView.class);
        // 利用server_id，查询Server，返回 serverName,serverCount
        for (GpuView gpuView : gpuViews) {
            Server server = serverService.getById(gpuView.getServerId());
            gpuView.setServerName(server.getName());
            gpuView.setServerVolume(server.getVolume());
        }
        // 封装为GpuView
        return ResponseResult.okResult(gpuViews);
    }

    @Override
    public ResponseResult addGPU(GpuDTO gpuDTO) {

        Server byId = serverService.getById(gpuDTO.getServerId());
        if(byId == null){
            return ResponseResult.errorResult(400,"此服务器不存在，无法添加GPU");
        }
        if(gpuDTO.getNumber()>byId.getVolume()){
            return ResponseResult.errorResult(400,"此服务器容量不足，无法添加GPU");
        }
        LambdaQueryWrapper<Gpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Gpu::getServerId,gpuDTO.getServerId());
        wrapper.eq(Gpu::getNumber,gpuDTO.getNumber());
        Gpu one = getOne(wrapper);
        if(one!=null){
            return ResponseResult.errorResult(400,"此编号GPU已存在，请检查是否您填写的编号");
        }
        Gpu gpu = BeanCopyUtils.copyBean(gpuDTO, Gpu.class);
        gpu.setDelFlag(0);
        boolean save = save(gpu);
        if(!save){
            return ResponseResult.errorResult(400,"网络错误，添加失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteGPU(GpuDTO gpuDTO) {
        Server byId = serverService.getById(gpuDTO.getServerId());
        if(byId == null){
            return ResponseResult.errorResult(400,"此服务器不存在，无法删除GPU");
        }
        boolean b = removeById(gpuDTO.getId());
        if(!b){
            return ResponseResult.errorResult(400,"网络错误，添加失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getGPUPage(Integer pageNum, Integer pageSize) {

        Page<Gpu> gpuPage = new Page<>(pageNum,pageSize);
        page(gpuPage);
        List<GpuView> gpuViews = BeanCopyUtils.copyBeanList(gpuPage.getRecords(), GpuView.class);
        PageView pageView = new PageView(gpuViews, gpuPage.getTotal());
        return ResponseResult.okResult(pageView);
    }

    @Override
    public ResponseResult updateGPU(GpuDTO gpuDTO) {
        Gpu gpu = getById(gpuDTO.getId());
        if (gpu == null){
            return ResponseResult.errorResult(400,"此GPU不存在，无法更新");
        }
        Gpu gpu1 = BeanCopyUtils.copyBean(gpuDTO, Gpu.class);
        boolean b = updateById(gpu1);
        if(!b){
            return ResponseResult.errorResult(400,"网络错误，更新失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    @Deprecated
    public ResponseResult getGPUByServerId(Long serverId) {
        LambdaQueryWrapper<Gpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Gpu::getServerId,serverId);
        List<Gpu> list = list(wrapper);
        List<GpuView> gpuViews = BeanCopyUtils.copyBeanList(list, GpuView.class);
        for (GpuView gpuView : gpuViews) {
            Server server = serverService.getById(serverId);
            gpuView.setServerName(server.getName());
            gpuView.setServerVolume(server.getVolume());
        }
        return ResponseResult.okResult(gpuViews);
    }

    /**
     * 通过多个服务器id 查询gpu
     * @param serverIds server的id集合
     */
    @Override
    public ResponseResult getGPUByServerIds(Ids serverIds) {
        LambdaQueryWrapper<Gpu> wrapper = new LambdaQueryWrapper<>();
        List<Gpu> gpus = new ArrayList<>();
        System.out.println("====>"+ serverIds);
        for (Long serverId : serverIds.getIds()) {
            wrapper.eq(Gpu::getServerId,serverId);
            List<Gpu> list = list(wrapper);
            gpus.addAll(list);
            wrapper.clear();
        }
        List<GpuView> gpuViews = BeanCopyUtils.copyBeanList(gpus, GpuView.class);
        for (GpuView gpuView : gpuViews) {
            Server server = serverService.getById(gpuView.getServerId());
            gpuView.setServerName(server.getName());
            gpuView.setServerVolume(server.getVolume());
        }
        return ResponseResult.okResult(gpuViews);
    }



}
