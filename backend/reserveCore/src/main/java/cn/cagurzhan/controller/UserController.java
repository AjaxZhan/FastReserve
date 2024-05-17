package cn.cagurzhan.controller;

import cn.cagurzhan.dto.RegisterDTO;
import cn.cagurzhan.dto.UserUpdateDTO;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cagur
 * @version 1.0
 */
@RestController
@Api(tags = "用户相关接口")
public class UserController {
    /**  用户服务 */
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "用户注册")
    public ResponseResult register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }


    @PostMapping("/user/update")
    @ApiOperation(value = "用户更新个人资料",notes = "用户修改个人资料")
    public ResponseResult updateUser(@RequestBody UserUpdateDTO userUpdateDTO){
        return userService.updateUser(userUpdateDTO);
    }

}
