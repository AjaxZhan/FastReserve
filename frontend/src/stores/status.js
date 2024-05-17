import {  reactive } from 'vue'
import { defineStore } from 'pinia'

import { getCurrentInstance } from 'vue'
/**
 * 负责GPU状态信息的状态管理
 */
export const useStatusStore = defineStore('status', () => {    

    const {proxy} = getCurrentInstance()

    // GPU状态信息
    const statusModel = reactive({
        status : '',
    })

    // 发送请求，获取GPU状态信息
    
    const updateStatusModel = async (id) =>{
        let res = await proxy.$api.getGPUStatus(id)
        statusModel.status = res
    }

    return {statusModel,updateStatusModel}

})
