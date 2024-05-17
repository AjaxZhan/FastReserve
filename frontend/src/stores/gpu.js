import {  reactive } from 'vue'
import { defineStore } from 'pinia'

import { getCurrentInstance } from 'vue'
/**
 * 负责GPU信息和服务器信息的状态管理
 */
export const useGPUStore = defineStore('gpu', () => {    

    const {proxy} = getCurrentInstance()

    // 全部GPU数据
    const allGPU =reactive({
        data : []
    })

    // GPU数据
    const  gpuModel = reactive({
        data :[
        // {
        //     id  : 1,        
        //     servereId : 1,
        //     serverName :'服务器1',  
        //     serverVolume: 4, 
        //     number : 1,     
        //     model :  '3090',     
        //     status: 0,      
        // },
    ]})

    // 服务器数据
    const serverModel = reactive({
        server : [
            // {
            //     id : 1,
            //     name : '服务器11111111',
            //     status : "0",
            //     volume: 4,
            // }
        ]
    })

    // 发送请求，获取所有GPU
    const updateGPUModel= async () => {
        allGPU.data =  await proxy.$api.getGPU()
    }

    // 发送请求，获取我预约的GPU
    const updateMyGPUModel = async(userId)=>{
        gpuModel.data = await proxy.$api.getMyGPU({userId : userId})
    }

    // 发送请求，获取所有服务器
    const updateServerModel = async()=>{
        serverModel.server = await proxy.$api.listAllServer();
    }

    /** 
     * @deprecated 
     * 发送请求，根据服务器ID获取GPU
     */
    const updateGPUModelByServerId = async(id)=>{
        gpuModel.data =  await proxy.$api.getGPUByServerId(id)
    }
    // 根据多个服务器id获取GPU
    const getGPUByServerIds = async(ids) =>{
        gpuModel.data = await proxy.$api.getGPUByServerIds({"ids" : ids })
    }


    return {gpuModel,serverModel,updateGPUModel,allGPU,updateMyGPUModel,getGPUByServerIds,updateServerModel,updateGPUModelByServerId}

})
