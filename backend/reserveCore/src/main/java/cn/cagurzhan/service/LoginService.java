package cn.cagurzhan.service;

import cn.cagurzhan.view.LoginView;
import cn.cagurzhan.dto.LoginDTO;
import cn.cagurzhan.entity.ResponseResult;

/**
 * @author Cagur
 * @version 1.0
 */
public interface LoginService {
    ResponseResult<LoginView> login(LoginDTO loginDTO);

    ResponseResult logout();
}
