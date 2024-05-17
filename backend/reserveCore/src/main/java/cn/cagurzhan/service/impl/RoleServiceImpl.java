package cn.cagurzhan.service.impl;

import cn.cagurzhan.service.UserService;
import cn.cagurzhan.entity.Role;
import cn.cagurzhan.mapper.RoleMapper;
import cn.cagurzhan.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (Role)表服务实现类
 *
 * @author makejava
 * @since 2023-09-24 11:51:57
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserService userService;
    /**
     * 通过用户ID查找角色
     * */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // 判断是否为管理员
        if (userService.getById(id).getType().equals("1")) {
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        // 查询用户所具有的角色
        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}

