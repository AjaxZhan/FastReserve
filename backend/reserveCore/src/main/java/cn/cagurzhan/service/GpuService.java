package cn.cagurzhan.service;

import cn.cagurzhan.dto.GpuDTO;
import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.entity.Gpu;
import cn.cagurzhan.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Cagur
 * @version 1.0
 */
public interface GpuService extends IService<Gpu> {
    ResponseResult getGPU();

    ResponseResult addGPU(GpuDTO gpuDTO);

    ResponseResult deleteGPU(GpuDTO gpuDTO);

    ResponseResult getGPUPage(Integer pageNum, Integer pageSize);

    ResponseResult updateGPU(GpuDTO gpuDTO);

    ResponseResult getGPUByServerId(Long serverId);

    ResponseResult getGPUByServerIds(Ids serverIds);
}
