package cn.cagurzhan.service;

import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.dto.ReserveDTO;
import cn.cagurzhan.dto.ReserveDTOBatch;
import cn.cagurzhan.entity.Reserve;
import cn.cagurzhan.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Cagur
 * @version 1.0
 */
public interface ReserveService extends IService<Reserve> {
    ResponseResult getByGPU(Integer gpuId);

    ResponseResult add(ReserveDTO reserveDTO);

    ResponseResult updateReserve(ReserveDTO reserveDTO);

    ResponseResult deleteReserve(ReserveDTO reserveDTO);

    ResponseResult getMyCount(Long userId);

    ResponseResult getMy(Long userId);

    ResponseResult getByGPUs(Ids gpuIds);

    ResponseResult addOrder(ReserveDTO reserveDTO);

    ResponseResult verify(ReserveDTO reserveDTO);

    ResponseResult listOrder(Integer pageNum, Integer pageSize);

    ResponseResult listMyOrder(Long userId , Integer pageNum, Integer pageSize);

    ResponseResult fail(ReserveDTO reserveDTO);

    ResponseResult listAll();

    ResponseResult addBatch(ReserveDTOBatch reserveDTOBatch);

    ResponseResult addOrderBatch(ReserveDTOBatch reserveDTOBatch);
}
