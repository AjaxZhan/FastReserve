package cn.cagurzhan.service;

import cn.cagurzhan.dto.AdminUserUpdateDTO;
import cn.cagurzhan.entity.ResponseResult;

/**
 * @author Cagur
 * @version 1.0
 */
public interface AdminService {
    ResponseResult getUser(Integer pageNum, Integer pageSize);

    ResponseResult updateUser(AdminUserUpdateDTO adminUserUpdateDTO);

    ResponseResult toggleStatus(AdminUserUpdateDTO adminUserUpdateDTO);

    ResponseResult deleteUser(AdminUserUpdateDTO adminUserUpdateDTO);

    ResponseResult addUser(AdminUserUpdateDTO adminUserUpdateDTO);
}
