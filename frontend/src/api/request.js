/**
 * @description 二次封装axios
 */
import axios from 'axios'
import {ElMessage} from 'element-plus'
const NETWORK_ERROR = '网络请求异常，请稍后重试'
const baseURL = 'http://127.0.0.1:7777'

// 创建axios实例
const service = axios.create({
    baseURL :  baseURL  // 后端地址
})
// 请求前拦截
service.interceptors.request.use((req)=>{
    if(req.url!=='/login' && req.url!=='/register' && req.url!=='/admin/getGlobal'){
        req.headers['token'] = localStorage.getItem('token')
    }
    return req;
})
// 请求后拦截
service.interceptors.response.use((res)=>{
    const {code,data,msg } = res.data

    if(code === 200){
        return data
    }else if(code == 400){
        ElMessage.error(msg || NETWORK_ERROR)
        return Promise.reject(msg || NETWORK_ERROR)
    }else if(code == 500){
        localStorage.removeItem('user')
        localStorage.removeItem('token')
        ElMessage.error(msg || '无权操作')
        return Promise.reject(msg || '无权操作')
    }else{
        ElMessage.error(msg || '表单校验失败，请检查是否缺项')
        return Promise.reject(msg || '未知错误，请检查网络')
    }
})

// 封装的核心函数
function request(options){
    // 请求方法和请求参数
    options.method = options.method || 'get'
    if(options.method.toLowerCase() === 'get'){
        options.params = options.data
    }

    service.defaults.baseURL = baseURL
    return service(options) 
}
export default request