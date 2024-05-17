package cn.cagurzhan.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserRole)表实体类
 *
 * @author makejava
 * @since 2023-10-10 16:30:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_role")
public class UserRole  {
    private Long userId;
    private Long roleId;




}
