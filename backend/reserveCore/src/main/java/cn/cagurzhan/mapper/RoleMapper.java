package cn.cagurzhan.mapper;

import cn.cagurzhan.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long id);
}
