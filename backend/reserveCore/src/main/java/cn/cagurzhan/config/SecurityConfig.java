package cn.cagurzhan.config;

import cn.cagurzhan.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Cagur
 * @version 1.0
 * Spring Security 配置类，可配置加密
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true ) // 开启权限管理
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 注入 AuthenticationManager
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    /**
     * 注入BCryptPasswordEncoder，将密码进行加密存储
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Jwt认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * 处理身份验证的的请求
     */
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    /**
     * 处理权限验证的请求
     */
    @Autowired
    private AccessDeniedHandler  accessDeniedHandler;

    /**
     * 配置跨域访问、登录接口
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 放行登录接口
                .antMatchers("/login").anonymous()
                // 放行注册接口
                .antMatchers("/register").anonymous()
                .antMatchers("/logout").authenticated()
                // 放行获取网站基本信息的接口
                .antMatchers("/admin/getGlobal").anonymous()
                // 放行Swagger-UI
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/v2/api-docs").anonymous()
                .antMatchers("/swagger-resources").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/configuration/ui").anonymous()
                .antMatchers("/configuration/security").anonymous()
                .antMatchers("/webjars/**").anonymous()
                // 其它接口都需要身份验证
                .anyRequest().authenticated();
        // 自定义异常处理
        http.exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint);
        // 关闭Spring Security默认的logout功能
        http.logout().disable();
        // 增加JWT认证过滤器，拦截请求
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 允许跨域请求
        http.cors();
    }




}
