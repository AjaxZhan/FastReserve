package cn.cagurzhan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 * 用于前端有部分需要发送ID数组的需求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "id数组")
public class Ids {
    @ApiModelProperty(notes = "ID数组")
    private List<Long> ids;
}
