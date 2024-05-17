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
@ApiModel(description = "更新用户信息实体")
public class AdminUserUpdateDTO {
    @ApiModelProperty(notes = "用户ID")
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    @ApiModelProperty(notes = "用户状态，0为正常，1为冻结")
    private String status;
    @ApiModelProperty(notes = "性别，0为男，1为女")
    private String sex;
    @ApiModelProperty(notes = "类型，0为用户，1为管理员")
    private String type;
}
