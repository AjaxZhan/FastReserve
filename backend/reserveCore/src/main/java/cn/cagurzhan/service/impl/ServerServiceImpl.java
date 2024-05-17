package cn.cagurzhan.service.impl;

import cn.cagurzhan.dto.ServerDAO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.Server;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.mapper.ServerMapper;
import cn.cagurzhan.service.ServerService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.view.PageView;
import cn.cagurzhan.view.ServerView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements ServerService {
    @Override
    public ResponseResult getAll(Integer pageNum, Integer pageSize) {

        Page<Server> serverPage = new Page<>(pageNum,pageSize);
        page(serverPage);
        List<ServerView> serverViews = BeanCopyUtils.copyBeanList(serverPage.getRecords(), ServerView.class);
        PageView pageView = new PageView(serverViews, serverPage.getTotal());
        return ResponseResult.okResult(pageView);
    }

    @Override
    public ResponseResult add(ServerDAO serverDAO) {
        Server server = BeanCopyUtils.copyBean(serverDAO, Server.class);
        server.setDelFlag(0);
        boolean save = save(server);
        if (!save){
            return ResponseResult.errorResult(400,"网络错误，添加失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(ServerView serverView) {
        boolean b = removeById(serverView.getId());
        if(!b){
            return ResponseResult.errorResult(400,"网络错误，删除失败");
        }
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult updateServer(ServerView serverView) {

        Server byId = getById(serverView.getId());
        if(byId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK.getCode(),"错误，查无此服务器");
        }
        Server server = BeanCopyUtils.copyBean(serverView, Server.class);
        updateById(server);
        return ResponseResult.okResult();
    }
}
