
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// ElementPlus 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 引入less
import './assets/less/index.less'
// API
import api from './api/api'
// 引入vue-Markdown

const app = createApp(App)

// element 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 挂载api
app.config.globalProperties.$api  =api

// 路由守卫
router.beforeEach((to,from,next)=>{

    if(!localStorage.getItem('token') && to.name!=='login'){
        next({name : 'login'})
    }else{
        next()
    }
})
app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
