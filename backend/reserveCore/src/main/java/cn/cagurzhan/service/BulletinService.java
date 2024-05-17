package cn.cagurzhan.service;

import cn.cagurzhan.entity.Bulletin;
import cn.cagurzhan.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (Bulletin)表服务接口
 *
 * @author makejava
 * @since 2023-10-01 16:46:25
 */
public interface BulletinService extends IService<Bulletin> {

    ResponseResult getPage(Integer pageNum, Integer pageSize);
}
