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
@ApiModel(description = "GPU实体")
public class GpuDTO {
    @ApiModelProperty(notes = "GPU的ID")
    private Long id;
    @ApiModelProperty(notes = "服务器的ID")
    private Long serverId;
    @ApiModelProperty(notes = "同一台服务器中，GPU的编号")
    private Integer number;
    @ApiModelProperty(notes = "GPU的型号")
    private String model;
    @ApiModelProperty(notes = "GPU的状态，0正常，1停用")
    private String status;
}
