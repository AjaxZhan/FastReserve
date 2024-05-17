package cn.cagurzhan.entity;


import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Global)表实体类
 *
 * @author makejava
 * @since 2023-09-22 20:26:26
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_global")
@ApiModel(description = "网站基本信息实体")
public class Global  {
    @ApiModelProperty(notes = "不用管，方便查询")
    private Integer id;
    @ApiModelProperty(notes = "网站标题")
    private String title;
    @ApiModelProperty(notes = "最新公告")
    private String bulletin;
    @ApiModelProperty(notes = "注册状态，0为注册使用，1为注册后需要审批")
    private String registerFlag;
    @ApiModelProperty(notes = "用户最多预约时间，以小时为单位")
    private Long maxTime;
    @ApiModelProperty(notes = "用户最多能预约多少张卡")
    private Long cardLimit;

}
