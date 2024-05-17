package cn.cagurzhan.service.impl;

import cn.cagurzhan.entity.LoginUser;
import cn.cagurzhan.entity.User;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.mapper.MenuMapper;
import cn.cagurzhan.mapper.UserMapper;
import cn.cagurzhan.exception.SystemException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);
        // 是否查询到
        if(user == null){
            throw new SystemException(AppHttpCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 是否冻结
        if(user.getStatus().equals("1")){
            throw new SystemException(AppHttpCodeEnum.USER_FREEZE);
        }
        // 返回用户信息
        // todo 查询权限信息
        List<String> perms = menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user,perms);
    }

}
