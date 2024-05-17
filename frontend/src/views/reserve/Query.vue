<template>

    <!-- 组件：Select -->
    <el-form :model="formGPU" style="display: flex; justify-content: left;width: 100%;">
      
      <el-form-item label="请选择服务器">
        <el-select v-model="currentServer" multiple collapse-tags placeholder="选择服务器" style="width: 300px;" @change="handleServerSelect()" @remove-tag="handleServerSelect">
            <el-option
            v-for="g in formServer.server"
            :key = "g.id"
            :label="g.title"
            :disabled="g.status==1"
            :value="g.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="请选择GPU型号" style="margin-left: 20px;">
        <el-select v-model="currentGPU" multiple collapse-tags placeholder="GPU型号" style="width: 300px;" @change="handleGpuSelect()" @remove-tag="handleGpuSelect()">
            <el-option
            v-for="g in formGPU.gpu"
            :key = "g.id"
            :label="g.title"
            :disabled="g.status==1"
            :value="g.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="margin: auto;">
        <el-radio-group v-model="isMy" @change="handleIsMy()">
          <el-radio label="0" border>查看所有</el-radio>
          <el-radio label="1" border>只看我的</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item style="margin-left: auto;">
        <el-button color="#ab3724" @click="dialogType='order';addVisible=true;ElMessage({
                message: '请认真填写，工单一经提交不支持修改',
                type: 'warning',
      });reserveForm.gpuId = info.event.extendedProps.gpuId">提交工单</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 组件：FullCalendar -->

    <FullCalendar :options="calendarOptions" >
        <template v-slot:eventContent='arg'>
            <div style=" font-weight: 900;"
            :color="arg.event.color"
            >{{ arg.event.title }}</div>
        </template> 
    </FullCalendar>

    <!-- 组件：修改Dialog -->

  <el-dialog
    v-model="dialogFormVisible"
    :title="修改预约"
  >
    <el-form :model="reserveForm" :rules="rules">
      <el-form-item label="申请原因" :label-width="formLabelWidth" prop="title">
        <el-input v-model="reserveForm.title" autocomplete="off" />
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="comment">
        <el-input v-model="reserveForm.comment" autocomplete="off" />
      </el-form-item>
      <el-form-item label="GPU" :label-width="formLabelWidth" prop="gpuId">
        <el-select v-model="reserveForm.gpuId" placeholder="请选择GPU">
            <el-option
            v-for="g in innerFormGPU.gpu"
            :key = "g.id"
            :disabled="g.status == 1"
            :label="g.title"
            :value="g.id"
            ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" :label-width="formLabelWidth" prop="start">
        <el-date-picker
        v-model="reserveForm.start"
        type="datetime"
        placeholder="选择日期和时间"
        arrow-control
        />
      </el-form-item>
      <el-form-item label="结束时间" :label-width="formLabelWidth" prop="end">
        <el-date-picker
        v-model="reserveForm.end"
        type="datetime"
        placeholder="选择日期和时间"
        arrow-control
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleDelete" type="danger">删除预约</el-button>
        <el-button type="primary" @click="handleSubmit">确认预约</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 组件：新增Dialog  -->
  <el-dialog
    v-model="addVisible"
    :title="dialogType == 'add' ? '新增预约' : '提交工单'"
  >
    <el-form :model="addForm" :rules="rules">
      <el-form-item label="申请原因" :label-width="formLabelWidth" prop="title">
        <el-input v-model="addForm.title" autocomplete="off" />
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="comment">
        <el-input v-model="addForm.comment" autocomplete="off" />
      </el-form-item>
      <el-form-item label="GPU" :label-width="formLabelWidth" prop="gpuIds">
        <el-select v-model="addForm.gpuIds" placeholder="请选择GPU" multiple collapse-tags>
            <el-option
            v-for="g in innerFormGPU.gpu"
            :key = "g.id"
            :disabled="g.status == 1"
            :label="g.title"
            :value="g.id"
            ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="开始时间" :label-width="formLabelWidth" prop="start">
        <el-date-picker
        v-model="addForm.start"
        type="datetime"
        placeholder="选择日期和时间"
        arrow-control
        />
      </el-form-item>
      <el-form-item label="结束时间" :label-width="formLabelWidth" prop="end">
        <el-date-picker
        v-model="addForm.end"
        type="datetime"
        placeholder="选择日期和时间"
        arrow-control
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleSubmitAdd">{{ dialogType=="add" ? '添加预约' : '提交工单'  }}</el-button>
      </span>
    </template>
  </el-dialog>
  

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
import {useUserStore} from '@/stores/user.js'

const {proxy} = getCurrentInstance()
const reserveStore = useReserveStore()
const gpuStore = useGPUStore()
const userStore = useUserStore()

/**
 * Data : 数据区
 */


// 服务器多选框展示
const formServer = reactive({
  server : [],
})
// GPU多选框展示
const formGPU = reactive({
    gpu : [],
})
// 表单内部的GPU多选框展示
const innerFormGPU = reactive({
  gpu : []
})

