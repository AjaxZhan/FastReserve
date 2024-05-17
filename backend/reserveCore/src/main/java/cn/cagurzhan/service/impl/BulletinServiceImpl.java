package cn.cagurzhan.service.impl;

import cn.cagurzhan.dto.BulletinDTO;
import cn.cagurzhan.entity.Bulletin;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.mapper.BulletinMapper;
import cn.cagurzhan.service.BulletinService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.view.PageView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Bulletin)表服务实现类
 *
 * @author makejava
 * @since 2023-10-01 16:46:25
 */
@Service("bulletinService")
public class BulletinServiceImpl extends ServiceImpl<BulletinMapper, Bulletin> implements BulletinService {

    /**
     *  分页查询公告信息
     */
    @Override
    public ResponseResult getPage(Integer pageNum, Integer pageSize) {
        Page<Bulletin> bulletinPage = new Page<>(pageNum, pageSize);
        page(bulletinPage);
        List<BulletinDTO> bulletinDTOS = BeanCopyUtils.copyBeanList(bulletinPage.getRecords(), BulletinDTO.class);
        PageView pageView = new PageView(bulletinDTOS, bulletinPage.getTotal());
        return ResponseResult.okResult(pageView);
    }
}

