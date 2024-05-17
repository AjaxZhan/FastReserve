package cn.cagurzhan.utils;

import cn.cagurzhan.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断用户是否为管理员
     * */
    public static Boolean isAdmin(){
//        Long id = getLoginUser().getUser().getId();
//        return id != null && id.equals(1L);
        return false;
    }

    /**
     * 获取用户ID
     * */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}