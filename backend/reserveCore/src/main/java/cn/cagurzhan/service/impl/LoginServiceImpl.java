package cn.cagurzhan.service.impl;

import cn.cagurzhan.dto.LoginDTO;
import cn.cagurzhan.entity.LoginUser;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.service.LoginService;
import cn.cagurzhan.utils.BeanCopyUtils;
import cn.cagurzhan.utils.JwtUtil;
import cn.cagurzhan.utils.RedisCache;
import cn.cagurzhan.view.LoginView;
import cn.cagurzhan.view.UserView;
import cn.cagurzhan.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Cagur
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {

    /** 认证Manager */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /** 登录业务 */
    @Override
    public ResponseResult<LoginView> login(LoginDTO loginDTO) {

        // 实例化具体认证管理，调用认证方法
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // 判断是否认证通过
        if(authenticate==null){
            throw new SystemException(AppHttpCodeEnum.AUTHENTICATE_ERROR);
        }

        // 获取user的id，生成token。
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 将Auth存入Redis
        redisCache.setCacheObject("login:"+userId,loginUser);

        // token 和 Auth封装返回
        LoginView loginView = new LoginView();
        loginView.setToken(jwt);
        UserView userView = BeanCopyUtils.copyBean(loginUser.getUser(), UserView.class);
        loginView.setUserInfo(userView);

        return ResponseResult.okResult(loginView);
    }

    @Override
    public ResponseResult logout() {
        // 解析token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        // 删除redis中的用户信息
        redisCache.deleteObject("login:"+id);
        return ResponseResult.okResult();

    }


}
