package cn.cagurzhan.service.impl;

import cn.cagurzhan.dto.Ids;
import cn.cagurzhan.dto.ReserveDTO;
import cn.cagurzhan.dto.ReserveDTOBatch;
import cn.cagurzhan.entity.*;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.mapper.ReserveMapper;
import cn.cagurzhan.service.*;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.utils.SecurityUtils;
import cn.cagurzhan.view.GpuView;
import cn.cagurzhan.view.PageView;
import cn.cagurzhan.view.ReserveView;
import cn.cagurzhan.view.ReserveViewMy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve> implements ReserveService {

    @Autowired
    private UserService userService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private GpuService gpuService;
    @Autowired
    private ReserveMapper reserveMapper;
    @Autowired
    private GlobalService globalService;

    /**
     * 通过多个GPU的id，查询预约信息
     * @param gpuIds gpuId的集合
     */
    @Override
    public ResponseResult getByGPUs(Ids gpuIds) {
        List<Long> ids = gpuIds.getIds();
        // 查询GPU
        List<Gpu> gpus = gpuService.listByIds(ids);
        System.out.println("gpus = " + gpus);
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        List<Reserve> reserves = new ArrayList<>();
        for (Gpu gpu : gpus) {
            wrapper.eq(Reserve::getGpuId,gpu.getId()).and(wq2->wq2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
            // 预约信息必须为正常或通过审核
            List<Reserve> reserves1 = reserveMapper.selectList(wrapper);
            if(!reserves1.isEmpty()){
                reserves.addAll(reserves1);
            }
            wrapper.clear();
        }
        List<ReserveView> reserveViews = BeanCopyUtils.copyBeanList(reserves, ReserveView.class);
        for (ReserveView reserveView : reserveViews) {
            String who = userService.getById(reserveView.getCreateBy()).getNickname();
            reserveView.setWho(who);
        }
        return ResponseResult.okResult(reserveViews);
    }

    /**
     * 批量添加工单
     * @param reserveDTOBatch 含有多个GPU 的工单信息
     */
    @Override
    public ResponseResult addOrderBatch(ReserveDTOBatch reserveDTOBatch) {
        // 考虑到用户群体为小范围，此处省略判断GPU和服务器是否可用，因为前端做了限制

        // 所有GPU的id
        List<Long> gpuIds = reserveDTOBatch.getGpuIds();
        if(gpuIds.isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.ILLEGAL_ERROR);
        }
        // 对于所有GPU，判断预约时间是否冲突
        Date start = reserveDTOBatch.getStart();
        Date end = reserveDTOBatch.getEnd();
        for (Long gpuId : gpuIds) {
            // 查询该GPU的所有预约
            LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Reserve::getGpuId,gpuId).and(wq2->wq2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
            List<Reserve> list = list(wrapper);
            int cnt = 0;
            // 对每条预约做判断
            for (Reserve reserve : list) {
                if((reserve.getStart().getTime() - start.getTime()>=0 && reserve.getStart().getTime()-end.getTime()<=0)
                        || (reserve.getStart().getTime() - start.getTime() <=0 && reserve.getEnd().getTime() - start.getTime()>0)
                ){
                    // 冲突
                    cnt ++;
                }
            }
            if(cnt > 0){
                return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CONFLICT);
            }
        }
        // 批量添加预约
        for (Long gpuId : gpuIds) {
            Reserve reserve = BeanCopyUtils.copyBean(reserveDTOBatch, Reserve.class);
            reserve.setGpuId(gpuId);
            reserve.setDelFlag(0);
            reserve.setStatus(1);
            boolean save = save(reserve);
            if(!save){
                return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
            }
        }
        return ResponseResult.okResult();
    }

    /**
     * 提交工单
     * @param reserveDTO 工单信息
     */
    @Override
    public ResponseResult addOrder(ReserveDTO reserveDTO) {
        // 判断服务器和GPU是否可用
        String gpuStatus = gpuService.getById(reserveDTO.getGpuId()).getStatus();
        if(gpuStatus.equals("1")){
            return ResponseResult.errorResult(AppHttpCodeEnum.GPU_STATUS_ERROR);
        }
        // 判断是否预约时间冲突
        Date start = reserveDTO.getStart();
        Date end = reserveDTO.getEnd();
        // 查询该GPU的所有预约
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getGpuId,reserveDTO.getGpuId()).and(wq2->wq2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
        List<Reserve> list = list(wrapper);
        int cnt = 0;
        // 对每条预约做判断
        for (Reserve reserve : list) {
            if((reserve.getStart().getTime() - start.getTime()>=0 && reserve.getStart().getTime()-end.getTime()<=0)
                    || (reserve.getStart().getTime() - start.getTime() <=0 && reserve.getEnd().getTime() - start.getTime()>0)
            ){
                // 冲突
                cnt ++;
            }
        }
        if(cnt > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CONFLICT);
        }

        Reserve reserve = BeanCopyUtils.copyBean(reserveDTO, Reserve.class);
        reserve.setDelFlag(0);
        reserve.setStatus(1);
        boolean save = save(reserve);
        if(!save){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        return ResponseResult.okResult();

    }

    /**
     * 审核工单
     * @param reserveDTO 预约信息
     */
    @Override
    public ResponseResult verify(ReserveDTO reserveDTO) {
        Reserve byId = getById(reserveDTO.getId());
        if(byId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"该工单不存在");
        }
        if(byId.getStatus().equals(1) || byId.getStatus().equals(3)){
            byId.setStatus(2);
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"该预约为正常或已审核");
        }
        boolean b = updateById(byId);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        return ResponseResult.okResult();
    }

    /**
     * 分页获取工单信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseResult listOrder(Integer pageNum, Integer pageSize) {

        Page<Reserve> reservePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getStatus,1).or().eq(Reserve::getStatus,2).or().eq(Reserve::getStatus,3);
        page(reservePage,wrapper);
        List<ReserveView> reserveViews = BeanCopyUtils.copyBeanList(reservePage.getRecords(), ReserveView.class);
        for (ReserveView reserveView : reserveViews) {
            String who = userService.getById(reserveView.getCreateBy()).getNickname();
            reserveView.setWho(who);
        }
        PageView pageView = new PageView(reserveViews, reservePage.getTotal());
        return ResponseResult.okResult(pageView);
    }

    /**
     * 分页获取我的工单
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseResult listMyOrder(Long userId, Integer pageNum, Integer pageSize) {
        Page<Reserve> reservePage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getCreateBy,userId).and(wq2->wq2.eq(Reserve::getStatus,1).or().eq(Reserve::getStatus,2).or().eq(Reserve::getStatus,3));
        page(reservePage,wrapper);
        List<ReserveView> reserveViews = BeanCopyUtils.copyBeanList(reservePage.getRecords(), ReserveView.class);
        for (ReserveView reserveView : reserveViews) {
            String who = userService.getById(reserveView.getCreateBy()).getNickname();
            reserveView.setWho(who);
        }
        PageView pageView = new PageView(reserveViews, reservePage.getTotal());
        return ResponseResult.okResult(pageView);
    }

    /**
     * 驳回工单
     * @param reserveDTO 工单信息
     */
    @Override
    public ResponseResult fail(ReserveDTO reserveDTO) {
        Reserve byId = getById(reserveDTO.getId());
        if(byId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"该工单不存在");
        }
        if(byId.getStatus().equals(1) || byId.getStatus().equals(2)){
            byId.setStatus(3);
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"该预约为正常或已审核");
        }
        boolean b = updateById(byId);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        return ResponseResult.okResult();
    }

    /**
     *  获取所有预约信息
     *  用于页面初始化的时候
     */
    @Override
    public ResponseResult listAll() {
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        // 预约必须为正常预约 或 已批准的工单
        wrapper.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2);
        List<Reserve> list = list(wrapper);
        if(list.isEmpty()){
            return ResponseResult.okResult(null);
        }
        List<ReserveView> reserveViews = BeanCopyUtils.copyBeanList(list, ReserveView.class);
        for (ReserveView reserveView : reserveViews) {
            String who = userService.getById(reserveView.getCreateBy()).getNickname();
            reserveView.setWho(who);
        }
        return ResponseResult.okResult(reserveViews);
    }



    @Override
    @Deprecated
    public ResponseResult getByGPU(Integer gpuId) {
        // 查询GPU
        Gpu gpu = gpuService.getById(gpuId);
        // 根据GPU，查询预约
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getGpuId,gpu.getId());
        List<Reserve> reserves = reserveMapper.selectList(wrapper);
        if(reserves.isEmpty()){
            return ResponseResult.okResult(null);
        }
        // 封装VO
        List<ReserveView> reserveViews = BeanCopyUtils.copyBeanList(reserves, ReserveView.class);
        for (ReserveView reserveView : reserveViews) {
            String who = userService.getById(reserveView.getCreateBy()).getNickname();
            reserveView.setWho(who);
        }
        return ResponseResult.okResult(reserveViews);
    }

    /**
     * 批量添加预约信息
     * @param reserveDTOBatch 携带多个GPU
     */
    @Override
    public ResponseResult addBatch(ReserveDTOBatch reserveDTOBatch) {
        // 考虑到用户群体为小范围，此处省略判断GPU和服务器是否可用，因为前端做了限制
        Long cardLimit = globalService.getById(1).getCardLimit();

        List<Long> gpuIds = reserveDTOBatch.getGpuIds();
        if(gpuIds.isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.ILLEGAL_ERROR);
        }

        // 查询已经预约的卡的数量
        LambdaQueryWrapper<Reserve> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Reserve::getCreateBy,reserveDTOBatch.getCreateBy());
        wrapper1.gt(Reserve::getEnd,new Date());
        List<Reserve> ll = list(wrapper1);
        List<Long> allGpuId = ll.stream().map(Reserve::getGpuId).distinct().collect(Collectors.toList());
        allGpuId.addAll(gpuIds);
        long count = allGpuId.stream().distinct().count();
        if(count > cardLimit){
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CARD_OVER);
        }

        // 判断用户是否达到最大预约时长
        Long maxTime = globalService.getById(1).getMaxTime() * 60;
        Long reserveTime =( reserveDTOBatch.getEnd().getTime() - reserveDTOBatch.getStart().getTime()) /(60 *1000);
        reserveTime = reserveTime * gpuIds.size();

        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<Reserve>();
        wrapper.gt(Reserve::getEnd,new Date()).and(wq2->wq2.eq(Reserve::getCreateBy,reserveDTOBatch.getCreateBy()))
                .and(wq3->wq3.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
        List<Reserve> reserves = list(wrapper);
        for (Reserve reserve : reserves) {
            reserveTime  += ( reserve.getEnd().getTime()- reserve.getStart().getTime()) / (60 *1000);
        }
        if(reserveTime >maxTime){
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_MAX_OVER);
        }

        // 对于所有GPU，判断预约时间是否冲突
        Date start = reserveDTOBatch.getStart();
        Date end = reserveDTOBatch.getEnd();
        for (Long gpuId : gpuIds) {
            // 查询该GPU的所有预约
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Reserve::getGpuId,gpuId).and(wq2->wq2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
            List<Reserve> list = list(wrapper);
            int cnt = 0;
            // 对每条预约做判断
            for (Reserve reserve : list) {
                if((reserve.getStart().getTime() - start.getTime()>=0 && reserve.getStart().getTime()-end.getTime()<=0)
                        || (reserve.getStart().getTime() - start.getTime() <=0 && reserve.getEnd().getTime() - start.getTime()>0)
                ){
                    // 冲突
                    cnt ++;
                }
            }
            if(cnt > 0){
                return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CONFLICT);
            }
        }



        // 批量添加预约
        for (Long gpuId : gpuIds) {
            Reserve reserve = BeanCopyUtils.copyBean(reserveDTOBatch, Reserve.class);
            reserve.setGpuId(gpuId);
            reserve.setDelFlag(0);
            reserve.setStatus(0);
            boolean save = save(reserve);
            if(!save){
                return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
            }
        }
        return ResponseResult.okResult();

    }



    /**
     * 添加预约
     * @param reserveDTO 预约信息
     */
    @Override
    public ResponseResult add(ReserveDTO reserveDTO) {

        // 判断卡数量是否超过限制
        Long cardLimit = globalService.getById(1).getCardLimit();
        // 查询已经预约的卡的数量
        LambdaQueryWrapper<Reserve> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Reserve::getCreateBy,reserveDTO.getCreateBy());
        wrapper1.gt(Reserve::getEnd,new Date());
        List<Reserve> ll = list(wrapper1);
        List<Long> allGpuId = ll.stream().map(Reserve::getGpuId).distinct().collect(Collectors.toList());
        allGpuId.add(reserveDTO.getGpuId());
        long count = allGpuId.stream().distinct().count();
        if(count > cardLimit){
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CARD_OVER);
        }

        // 判断服务器和GPU是否可用
        String gpuStatus = gpuService.getById(reserveDTO.getGpuId()).getStatus();
        if(gpuStatus.equals("1")){
            return ResponseResult.errorResult(AppHttpCodeEnum.GPU_STATUS_ERROR);
        }
        // 判断是否预约时间冲突
        Date start = reserveDTO.getStart();
        Date end = reserveDTO.getEnd();
        // 查询该GPU的所有预约
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getGpuId,reserveDTO.getGpuId()).and(wq2->wq2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
        List<Reserve> list = list(wrapper);
        int cnt = 0;
        // 对每条预约做判断
        for (Reserve reserve : list) {
            if((reserve.getStart().getTime() - start.getTime()>=0 && reserve.getStart().getTime()-end.getTime()<=0)
            || (reserve.getStart().getTime() - start.getTime() <=0 && reserve.getEnd().getTime() - start.getTime()>0)
            ){
                // 冲突
                cnt ++;
            }
        }
        if(cnt > 0){
            System.out.println("cnt=" + cnt);
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_CONFLICT);
        }
        // 判断用户是否达到最大预约时长
        Long maxTime = globalService.getById(1).getMaxTime() * 60;
        Long reserveTime =( reserveDTO.getEnd().getTime() - reserveDTO.getStart().getTime()) /(60 *1000);
        wrapper = new LambdaQueryWrapper<Reserve>();
        wrapper.gt(Reserve::getEnd,new Date()).and(wq2->wq2.eq(Reserve::getCreateBy,reserveDTO.getCreateBy()))
                        .and(wq3->wq3.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
        List<Reserve> reserves = list(wrapper);
        for (Reserve reserve : reserves) {
            reserveTime  += ( reserve.getEnd().getTime()- reserve.getStart().getTime()) / (60 *1000);
        }
        if(reserveTime >maxTime){
            return ResponseResult.errorResult(AppHttpCodeEnum.RESERVE_MAX_OVER);
        }

        Reserve reserve = BeanCopyUtils.copyBean(reserveDTO, Reserve.class);
        reserve.setDelFlag(0);
        reserve.setStatus(0);
        boolean save = save(reserve);
        if(!save){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        return ResponseResult.okResult();
    }

    /**
     * 更新预约
     * @param reserveDTO 预约信息
     */
    @Override
    public ResponseResult updateReserve(ReserveDTO reserveDTO) {
        // 判断服务器和GPU是否可用
        String gpuStatus = gpuService.getById(reserveDTO.getGpuId()).getStatus();
        if(gpuStatus.equals("1")){
            return ResponseResult.errorResult(AppHttpCodeEnum.GPU_STATUS_ERROR);
        }
        if(getById(reserveDTO.getId()) == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"该预约不存在");
        }
        // 不是用户本人，并且不是管理员，拒绝预约
        if(!reserveDTO.getCreateBy().equals(SecurityUtils.getLoginUser().getUser().getId())){
            if(!userService.getById(reserveDTO.getCreateBy()).getType().equals("1")){
                return ResponseResult.errorResult(AppHttpCodeEnum.ILLEGAL_ERROR);
            }
        }

        Reserve reserve = BeanCopyUtils.copyBean(reserveDTO, Reserve.class);
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getId,reserve.getId());
        updateById(reserve);
        return ResponseResult.okResult();
    }

    /**
     * 删除预约
     * @param reserveDTO 预约信息
     */
    @Override
    public ResponseResult deleteReserve(ReserveDTO reserveDTO) {
        if(getById(reserveDTO.getId()) == null){
            return ResponseResult.errorResult(400,"该预约不存在");
        }
        Reserve reserve = BeanCopyUtils.copyBean(reserveDTO, Reserve.class);
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getId,reserve.getId());
        remove(wrapper);
        return ResponseResult.okResult();
    }

    /**
     * 获取我的预约数
     * @param userId 用户ID
     */
    @Override
    @Deprecated
    public ResponseResult getMyCount(Long userId) {
        // 获取我的预约数量
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2);
        List<Reserve> reserveList = list(wrapper);
        long count = reserveList.stream().filter(reserve -> reserve.getCreateBy() == userId).count();
        return ResponseResult.okResult(count);
    }

    /**
     * 获取我的预约
     * @param userId 用户ID
     */
    @Override
    public ResponseResult getMy(Long userId) {

        // 查询此用户
        User user = userService.getById(userId);
        if(user == null){
            return ResponseResult.errorResult(300,"查询失败，查无此用户");
        }
        // 查询此用户的reserve
        LambdaQueryWrapper<Reserve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reserve::getCreateBy,userId).and(q2->q2.eq(Reserve::getStatus,0).or().eq(Reserve::getStatus,2));
        List<Reserve> reserveList = list(wrapper);
        if(reserveList.isEmpty()){
            return ResponseResult.okResult(null);
        }
        List<ReserveViewMy> reserveViewMIES = BeanCopyUtils.copyBeanList(reserveList, ReserveViewMy.class);
        // 每一条reserve，查询GPU信息
        for (ReserveViewMy reserveViewMY : reserveViewMIES) {
            reserveViewMY.setWho(userService.getById(reserveViewMY.getCreateBy()).getNickname());
            Gpu gpu = gpuService.getById(reserveViewMY.getGpuId());
            GpuView gpuView = BeanCopyUtils.copyBean(gpu, GpuView.class);
            Server server = serverService.getById(gpuView.getServerId());
            gpuView.setServerName(server.getName());
            gpuView.setServerVolume(server.getVolume());
            reserveViewMY.setGpu(gpuView);
        }
        return ResponseResult.okResult(reserveViewMIES);
    }



}