// 选择的服务器
const currentServer = ref([])
// 选择的GPU
const currentGPU = ref([])

// 修改预约 - 预约数据
const reserveForm = reactive({
    id : '',
    title : '',
    start : new Date(),
    end : new Date(),
    comment : '',
    createBy : 0,
    gpuId : 1,
})

// 添加预约 - 预约数据
const addForm = reactive({
    id : '',
    title : '',
    start : new Date(),
    end : new Date(),
    comment : '',
    createBy : 0,
    gpuIds : [],
})


/**
 * Component : 组件参数
 */

// 单选框，0表示查看所有，1表示查看我的预约
const isMy = ref('0')
// 修改对话框 - 是否可视
const dialogFormVisible = ref(false)
// 添加对话框 - 是否可视
const addVisible = ref(false)
// 添加对话框类型：预约add/工单order
const dialogType = ref('add')
// 表单宽度
const formLabelWidth = "140px";
// 服务器是否被选中
const isServerSelected = ref(false)
// 表单验证
const rules = reactive({
  title: [
    { required: true, message: '申请原因为必填项', trigger: 'blur' },
  ],
  start: [
    {
      required: true,
      message: '开始时间为必填项',
      trigger: 'change',
    },
  ],
  end: [
    {
      required: true,
      message: '结束时间为必填项',
      trigger: 'change',
    },
  ],
  gpuId: [
    {
      required: true,
      message: '请选择要预约的GPU！',
      trigger: 'change',
    },
  ],
})
// 日历参数
const calendarOptions = reactive( {
        plugins: [ dayGridPlugin, interactionPlugin,timeGridPlugin ],
        // View : Initial View
        initialView: 'dayGridMonth',
        // View : HeaderToolbar
        headerToolbar:{
            left : 'prev,next',
            center : 'title',
            right : 'dayGridMonth,timeGridWeek,timeGridDay',
        },
        // Event : select
        selectable: true,
        select :  handleSelect ,
        // Event : Click
        dateClick : handleDateClick,
        // Event : edit
        editable : true,
        // Event : Click event 
        eventClick : handleEventClick,
        // Data : events
        events: parseReserveModelToView(reserveStore.reserveModel.data),
})

/**
 * Event : 事件区
 */

// 单选框被选中：是否查看我的预约
const handleIsMy =async()=>{
  if(isMy.value === '1'){
    // 查询我的预约
    await reserveStore.updateReserveModelMy(userStore.user.userInfo.id)
    calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModelMy.data)
  }else{

    if(currentServer.value.length>0 &&  currentGPU.value.length >0){
      // 查询预约
      await updateView()
    }else{
      // 查询所有
      await reserveStore.listAllReserveModel()
      calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModel.data) // 预约数据 --> 视图
    }
  }
}

// 事件被点击：修改预约
function handleEventClick(info){
    if(info.event.extendedProps.createBy == userStore.user.userInfo.id  || userStore.user.userInfo.type=='1'){
      // 必须是预约人才能修改自己的预约
      reserveForm.title = info.event.extendedProps.reason
      reserveForm.comment = info.event.extendedProps.comment
      reserveForm.start = info.event.start
      reserveForm.end = info.event.end
      reserveForm.gpuId = info.event.extendedProps.gpuId
      reserveForm.id = info.event.id
      reserveForm.createBy = info.event.extendedProps.createBy
      dialogFormVisible.value = true
    }
}
// 日历被点击：新增预约
function handleDateClick(arg){
    dialogType.value = 'add'
    addForm.start =  arg.dateStr
    addForm.gpuId = currentGPU.value[0]
    addVisible.value = true
}
// 日历被拖选：新增预约
function handleSelect(arg){
  dialogType.value = 'add'
  addForm.start = arg.start
  addForm.end = arg.end
  addForm.gpuIds = currentGPU.value
  addVisible.value = true
}
// 删除预约
const handleDelete= async () =>{
    await proxy.$api.delete(reserveForm)
    ElMessage({
        message: '删除成功！',
        type: 'success',
    })
    dialogFormVisible.value = false;
    reserveForm.title = ''
    await updateView()
}

// 新增预约 / 提交工单
const handleSubmitAdd = async () =>{
  if(dialogType.value === 'add'){
      addForm.createBy = userStore.user.userInfo.id
      await proxy.$api.addBatchReserve(addForm)
      ElMessage({
          message: '预约成功！',
          type: 'success',
      })
      addVisible.value = false;
      addForm.title = ''
      addForm.comment = ''
      await updateView()
  }else if(dialogType.value === 'order'){
    addForm.createBy = userStore.user.userInfo.id
    await proxy.$api.addBatchOrder(addForm)
    ElMessage({
        message: '工单提交成功，请等待管理员审核！',
        type: 'success',
    })
    addVisible.value = false;
    addForm.title = ''
    addForm.comment = ''
    await updateView()
    }
}

