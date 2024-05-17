package cn.cagurzhan.service;

import cn.cagurzhan.entity.Global;
import cn.cagurzhan.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (Global)表服务接口
 *
 * @author makejava
 * @since 2023-09-22 20:26:26
 */
public interface GlobalService extends IService<Global> {

    ResponseResult updateGlobal(Global global);
}
