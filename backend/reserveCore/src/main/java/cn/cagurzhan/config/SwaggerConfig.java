package cn.cagurzhan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Cagur
 * @version 1.0
 * Swagger 接口文档配置类
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描的包
                .apis(RequestHandlerSelectors.basePackage("cn.cagurzhan.controller"))
                .build();
    }

    private ApiInfo apiInfo(){
        // 这里填写联系方式，即 组织名、URL地址、邮箱
        Contact contact = new Contact("FastReserve", "", "");
        return new ApiInfoBuilder()
                .title("GPU预约系统接口文档")
                .description("GPU预约系统接口文档，此文档用于前后端开发和维护使用")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
