<template>
    <div class="user-header">
      <el-form :inline="true" :model="formData">
        <el-form-item label="请选择服务器">
        <el-select v-model="currentServer" placeholder="选择服务器" style="width: 300px;" @change="handleServerSelect()">
            <el-option
            v-for="g in formServer.server"
            :key = "g.id"
            :label="g.title"
            :disable="g.status==1"
            :value="g.id"></el-option>
        </el-select>
      </el-form-item>
      </el-form>
    </div>
    <div class="table">
      <el-table :data="tableList.data" style="width: 100%; " row-style="height:50px">
        <el-table-column
          v-for="item in tableLabel"
          :key="item.prop"
          :label="item.label"
          :prop="item.prop"
          :width="item.width ? item.width : 125"
          align="center"
          style="font-size: 20px;"
        />
    </el-table>
    </div>
</template>

<script setup>
import { onMounted, reactive , ref } from "vue";
import { useStatusStore } from "../../stores/status";
import {useGPUStore} from '@/stores/gpu.js'

const statusStore = useStatusStore()
const gpuStore = useGPUStore()
/**
 * Data : 数据区
 */

// 选择的服务器
const currentServer = ref()

// 服务器多选框展示
const formServer = reactive({
  server : [],
})

// 表格标题
const tableLabel = reactive([
  {
    prop: "server_id",
    label: "服务器ID",
    width: 120,
  },
  {
    prop: "gpu_index",
    label: "GPU编号",
    width: 120,
  },
  {
    prop: "gpu_name",
    label: "GPU名称",
    width: 220,
  },
  {
    prop: "temperature",
    label: "温度",
    width: 100,
  },
  {
    prop: "used",
    label: "显存使用",
    width: 120,
  },
  {
    prop: "total",
    label: "总显存",
    width: 120,
  },
  {
    prop: "utilization",
    label: "GPU利用率",
    width: 120,
  },
  {
    prop: "username",
    label: "使用者",
    width: 250,
  },
  {
    prop: "command",
    label: "程序名称",
    width: 250,
  },
]);

// GPU状态展示信息
const tableList = reactive({
    data : [],
})

/**
 * Event : 事件区
 */

// 选中服务器，发送请求更新GPU状态
const handleServerSelect = async() =>{
  tableList.data = []
  await getGPUStatusData(currentServer.value)
}

// 

// 访问页面
onMounted(()=>{
    init();
})

// 初始化
const init = async ()=>{
  // 更新服务器信息
  currentServer.value = []
  updateServer()
}

/**
 * Utils : 工具区
 */

 // 将server信息 转为 多选框展示视图
function parseServerModelToView(model){
  var views = []
  if(model === undefined) return;
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

// 更新Server
const updateServer = async()=>{
  await gpuStore.updateServerModel() // 请求所有服务器数据
  formServer.server = parseServerModelToView(gpuStore.serverModel.server) // 服务器数据  -->  视图
}

// 获取GPU状态的数据
const getGPUStatusData = async (id) =>{
    await statusStore.updateStatusModel({id : id})
    console.log(statusStore.statusModel.status)
    if(statusStore.statusModel.status===undefined) return;
    for(var i=0;i<statusStore.statusModel.status.gpus.length;i++){
      tableList.data.push({
        server_id : statusStore.statusModel.status.id,
        gpu_index :  statusStore.statusModel.status.gpus[i].index,
        gpu_name : statusStore.statusModel.status.gpus[i].name,
        temperature : statusStore.statusModel.status.gpus[i].temperature,
        used : statusStore.statusModel.status.gpus[i].used,
        total : statusStore.statusModel.status.gpus[i].total,
        utilization : (parseFloat(statusStore.statusModel.status.gpus[i].used) / parseFloat(statusStore.statusModel.status.gpus[i].total)).toFixed(2),
        username : '无',
        command : '无'
      })
      if(statusStore.statusModel.status.gpus[i].processes.length > 0){
        tableList.data[i].username = statusStore.statusModel.status.gpus[i].processes[0].username
        tableList.data[i].command = statusStore.statusModel.status.gpus[i].processes[0].command
      }
    }
}

</script>

<style lang="less" scoped>
.table {
   position: relative;
   height: 520px;
   .pager {
     position: relative;
     margin-top: 30px;
     justify-content: right;
   }
}

.el-table .td{
  text-align: center !important;
}

.user-header {
  display: flex;
  justify-content: space-between;
}
</style>