package cn.cagurzhan.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Cagur
 * @version 1.0
 * 配置REST，用于向FLASK程序发送HTTP请求
 */
@Configuration
public class RestTemplateConfig {
    /**
     * @return 将RestTemplate 注入容器
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
