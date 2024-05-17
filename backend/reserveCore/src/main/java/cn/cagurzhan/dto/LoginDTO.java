package cn.cagurzhan.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户登录实体")
public class LoginDTO {
    private String username;
    private String password;
}
