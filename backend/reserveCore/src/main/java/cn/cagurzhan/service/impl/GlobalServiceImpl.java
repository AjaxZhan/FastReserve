package cn.cagurzhan.service.impl;

import cn.cagurzhan.entity.Global;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.mapper.GlobalMapper;
import cn.cagurzhan.service.GlobalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Global)表服务实现类
 *
 * @author makejava
 * @since 2023-09-22 20:26:26
 */
@Service("globalService")
public class GlobalServiceImpl extends ServiceImpl<GlobalMapper, Global> implements GlobalService {

    @Autowired
    private GlobalMapper globalMapper;

    /**
     * 网站信息
     * @param global 网站信息
     */
    @Override
    public ResponseResult updateGlobal(Global global) {
        globalMapper.update(global,null);
        return ResponseResult.okResult();
    }
}