// 修改预约
const handleSubmit =async () =>{
    reserveForm.createBy = userStore.user.userInfo.id
    await  proxy.$api.update(reserveForm)
    ElMessage({
        message: '预约修改成功',
        type: 'success',
    })
    dialogFormVisible.value = false;
    reserveForm.title = ''
    reserveForm.comment = ''
    await updateView()
}
/**
 * GPU被选中
 */
const handleGpuSelect = async() =>{
  await updateView()
}
/**
 * 服务器被选中
 */
const handleServerSelect = async() =>{
  if(!isServerSelected.value){
    isServerSelected.value = true
  }
  await updateView()
}
// 定时任务
setInterval(()=>{
    // 更新GPU
    updateView()
},60000)
// 启动任务
onMounted(()=>{
    init()
})


/**
 * Utils : 工具区
 */
// GPU信息 转为 多选框展示视图
function parseGPUModelToView(model){
    var views = []
    for(var i=0;i<model.length;i++){
        views.push({
            id  : model[i].id,
            title : '【' + (model[i].status == '0' ? '正常' : '维护中') +'】'
                + model[i].serverVolume + '卡-' + model[i].serverName + '-' + 'GPU' + model[i].number 
            ,status : model[i].status
              }
        )
    }
    return views
}
// 预约信息 转为 日历显示视图
function parseReserveModelToView(model){
    var views  = []
    if(model === undefined){
      return views;
    }
    for(var i=0;i<model.length;i++){
        var color = getRandomColor()
        //TODO 获取服务器名和GPU名
        let idx = gpuStore.allGPU.data.find(item=>item.id === model[i].gpuId)
        let gpuIdx = idx.number
        let sName =idx.serverName
        views.push(
            {
                id  : model[i].id,
                title :  "[" +sName + '-' + gpuIdx + "号卡]" +  model[i].who + '-' + model[i].title, 
                start: model[i].start,
                end : model[i].end,          
                color : color,
                extendedProps : {
                  createBy : model[i].createBy,
                  comment : model[i].comment,
                  reason : model[i].title,
                  gpuId : model[i].gpuId
                }
            }
        )
    }
    return views;
}

// 将server信息 转为 多选框展示视图
function parseServerModelToView(model){
  var views = []
    for(var i=0;i<model.length;i++){
        views.push({
            id  : model[i].id,
            title : '【' + (model[i].status == '0' ? '正常' : '维护中') +'】'
                +  model[i].name + "--"
                + model[i].volume + '卡',
            status : model[i].status
      })
    }
  return views
}

// 随机生成颜色
function getRandomColor(){
    var letters = '0123456789ABCDEF';  
    var color = '#';  
    for (var i = 0; i < 6; i++) {  
        color += letters[Math.floor(Math.random() * 16)];  
    }  
    return color;  
}

// 更新GPU Server Reserve
const updateView = async () =>{

  // 服务器
  await gpuStore.updateServerModel() // 请求所有服务器数据
  formServer.server = parseServerModelToView(gpuStore.serverModel.server) // 服务器数据  -->  视图

  // GPU
  if(!isServerSelected.value){
  }else{
    // 已选服务器
    await gpuStore.getGPUByServerIds(currentServer.value)
    formGPU.gpu  = parseGPUModelToView(gpuStore.gpuModel.data)
  }

  // 预约
  if(currentGPU.value.length>0){
    await reserveStore.updateReserveModelByIds(currentGPU.value) // 根据多个gpuid，请求预约数据
    calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModel.data) // 预约数据 --> 视图
  }else{
    if(!(currentServer.value.length>0)){
      // 没有选择服务器
      // 请求所有预约信息
      await reserveStore.listAllReserveModel()
      calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModel.data) // 预约数据 --> 视图
    }else{
      // 选择服务器
      // 根据服务器ID 请求预约信息

      // 查询对应的GPU ids
      var len = currentServer.value.length
      var ids = []
      for(var i=0;i<len;i++){
        var idx = gpuStore.allGPU.data.filter(item => item.serverId === currentServer.value[i])
        if(idx === undefined) continue;
        for(var j=0;j<idx.length;j++){
          ids.push(idx[j].id)
        }
      }
      await reserveStore.updateReserveModelByIds(ids) // 根据多个gpuid，请求预约数据
      calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModel.data) // 预约数据 --> 视图
    }
  }


}

// 初始化
const init = async() =>{
  // 先缓存所有GPU数据
  await gpuStore.updateGPUModel()
  innerFormGPU.gpu = parseGPUModelToView(gpuStore.allGPU.data)
  // 初始化选中的GPU和服务器
  currentServer.value = []
  currentGPU.value = []
  // 请求GPU数据和Server数据
  await updateView()
  // 默认将预约数据更新为所有数据
  await reserveStore.listAllReserveModel()
  calendarOptions.events =  parseReserveModelToView(reserveStore.reserveModel.data) // 预约数据 --> 视图

}
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