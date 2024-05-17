<template>
    
    <!-- FullCalendar -->

    <FullCalendar :options="calendarOptions" >
        <template v-slot:eventContent='arg'>
            <div style=" font-weight: 900;"
            :color="arg.event.color"
            >{{ arg.event.title }}</div>
        </template> 
    </FullCalendar>

</template>

<script setup>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import timeGridPlugin from '@fullcalendar/timegrid'

import { ElMessage } from 'element-plus'
import { ref,getCurrentInstance, onMounted, reactive } from 'vue'
import {useReserveStore} from '@/stores/reserve.js'
import {useGPUStore} from '@/stores/gpu.js'
import { useUserStore } from '../../stores/user'

const {proxy} = getCurrentInstance()

const reserveStore = useReserveStore()
const userStore = useUserStore()
/**
 * 数据区
 */

// 日历数据
const calendarOptions = reactive( {
        plugins: [ dayGridPlugin, interactionPlugin,timeGridPlugin ],
        // View : Initial View
        initialView: 'dayGridMonth',
        // View : HeaderToolbar
        headerToolbar:{
            left : 'prev,next',
            center : 'title',
            right : 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        // Data : events
        events: parseReserveModelToView(reserveStore.reserveModelMy.data)
})

/**
 * 工具区
 */

// 预约数据转为视图
function parseReserveModelToView(model){
    var views  = []
    // 避免空指针异常
    if(model === undefined) return ;
    for(var i=0;i<model.length;i++){
        views.push(
            {
                id  : model[i].id,
                title : model[i].gpu.serverName+ "-" + 'GPU' + model[i].gpu.number  + "-" + model[i].gpu.model + '-' + model[i].title, 
                start: model[i].start,
                end : model[i].end,          
                color : getRandomColor(),
            }
        )
    }
    return views;
}

// 随机颜色
function getRandomColor(){
    var letters = '0123456789ABCDEF';  
    var color = '#';  
    for (var i = 0; i < 6; i++) {  
        color += letters[Math.floor(Math.random() * 16)];  
    }  
    return color;  
}

// 更新视图数据
const updateView = async () =>{
    await reserveStore.updateReserveModelMy(userStore.user.userInfo.id)
    calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModelMy.data)
}

// 初始化
const init = async () =>{
    await updateView()
}

/**
 * 事件区
 */

// 定时任务
setInterval(async()=>{
    await updateView()
},60000)

// 启动任务
onMounted(async()=>{
    await init()
})


</script>   

<style scoped>
/* dialog */

.el-button--text {
  margin-right: 15px;
}
.el-select {
  width: 300px;
}
.el-input {
  width: 300px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}

</style>