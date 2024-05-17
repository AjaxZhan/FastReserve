/**
 * vue-router
 * 负责路由管理
 */
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component:()=>import('../views/Main.vue'),
    redirect: '/home',
    name : 'main',
    children: [
      {
        path : '/home',
        name : 'system:home',
        component: ()=>import('../views/home/Home.vue')
      },
      {
        path : '/bulletin',
        name : 'system:bulletin',
        component: ()=>import('../views/Bulletin.vue')
      },
      {
        path : '/reserve/query',
        name : 'system:reserve:query',
        component : ()=>import('../views/reserve/Query.vue')
      },
      {
        path : '/reserve/my',
        name : 'system:reserve:my',
        component : ()=>import('../views/reserve/My.vue')
      },
      {
        path : '/reserve/order',
        name : 'system:reserve:order',
        component : ()=>import('../views/reserve/Order.vue')
      },
      {
        path : '/reserve/status',
        name : 'system:reserve:status',
        component : ()=>import('../views/reserve/Status.vue')
      },
      {
        path : '/admin/user',
        name : 'system:admin:user',
        component : ()=>import('../views/admin/User.vue')
      },
      {
        path : '/admin/gpu',
        name : 'system:admin:gpu',
        component : ()=>import('../views/admin/GPU.vue')
      },
      {
        path : '/admin/server',
        name : 'system:admin:server',
        component : ()=>import('../views/admin/Server.vue')
      },
      {
        path : '/admin/global',
        name : 'system:admin:global',
        component : ()=>import('../views/admin/Global.vue')
      },
      {
        path : '/admin/order',
        name : 'system:admin:order',
        component : ()=>import('../views/admin/Order.vue')
      },
      {
        path : '/admin/bulletin',
        name : 'system:admin:bulletin',
        component : ()=>import('../views/admin/Bulletin.vue')
      }
    ]
  } ,
  {
    path : '/login',
    name : 'login',
    component: ()=>import('../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
