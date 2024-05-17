<template>
    <div class="user-header">
      <el-form :inline="true" :model="formData">
        <el-form-item label="">
          <el-input
            v-model="formData.keyword"
            placeholder="请输入预约ID"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button color="#ab3724" @click="handleSearch">搜索</el-button>
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
        <el-table-column fixed="right" label="操作" min-width="200">
          <template #default="scope">
            <el-popconfirm title="确定要删除吗？已通过的工单，删除则取消预约" @confirm="handleDelete(scope.row)">
              <template #reference>
            <el-button type="danger" size="small"
              >删除</el-button>
            </template>
          </el-popconfirm>
          </template>
        </el-table-column>
    </el-table>
      <!-- 分页 -->
      <el-pagination
        background
        layout="prev, pager, next"
        :total="parseInt(pageConfig.total)"
        :page-size="pageConfig.pageSize"
        class="pager mt-4"
        @current-change="handleChangePage"
      />
    </div>
</template>

<script setup>
import { getCurrentInstance, onMounted, reactive } from "vue";
import { useUserStore } from "../../stores/user";
import { useGPUStore } from '../../stores/gpu'
import {ElMessage} from 'element-plus'

const {proxy} = getCurrentInstance()
const userStore = useUserStore()
const gpuStore = useGPUStore()
/**
 * Data : 数据区
 */

// 表格标题
const tableLabel = reactive([
  {
    prop: "id",
    label: "预约ID",
    width: 80,
  },
  {
    prop: "gpuInfo",
    label: "GPU信息",
    width: 220,
  },
  {
    prop: "duration",
    label: "申请时长(小时)",
    width: 120,
  },
  {
    prop: "start",
    label: "开始时间",
    width: 180,
  },
  {
    prop: "end",
    label: "结束时间",
    width: 180,
  },
  {
    prop: "title",
    label: "申请原因",
    width: 300,
  },
  {
    prop: "status",
    label: "审核状态",
    width: 100,
  },
]);

// 展示信息
const tableList = reactive({
    data : [],
})

// 原始数据
const orderModel = reactive({
  data : [],
})


// 分页信息
const pageConfig = reactive({
    total : 0,
    pageSize : 10 , 
})

// 分页请求参数
const pageParam = reactive({
    pageNum : 1,
    pageSize : pageConfig.pageSize,
    userId : '',
});

// 搜索信息
const formData = reactive({
    keyword: "",
});

/**
 * Event : 事件区
 */

// 删除数据
const handleDelete = async (row) =>{
  await proxy.$api.delete(row)
  ElMessage({
      message: '删除成功！',
      type: 'success',
  })
  await getOrder(pageParam)
  updateOrder()
}

// 访问页面
onMounted(async ()=>{
    // 获取GPU信息
    await gpuStore.updateGPUModel()
    pageParam.userId = userStore.user.userInfo.id
    await getOrder(pageParam)
    updateOrder()
})

// 分页
const handleChangePage = (page) => {
    pageParam.pageNum = page;
    getOrder(pageParam)
};

// 搜索
const handleSearch = () => {
    tableList.data = tableList.data.filter(item =>(item.id.toLowerCase().indexOf(formData.id)) > -1) 
};

/**
 * Utils : 工具区
 */

// 获取工单数据
const getOrder = async (params) =>{
    let res = await proxy.$api.listMyOrder(params)
    orderModel.data = res.rows
    pageConfig.total = res.total
}

// 更新工单数据
const updateOrder = ()=>{
  tableList.data = []
  for(var i=0;i<pageConfig.total;i++){
      tableList.data.push({
        id : orderModel.data[i].id,
        gpuInfo : '',
        duration : ((new Date(orderModel.data[i].end).getTime() - new Date(orderModel.data[i].start).getTime())/(60*1000*60)).toFixed(2),
        start : orderModel.data[i].start,
        end : orderModel.data[i].end,
        title : orderModel.data[i].title,
        status : '',
      })
      let gpu = gpuStore.allGPU.data.find(item=>item.id === orderModel.data[i].gpuId)
      tableList.data[i].gpuInfo = gpu.serverName + "-" + gpu.number + "号卡"
      if(orderModel.data[i].status == "1"){
        tableList.data[i].status = '未审核'
      }else if(orderModel.data[i].status == "2"){
        tableList.data[i].status = '已通过'
      }else if(orderModel.data[i].status == "3"){
        tableList.data[i].status = '已驳回'
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