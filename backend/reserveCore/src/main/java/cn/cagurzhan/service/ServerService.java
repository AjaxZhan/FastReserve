package cn.cagurzhan.service;

import cn.cagurzhan.dto.ServerDAO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.Server;
import cn.cagurzhan.view.ServerView;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Cagur
 * @version 1.0
 */
public interface ServerService extends IService<Server> {
    ResponseResult getAll(Integer pageNum, Integer pageSize);

    ResponseResult add(ServerDAO serverDAO);

    ResponseResult delete(ServerView serverView);

    ResponseResult updateServer(ServerView serverView);
}
