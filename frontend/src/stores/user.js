import {  reactive ,ref} from 'vue'
import { defineStore } from 'pinia'

import { getCurrentInstance } from 'vue'
/**
 * 负责用户信息的状态管理
 */
export const useUserStore = defineStore('user', () => {    

    const {proxy} = getCurrentInstance()
    // 用户数据
    const user = reactive({
        username : '',
        password : '',
        userInfo : {
            email : '',
            sex : '',
            nickName : '',
            id : '',
            status : '',
            type : '',
        },
    })

    // 更新用户信息，存入本地
    const updateUser = async (username,password,userInfo)=>{
        user.username = username
        user.password = password
        user.userInfo = userInfo
        localStorage.setItem('user',JSON.stringify(user))
    }

    // 从本地读取用户信息
    const flushUser = ()=>{
        if(!localStorage.getItem('user')){
            return;
        }
        let res = JSON.parse(localStorage.getItem('user'))
        user.username = res.username;
        user.password = res.password
        user.userInfo = res.userInfo
    }

    return {user,updateUser,flushUser}
 
})
