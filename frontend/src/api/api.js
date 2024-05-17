/**
 * @description 管理整个项目的api
 */
import request from './request'

export default{
    // 批量添加预约
    addBatchReserve(params){
        return request({
            url : '/reserve/addBatch',
            method : 'post',
            data : params
        })
    },
    // 批量添加工单
    addBatchOrder(params){
        return request({
            url : '/order/addBatch',
            method : 'post',
            data : params
        })
    },
    // 获取所有预约信息
    listAllReserve(params){
        return request({
            url : '/reserve/list',
            method : 'get',
            data : params
        })
    },
    // 获取GPU信息
    getGPU(params){
        return request({
            url: '/gpu/getGPU',
            method: 'get',
            data : params,
        })
    },
    // 获取预约信息
    getByGPU(params){
        return request({
            url : '/reserve/getByGPU',
            method : 'get',
            data : params
        })
    },
    // 添加预约信息
    add(params){
        return request({
            url : '/reserve/add',
            method : 'post',
            data : params
        })
    },
    // 更新预约信息
    update(params){
        return request({
            url : '/reserve/update',
            method : 'post',
            data : params
        })
    },
    // 删除预约信息
    delete(params){
        return request({
            url  :'/reserve/delete',
            method : 'post',
            data : params
        })
    },
    // 登录
    login(params){
        return request({
            url : '/login',
            method : 'post',
            data : params
        })
    },
    // 注销
    logout(params){
        return request({
            url : '/logout',
            method : 'post',
            data : params
        })
    },
    //注册
    register(params){
        return request({
            url : '/register',
            method : 'post',
            data : params  
        })
    },
    // 获取我的预约数量
    getMyReserveCount(params){
        return request({
            url : '/reserve/getMyCount',
            method : 'get',
            data : params
        })
    },
    //更新资料
    updateUser(params){
        return request({
            url : '/user/update',
            method : 'post',
            data : params
        })
    },
    //获取我的预约信息
    getMyReserve(params){
        return request({
            url : '/reserve/getMy',
            method : 'get',
            data : params
        })
    },
    //管理员获取用户信息
    getAdminUser(params){
        return request({
            url : '/admin/getUser',
            method : 'get',
            data : params
        })
    },
    //管理添加用户
    addAdminUser(params){
        return request({
            url : '/admin/addUser',
            method : 'post',
            data : params
        })
    },
    // 管理员编辑用户信息
    updateAdminUser(params){
        return request({
            url : '/admin/updateUser',
            method : 'post',
            data : params
        })
    },
    // 管理员删除用户信息
    deleteUser(params){
        return request({
            url : '/admin/deleteUser',
            method : 'post',
            data : params
        })
    },
    // 管理员冻结用户
    toggleUserStatus(params){
        return request({
            url : '/admin/toggleStatus',
            method : 'post',
            data : params
        })
    },
    // 分页获取服务器信息
    getPageServer(params){
        return request({
            url : '/server/getAll',
            method : 'get',
            data : params
        })
    },
    //添加服务器
    addServer(params){
        return request({
            url : '/server/add',
            method : 'post',
            data : params
        })
    },
    //删除服务器
    deleteServer(params){
        return request({
            url : '/server/delete',
            method : 'post',
            data : params
        })
    },
    // 修改服务器
    updateServer(params){
        return request({
            url : '/server/update',
            method : 'post',
            data : params
        })
    },
    //分页查询GPU
    pageGPU(params){
        return request({
            url : '/gpu/getGPUPage',
            method : 'get',
            data : params
        })
    },
    //添加GPU
    addGPU(params){
        return request({
            url : '/gpu/addGPU',
            method : 'post',
            data : params
        })
    },
    //删除GPU
    deletGPU(params){
        return request({
            url : '/gpu/deleteGPU',
            method : 'post',
            data : params
        })
    },
    // 更新GPU
    updateGPU(params){
        return request({
            url : '/gpu/updateGPU',
            method : 'post',
            data : params
        })
    },
    // 更新网站信息
    updateGlobal(params){
        return request({
            url : '/admin/updateGlobal',
            method : 'post',
            data : params
        })
    },
    // 获取用户信息
    getGlobal(params){
        return request({
            url :  '/admin/getGlobal',
            method : 'get',
            data :params
        })
    },
    // 获取用户权限信息
    getInfo(params){
        return request({
            url :  '/getInfo',
            method : 'get',
            data :params
        })
    },
    // 获取路由
    getRouters(params){
        return request(
            {
                url : '/getRouters',
                method : 'get',
                data : params
            }
        )
    },
    // 获取所有服务器
    listAllServer(params){
        return request(
            {
                url : '/server/list',
                method : 'get',
                data : params
            }
        )
    },
    // 根据服务器ID获取GPU
    getGPUByServerId(params){
        return request({
            url : '/gpu/getGPUByServerId',
            method : 'get',
            data : params
        })
    },
    // 根据gpuIds，获取预约信息
    getReserveByGpuIds(params){
        return request({
            url : '/reserve/getByGPUs',
            method : 'post',
            data : params
        })
    },
    // 根据serverIds，查询GPU集合
    getGPUByServerIds(params){
        return request({
            url : '/gpu/getGPUByServerIds',
            method : 'post',
            data :params
        })
    },
    getGPUStatus(params){
        return request({
            url : '/gpu/getGPUStatus',
            method : 'get',
            data : params
        })
    },
    // 提交工单
    addOrder(params){
        return request({
            url : '/order/add',
            method : 'post',
            data : params
        })
    },
    // 审核工单
    verifyOrder(params){
        return request({
            url : '/order/verify',
            method : 'post',
            data : params
        })
    },
    // 查询工单
    listOrder(params){
        return request({
            url : '/order/list',
            method : 'get',
            data : params
        })
    },
    // 查询我的工单
    listMyOrder(params){
        return request({
            url : '/order/listMy',
            method : 'get',
            data : params
        })
    },
    // 审核不通过
    failOrder(params){
        return request({
            url : '/order/fail',
            method : 'post',
            data : params
        })
    },
    // 获取所有公告
    getAllBulletin(params){
        return request({
            url : '/bulletin/getAll',
            method : 'get',
            data : params
        })
    },
    // 分页获取公告
    getPageBulletin(params){
        return request({
            url : '/bulletin/getPage',
            method : 'get',
            data : params
        })
    },
    // 新增公告
    addBulletin(params){
        return request({
            url : '/bulletin/add',
            method : 'post',
            data : params
        })
    },
    // 删除公告
    deleteBulletin(params){
        return request({
            url : '/bulletin/delete',
            method : 'post',
            data : params
        })
    },
    // 更新公告
    updateBulletin(params){
        return request({
            url : '/bulletin/update',
            method : 'post',
            data : params
        })
    }
    

}