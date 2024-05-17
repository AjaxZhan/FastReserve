package cn.cagurzhan.mapper;

import cn.cagurzhan.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

/**
 * @author Cagur
 * @version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
