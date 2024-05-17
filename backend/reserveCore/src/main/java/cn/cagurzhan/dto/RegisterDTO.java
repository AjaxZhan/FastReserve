package cn.cagurzhan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户注册信息实体")
public class RegisterDTO {
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "密码")
    private String password;
    @ApiModelProperty(notes = "姓名")
    private String nickname;
    @ApiModelProperty(notes = "邮箱")
    private String email;
    @ApiModelProperty(notes = "性别，0为男，1为女")
    private String sex;
}
