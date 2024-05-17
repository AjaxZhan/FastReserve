package cn.cagurzhan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Cagur
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("cn.cagurzhan.mapper") // Mybatis Mapper 文件扫描
@EnableScheduling // 开启定时任务
// @EnableSwagger2 // 开启Swagger UI 接口文档
public class ReserveSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReserveSystemApplication.class,args);
    }
}
