package cn.cagurzhan.filter;

import cn.cagurzhan.entity.LoginUser;
import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.enums.AppHttpCodeEnum;
import cn.cagurzhan.utils.JwtUtil;
import cn.cagurzhan.utils.RedisCache;
import cn.cagurzhan.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Cagur
 * @version 1.0
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    /**
     * 认证过滤
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     * @throws ServletException 异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取Header 中的 token
        String token = request.getHeader("token");
        // 没有token，放行
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return ;
        }
        String userId = "";
        Claims claims = null;
        // 解析 token 获取 userId
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理token超时或非法token
            // 因为Filter不经过控制，这里需要用工具类转换一下格式
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR.getCode(),"非法token，请重新操作");
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        userId = claims.getSubject();
        // token合法，从Redis中读取，验证是否登录
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        if(loginUser==null){
            // 登录状态过期
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_ERROR.getCode(),"登录状态过期，请重新登录");
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        // 用户已登录
        // 将信息存入context
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // 放行
        filterChain.doFilter(request,response);
    }
}
