package cn.cagurzhan.service;

import cn.cagurzhan.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * (Role)表服务接口
 *
 * @author makejava
 * @since 2023-09-24 11:51:57
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
