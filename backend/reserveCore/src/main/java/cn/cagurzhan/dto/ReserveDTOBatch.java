package cn.cagurzhan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "批量预约的实体")
public class ReserveDTOBatch {
    @ApiModelProperty(notes = "预约的ID")
    private Long id;
    @ApiModelProperty(notes = "预约标题")
    private String title;
    @ApiModelProperty(notes = "被预约的GPU的ID数组")
    private List<Long> gpuIds;
    @ApiModelProperty(notes = "开始时间")
    private Date start;
    @ApiModelProperty(notes = "结束时间")
    private Date end;
    @ApiModelProperty(notes = "备注")
    private String comment;
    @ApiModelProperty(notes = "预约者")
    private Long createBy;
}
