package cn.cagurzhan.service.impl;

import cn.cagurzhan.service.UserService;
import cn.cagurzhan.dto.AdminUserUpdateDTO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.User;
import cn.cagurzhan.entity.UserRole;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.exception.SystemException;
import cn.cagurzhan.mapper.UserRoleMapper;
import cn.cagurzhan.service.AdminService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.utils.RedisCache;
import cn.cagurzhan.view.AdminUserView;
import cn.cagurzhan.view.PageView;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 分页获取用户信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseResult getUser(Integer pageNum, Integer pageSize) {
        // 分页查询用户信息
        Page<User> userPage = new Page<>(pageNum,pageSize);
        userService.page(userPage);
        // 封装为VO
        List<AdminUserView> adminUserViews = BeanCopyUtils.copyBeanList(userPage.getRecords(), AdminUserView.class);
        PageView pageView = new PageView(adminUserViews,userPage.getTotal());
        return ResponseResult.okResult(pageView);
    }

    /**
     * 更新用户信息
     * @param adminUserUpdateDTO 用户信息
     */
    @Override
    public ResponseResult updateUser(AdminUserUpdateDTO adminUserUpdateDTO) {
        // 查找用户
        User user = userService.getById(adminUserUpdateDTO.getId());
        if (user == null){
            throw new SystemException(300,"修改失败，查无此用户");
        }
        User newUser = BeanCopyUtils.copyBean(adminUserUpdateDTO, User.class);
        // 设置密码
        if(adminUserUpdateDTO.getPassword().isEmpty()){
            newUser.setPassword(user.getPassword());
        }else{
            newUser.setPassword(passwordEncoder.encode(adminUserUpdateDTO.getPassword()));
        }
        // 更新
        boolean b = userService.updateById(newUser);
        if (!b){
            return ResponseResult.errorResult(400,"网络异常，更新失败");
        }
        return ResponseResult.okResult();
    }

    /**
     * 冻结用户 / 解冻用户
     * @param adminUserUpdateDTO 用户信息
     */
    @Override
    public ResponseResult toggleStatus(AdminUserUpdateDTO adminUserUpdateDTO) {
        // 查找用户
        User user = userService.getById(adminUserUpdateDTO.getId());
        if (user == null){
            throw new SystemException(300,"修改失败，查无此用户");
        }
        if(user.getStatus().equals("1")){
            user.setStatus("0");
        }else{
            user.setStatus("1");
        }
        boolean b = userService.updateById(user);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.NETWORK);
        }
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     * @param adminUserUpdateDTO 用户信息
    */
    @Override
    public ResponseResult deleteUser(AdminUserUpdateDTO adminUserUpdateDTO) {
        // 查找用户
        User user = userService.getById(adminUserUpdateDTO.getId());
        if (user == null){
            throw new SystemException(300,"修改失败，查无此用户");
        }
        if(user.getId() == 1L){
            throw new SystemException(300,"非法操作，不能修改超级管理员");
        }
        userService.removeById(user.getId());
        return ResponseResult.okResult();
    }

    /**
     * 添加用户
     * @param adminUserUpdateDTO 用户信息
     */
    @Override
    public ResponseResult addUser(AdminUserUpdateDTO adminUserUpdateDTO) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,adminUserUpdateDTO.getUsername());
        User user = userService.getOne(wrapper);
        if (user != null) {
            return ResponseResult.errorResult(500,"添加失败，此用户名已经存在");
        }
        user = BeanCopyUtils.copyBean(adminUserUpdateDTO,User.class);
        user.setDelFlag(0);
        user.setPassword(passwordEncoder.encode(adminUserUpdateDTO.getPassword()));
        boolean save = userService.save(user);
        if(!save){
            return ResponseResult.errorResult(400,"添加失败");
        }
        // 添加用户权限
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getUsername,adminUserUpdateDTO.getUsername());
        User user1 = userService.getOne(wrapper1);
        userRoleMapper.insert(new UserRole(user1.getId(), 2L));

        return ResponseResult.okResult();
    }

}