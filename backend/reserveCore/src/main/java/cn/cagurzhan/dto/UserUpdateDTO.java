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
@ApiModel(description = "用户更新资料实体")
public class UserUpdateDTO {
    private Long id;
    private String password;
    private String nickname;
    private String email;
    @ApiModelProperty(notes = "性别，0为男，1为女")
    private String sex;
    @ApiModelProperty(notes = "状态，0为正常，1为冻结")
    private String type;
}
