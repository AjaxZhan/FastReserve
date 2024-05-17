import {  reactive } from 'vue'
import { defineStore } from 'pinia'

import { getCurrentInstance } from 'vue'
/**
 * 负责网站基本信息的状态管理
 */
export const useGlobalStore = defineStore('global', () => {    

    const {proxy} = getCurrentInstance()

    // 网站基本信息
    const webInfo = reactive({
        title :  '',
        registerFlag  : false,
        bulletin : '',
        maxTime : 60,
        cardLimit : 1,
    })

    // 发送请求更新网站信息
    const getWebInfo= async () => {
        let res = await proxy.$api.getGlobal()
        webInfo.title = res.title
        webInfo.registerFlag = res.registerFlag === '0' ? false : true
        webInfo.bulletin = res.bulletin;
        webInfo.maxTime = res.maxTime
        webInfo.cardLimit = res.cardLimit
    }

    return {webInfo,getWebInfo}

})
