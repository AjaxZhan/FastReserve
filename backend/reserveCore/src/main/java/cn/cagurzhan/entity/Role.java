package cn.cagurzhan.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Role)表实体类
 *
 * @author makejava
 * @since 2023-09-24 11:51:57
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_role")
public class Role  {
    @TableId
    private Long id;
    private String roleName; // 角色名称
    private String roleKey; // 角色key
    private Integer delFlag;

}
