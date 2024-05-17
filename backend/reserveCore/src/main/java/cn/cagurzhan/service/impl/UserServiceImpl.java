package cn.cagurzhan.service.impl;

import cn.cagurzhan.dto.RegisterDTO;
import cn.cagurzhan.dto.UserUpdateDTO;
import cn.cagurzhan.entity.Global;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.User;
import cn.cagurzhan.entity.UserRole;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.mapper.UserMapper;
import cn.cagurzhan.mapper.UserRoleMapper;
import cn.cagurzhan.service.GlobalService;
import cn.cagurzhan.service.UserService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.utils.RedisCache;
import cn.cagurzhan.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /** 密码加密 */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 操作Redis缓存 */
    @Autowired
    private RedisCache redisCache;

    /** 全局信息 */
    @Autowired
    private GlobalService globalService;

    /** 用户和用户权限关联表 */
    @Autowired
    private UserRoleMapper userRoleMapper;

    /** 用户注册 */
    @Override
    public ResponseResult register(RegisterDTO registerDTO) {
        // 检查空值
        if(registerDTO.getUsername().isEmpty() || registerDTO.getPassword().isEmpty() || registerDTO.getNickname().isEmpty() || registerDTO.getSex().isEmpty() || registerDTO.getEmail().isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.EMPTY_ERROR);
        }
        // 检验用户是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,registerDTO.getUsername());
        if (getOne(wrapper) != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_EXIST_ERROR);
        }
        // 设置用户信息
        User user = BeanCopyUtils.copyBean(registerDTO, User.class);
        user.setType("0");
        user.setDelFlag(0);
        String enpassword = passwordEncoder.encode(registerDTO.getPassword());
        user.setPassword(enpassword);
        // 判断是否开启注册需验证功能
        Global global = globalService.getById(1);
        if(global.getRegisterFlag().equals("1")){
            // 开启
            user.setStatus("1");
        }else{
            user.setStatus("0");
        }
        // 更新数据库
        boolean save = save(user);
        if(!save){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        // 设置权限
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getUsername,registerDTO.getUsername());
        User user1 = getOne(wrapper1);
        int insert = userRoleMapper.insert(new UserRole(user1.getId(), 2L));
        return ResponseResult.okResult();
    }

    /** 更新用户 */
    @Override
    public ResponseResult updateUser(UserUpdateDTO userUpdateDTO) {
        // 查询是否有此用户
        User user = getById(userUpdateDTO.getId());
        if(user == null){
            return ResponseResult.errorResult(300,"修改失败，查无此用户");
        }
        // 是否为用户自己
        if(!user.getId().equals(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.ILLEGAL_ERROR);
        }
        User newUser = BeanCopyUtils.copyBean(userUpdateDTO, User.class);
        if(userUpdateDTO.getPassword()==null || user.getPassword().isEmpty()){
            newUser.setPassword(user.getPassword());
        }else{
            System.out.println("password" + userUpdateDTO.getPassword() + " type");
            newUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        boolean update = updateById(newUser);
        if(!update) {
            return ResponseResult.errorResult(400, "更新用户时出现错误");
        }
        redisCache.deleteObject("login:"+newUser.getId());
        return ResponseResult.okResult();
    }
}
