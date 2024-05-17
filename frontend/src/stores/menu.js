import {  getCurrentInstance, reactive } from 'vue'
import { defineStore } from 'pinia'
/**
 * 负责菜单信息的状态管理
 */
export const useMenuStore = defineStore('menu', () => {

    const {proxy} = getCurrentInstance();

    // 菜单信息
    // const Menus = reactive([
    //     {
    //         path:'/home',
    //         name:'home',
    //         label:'用户中心',
    //         icon:'userFilled',
    //         url:'/'
    //     },
    //     {
    //         path: '/reserve',
    //         label:'GPU预约',
    //         icon: 'Calendar',
    //         children: [
    //             {
    //                 path: '/query',
    //                 name : 'reserve_query',
    //                 label: '预约中心',
    //                 icon: 'starFilled',
    //                 url :'/reserve/query'
    //             },
    //             {
    //                 path: '/my',
    //                 name : 'reserve_my',
    //                 label: '我的预约',
    //                 icon: 'HomeFilled',
    //                 url :'/reserve/my'
    //             },
    //             {
    //                 path: '/order',
    //                 name : 'reserve_order',
    //                 label : '工单系统',
    //                 icon : 'system',
    //                 url : '/reserve/system'
    //             }
    //         ]
    //     },
    //     {
    //         path: '/admin',
    //         label:'管理中心',
    //         icon: 'Avatar',
    //         children: [
    //             {
    //                 path: '/user',
    //                 name : 'admin_user',
    //                 label: '用户管理',
    //                 icon: 'UserFilled',
    //                 url :'/admin/user'
    //             },
    //             {
    //                 path: '/gpu',
    //                 name : 'admin_gpu',
    //                 label: 'GPU管理',
    //                 icon: 'MessageBox',
    //                 url :'/admin/gpu'
    //             },
    //             {
    //                 path: '/server',
    //                 name : 'admin_server',
    //                 label: '服务器管理',
    //                 icon: 'MessageBox',
    //                 url :'/admin/server'
    //             },
    //             {
    //                 path: '/global',
    //                 name : 'admin_global',
    //                 label: '网站基本信息',
    //                 icon: 'setting',
    //                 url :'/admin/global'
    //             },
    //             {
    //                 path : '/order',
    //                 name : 'admin_order',
    //                 label: '工单审核',
    //                 icon : 'setting',
    //                 url : 'admin/order'
    //             }
    //         ]
    //     }
    // ])
    
    // 菜单数据
    const Menus = reactive({
        menus: [],
    })

    // 是否折叠
    const isCollapse = reactive({is : true})

    // 当前选择菜单
    const current = reactive({
        currentMenu : {},
    })

    // 折叠状态更新
    function updateIsCollapse(){
        isCollapse.is = !isCollapse.is
    }

    // 选择菜单
    function selectMenu(val){
        val.name === 'home' ? (current.currentMenu = {} ) : (current.currentMenu = val)
    }

    /**
     * 发送请求，获取菜单信息
     */
    const updateMenu = async () =>{
        let res = await proxy.$api.getRouters()
        Menus.menus = menuFormatter(res.menus)
        localStorage.setItem("menus",JSON.stringify(Menus.menus))
    }
    /**
     * 从本地存储更新菜单信息
     */
    const flushMenus = async() =>{
        if(!localStorage.getItem("menus")) return;
        Menus.menus = JSON.parse(localStorage.getItem("menus"))
    }

    /**
     * 格式化菜单信息
     */
    const menuFormatter = (menus)=>{
        let newMenus = [];
        for(var i=0;i<menus.length;i++){
            if( JSON.stringify(menus[i].children) === '[]'){
                newMenus.push({
                    path : "/" + menus[i].path,
                    name : menus[i].perms,
                    label : menus[i].menuName,
                    icon : menus[i].icon,
                    url: menus[i].component!==undefined ? "/" + menus[i].component : null,
                })
            }else{
                newMenus.push({
                    path : "/" + menus[i].path,
                    name : menus[i].perms,
                    label : menus[i].menuName,
                    icon : menus[i].icon,
                    url: menus[i].component!==undefined ? "/" + menus[i].component : null,
                    children : menus[i].children ?  menuFormatter(menus[i].children) : null
                })
        }
            
        }
        return newMenus;
    }
    

    return {Menus,updateMenu, isCollapse,current, updateIsCollapse,selectMenu,flushMenus}

})
