package cn.cagurzhan.handler;

import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Cagur
 * @version 1.0
 * 认证失败的异常
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 全局异常处理
        e.printStackTrace();
        ResponseResult res = null;
        if(e instanceof BadCredentialsException){
            res = ResponseResult.errorResult(500,e.getMessage());
        }else if(e instanceof InsufficientAuthenticationException){
          res = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR.getCode(),"用户名或密码错误");
        } else{
            res = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR);
        }
        WebUtils.renderString(response,JSON.toJSONString(res));
    }
}
