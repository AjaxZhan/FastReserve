import {  reactive } from 'vue'
import { defineStore } from 'pinia'

import { getCurrentInstance } from 'vue'
/**
 * 负责预约信息的状态管理
 */
export const useReserveStore = defineStore('reserve', () => {    

    const {proxy } = getCurrentInstance()

    // 预约数据
    const reserveModel = reactive({
        data : [
        // {
        //     id  : 1,                        
        //     title : '模型训练',                                    
        //     start: new Date('2023-09-10T10:00:00'),   
        //     end : new Date(),                
        //     comment : '我是说明，训练模型',    
        //     who : '刘老师', 
        //     createBy : 1,                  
        // },
    ]})
    // 我的预约数据
    const reserveModelMy = reactive({
        data : []
    })

    /**
     * @deprecated
     * 根据gpuId查询预约信息 
     * */
    const updateReserveModel = async(gpuId)=> {
        reserveModel.data = await proxy.$api.getByGPU({"gpuId" : gpuId});
    }

    /**
     * 查询所有预约信息
     */
    const listAllReserveModel = async() =>{
        reserveModel.data = await proxy.$api.listAllReserve();
    }

    /** 
     * 根据多个gpuId，查询预约信息
     * */
    const updateReserveModelByIds = async(gpuIds) =>{
        reserveModel.data = await proxy.$api.getReserveByGpuIds({"ids" : gpuIds})
    }
    /** 
     * 查询我的预约
     *  */
    const updateReserveModelMy = async(userId)=>{
        reserveModelMy.data  = await proxy.$api.getMyReserve({"userId" : userId})
    }



    return {reserveModel,updateReserveModel,reserveModelMy,updateReserveModelMy,updateReserveModelByIds,listAllReserveModel}

})
