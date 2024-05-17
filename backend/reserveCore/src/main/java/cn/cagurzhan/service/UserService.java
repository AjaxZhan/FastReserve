package cn.cagurzhan.service;

import cn.cagurzhan.dto.RegisterDTO;
import cn.cagurzhan.dto.UserUpdateDTO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Cagur
 * @version 1.0
 */
public interface UserService extends IService<User> {
    ResponseResult register(RegisterDTO registerDTO);

    ResponseResult updateUser(UserUpdateDTO userUpdateDTO);
}
