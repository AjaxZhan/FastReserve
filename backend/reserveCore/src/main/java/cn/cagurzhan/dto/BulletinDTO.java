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
@ApiModel(description = "公告实体")
public class BulletinDTO {
    @ApiModelProperty(notes = "公告ID")
    private Long id;
    @ApiModelProperty(notes = "公告标题")
    private String title;
    @ApiModelProperty(notes = "公告内容")
    private String content;
    @ApiModelProperty(notes = "创建者ID")
    private Long createBy;
    @ApiModelProperty(notes = "创建时间")
    private Date createTime;
    @ApiModelProperty(notes = "更新时间")
    private Date updateTime;
}
