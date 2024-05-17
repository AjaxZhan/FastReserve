package cn.cagurzhan.service.impl;

import cn.cagurzhan.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Service("ps")
public class PermissionService {
    /**
     *
     * @param permission 要判断的权限
     * @return 是否有权限
     */
    public boolean hasPermission(String permission){
        // 判断当前用户是否具有permission
        if(SecurityUtils.getLoginUser().getUser().getType().equals("1")){
            // 管理员
            return true;
        }
        // 查询权限信息
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
