package cn.cagurzhan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "服务器实体")
public class ServerDAO {
    @ApiModelProperty(notes = "服务器容量，指服务器为几卡")
    private Integer volume;
    @ApiModelProperty(notes = "服务器名字")
    private String name;
    @ApiModelProperty(notes = "服务器状态，0为正常，1为停用")
    private String status;
    @ApiModelProperty(notes = "服务器内网IP地址，一般是192开头")
    private String ip;
}
