<template>
  <el-col
    :span="15"
    style="margin-top: 50px; margin-left: auto; margin-right: auto"
  >
    <el-card shadow="hover" style="">
      <div class="user">
        <div class="icon" style="text-align: center">
          <img src="/logo-nobg.png" alt="/" style="width: 200px;height: 200px;" />
        </div>
        <div class="user-info" style="margin-top: 10px">
          <h3
            class="name"
            style="font-size: 24px; font-weight: 900; text-align: center"
          >
            {{ userStore.user.userInfo.nickname
            }}{{
              userStore.user.userInfo.sex == 0 ? "先生" : "女士"
            }}，{{ sayHello(new Date()) }}！欢迎您来到FastReserve GPU预约系统
          </h3>
        </div>
      </div>
      <div
        class="login-info"
        style="
          margin-top: 20px;
          text-align: center;
          font-size: 20px;
          font-weight: 700;
        "
      >
        <el-descriptions
          class="margin-top"
          :column="3"
          :size="size"
          title="个人信息"
          style="margin-top: 50px;"
          border
        >
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="iconStyle">
                  <user />
                </el-icon>
                用户名
              </div>
            </template>
            <span class="showspan">{{ userStore.user.username }}</span>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="iconStyle">
                  <ChatLineRound />
                </el-icon>
                邮箱
              </div>
            </template>
            <span class="showspan">{{  userStore.user.userInfo.email  }}</span>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="iconStyle">
                  <Avatar />
                </el-icon>
                身份
              </div>
            </template>
            <span class="showspan"> {{ userStore.user.userInfo.type == 0 ? '用户' : '超级管理员' }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 30px;">
            您的当前预约记录为：<span style="color: #ab3724;">{{ reserveCount ? reserveCount : 0 }}</span>条，
            您的剩余预约时长为：<span style="color: #ab3724;">{{ duration ? duration : globalStore.webInfo.maxTime }}</span>小时
        </div> 
        <el-row style="margin-top: 30px;display: flex;justify-content: center;">
          <el-button color="#ab3724" size="large" @click="handleChange">更改资料</el-button>
          <el-button color="#ab3724" size="large" @click="router.push({name : 'system:reserve:query'})">查看预约</el-button>
        </el-row>
      </div>
    </el-card>
  </el-col>

   <!-- 注册 -->
   <el-dialog
    v-model="dialogFormVisible"
    title="修改资料"
    class="change"
    >
    <el-form :model="chanegForm">
      <el-form-item label="姓名" :label-width="formLabelWidth" prop="nickname">
        <el-input placeholder="输入要重置的姓名" v-model="chanegForm.nickname" autocomplete="off" />
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth" prop="sex">
        <el-select v-model="chanegForm.sex" placeholder="选择要更改的性别">
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
        <el-input placeholder="输入要更改的邮箱" v-model="chanegForm.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" :label-width="formLabelWidth" prop="password">
        <el-input placeholder="输入新密码，不修改请留空" v-model="chanegForm.password" autocomplete="off" />
      </el-form-item>
    </el-form>
    <el-row style="justify-content: center;margin-top: 30px;">
      <el-button color="#ab3724" @click="handleChangeSubmit" style="width: 100px;height: 40px;">确认修改</el-button>
    </el-row>
  </el-dialog>

</template>

<script setup>
import {ref,reactive,getCurrentInstance, onMounted, onBeforeMount} from "vue"
import { useUserStore } from "@/stores/user.js";
import {useRouter} from 'vue-router'
import { ElMessage } from 'element-plus'
import { useMenuStore } from "../../stores/menu"
import {useReserveStore} from '@/stores/reserve.js'
import { useGlobalStore } from "../../stores/global";

const userStore = useUserStore();
const globalStore = useGlobalStore()
const router = useRouter()
const {proxy} = getCurrentInstance()
const menuStore = useMenuStore()
const reserveStore = useReserveStore()

/**
 * 数据区
 */

// 表单宽度
const formLabelWidth = "140px";

// 对话框显示
const dialogFormVisible = ref(false)

// 修改资料的表单
const chanegForm = reactive({
  id : '',
  nickname : '',
  email : '',
  sex : '',
  password : '',
})

// 用户剩余预约时长
const duration = ref()
// 用户预约数
const reserveCount = ref()

/**
 * 工具区
 */

// 计算用户剩余时长
const calculateDuration = async()=>{
  // 最大时长
  let maxDuration = globalStore.webInfo.maxTime
  let d = 0
  let c = 0
  // 遍历所有符合条件的预约，累加时长
  await reserveStore.updateReserveModelMy(userStore.user.userInfo.id)
  // 防止空指针异常  
  if(reserveStore.reserveModelMy.data === undefined){
    return ;
  }
  reserveStore.reserveModelMy.data.forEach(reserve=>{
    let now = new Date()
    if(new Date(reserve.end) > now){
      c = c + 1
      d = d + (( new Date(reserve.end).getTime() - new Date(reserve.start).getTime()) / (60*1000*60))
    }
  })

  duration.value  = (maxDuration - d).toFixed(2)
  reserveCount.value = c
}

// 生成欢迎语句 
const sayHello = (date) =>{
    if(date.getHours() >= 5 && date.getHours() <= 11){
        return "早上好";
    }else if(date.getHours() >= 11 && date.getHours() <= 13){
        return "中午好";
    }else if(date.getHours() >= 13 && date.getHours() <= 18){
        return "下午好";
    }else if(date.getHours() >= 18 && date.getHours() <= 23){
        return "晚上好";
    }else if(date.getHours()>=23 || date.getHours()<=4){
        return "夜深了，早点休息哦~"
    }
}

/**
 * 事件区
 */

// 显示修改资料对话框
const handleChange = ()=>{
  chanegForm.nickname = userStore.user.userInfo.nickname
  chanegForm.sex = userStore.user.userInfo.sex == '0' ? '男' : '女'
  chanegForm.email = userStore.user.userInfo.email
  dialogFormVisible.value = true
}

// 修改资料提交
const handleChangeSubmit =async ()=>{
  chanegForm.id = userStore.user.userInfo.id;
  await proxy.$api.updateUser(chanegForm)
      ElMessage({
        message: '修改成功!请重新登录',
        type: 'success',
  })
  location.replace('/login')
} 

// 定时计算剩余时长
setInterval(async () => {
  await calculateDuration()
}, 20000);

// 刚进入时，获取菜单，计算剩余时长
onMounted(async()=>{
  await menuStore.flushMenus()
  await calculateDuration()
})


</script>

<style lang="less" scoped>
.home {
  .user {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 1px solid #ccc;
    margin-bottom: 20px;
    img {
      width: 150px;
      height: 150px;
      border-radius: 50%;
      margin-right: 40px;
    }
  }
  .login-info {
    p {
      line-height: 30px;
      font-size: 14px;
      color: #999;
      span {
        color: #666;
        margin-left: 60px;
      }
    }
  }
}
.showspan{
  font-weight: 700;
  color : #ab3724,
}
.change{
  .el-input{
    margin-right: 200px;
  }
}
</style>
