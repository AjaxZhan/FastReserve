package cn.cagurzhan.handler;

import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Cagur
 * @version 1.0
 * 权限不足的异常
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        exception.printStackTrace();
        ResponseResult res = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR);
        WebUtils.renderString(response, JSON.toJSONString(res));
    }
}
