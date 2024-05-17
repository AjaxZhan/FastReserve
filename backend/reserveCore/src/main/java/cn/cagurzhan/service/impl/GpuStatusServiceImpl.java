package cn.cagurzhan.service.impl;

import cn.cagurzhan.entity.GpuStatusParent;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Cagur
 * @version 1.0
 * 从Redis获取GPU实时情况
 */
@Service
public class GpuStatusServiceImpl {

    @Autowired
    private RedisCache redisCache;

    public ResponseResult getFromRedis(Integer id){
        GpuStatusParent cacheObject = redisCache.getCacheObject("server:"+id.toString());
        return ResponseResult.okResult(cacheObject);
    }

}
