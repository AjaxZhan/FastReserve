<!-- 
  登录页面
 -->
<template>
  <div class="wrapper">
    <el-form
      v-model="loginForm"
      width="80px"
      class="login-box"
      style="
        background-color: #fff;
        border-radius: 10px;
        position: relative;
        top: 100px;
      ">
      <div class="login-title">欢迎来到 {{ globalStore.webInfo.title }}</div>
      <div class="myform">
        <el-form-item label="账号" prop="username">
          <el-input
            prefix-icon="el-icon-user"
            type="text"
            placeholder="请输入用户名"
            v-model="loginForm.username"
          />
        </el-form-item>
      </div>
      <div class="myform">
        <el-form-item label="密码" prop="password">
          <el-input
            prefix-icon="el-icon-lock"
            type="password"
            placeholder="请输入密码"
            v-model="loginForm.password"
          />
        </el-form-item>
      </div>

      <div class="myform" style="margin-top: 30px;">
        <el-form-item>
          <el-button type="primary" @click="handleSubmit(loginForm)" style="margin-right: 30px;">登录</el-button>
          <el-button type="primary" @click="dialogFormVisible = true"
            >注册</el-button>
        </el-form-item>
      </div>
    </el-form>

    <!-- 注册 -->
    <el-dialog
    v-model="dialogFormVisible"
    title="欢迎注册"
    class="register"
    >
    <el-form :model="registerForm" :rules="registerRules">
      <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
        <el-input v-model="registerForm.username" placeholder="请输入用户名，须与服务器账户一致"/>
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
        <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item label="姓名" :label-width="formLabelWidth" prop="nickname">
        <el-input v-model="registerForm.nickname" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth" prop="sex">
        <el-select v-model="registerForm.sex" placeholder="选择性别">
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
        <el-input v-model="registerForm.email" autocomplete="off" placeholder="请输入邮箱"/>
      </el-form-item>
    </el-form>
    <el-row style="justify-content: center;margin-top: 30px;">
      <el-button color="#ab3724" @click="handleRegisterSubmit" style="width: 100px;">注册</el-button>
    </el-row>
  </el-dialog>
  </div>
</template>

<script setup>
import { getCurrentInstance,reactive, ref } from "vue";
import  {useRouter} from 'vue-router'
import {useUserStore} from '@/stores/user.js'
import { ElMessage } from 'element-plus'
import { useGlobalStore } from "../stores/global";
import { useMenuStore } from "@/stores/menu.js";

const {proxy} = getCurrentInstance()
const router = useRouter()
const userStore = useUserStore()
const globalStore = useGlobalStore()
const menuStore = useMenuStore()

/**
 * 数据区
 */

// 登录表单
const loginForm = reactive({
  username: "",
  password: "",
});

// 注册表单
const registerForm = reactive({
  username : '',
  password : '',
  nickname : '',
  email : '',
  sex : '',
})

// 注册表单验证
const registerRules = reactive({
  username: [
    {
      required: true,
      message: "用户名不可为空！",
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: "密码不可为空！",
      trigger: "blur",
    },
  ],
  nickname: [
    {
      required: true,
      message: "姓名不可为空！",
      trigger: "blur",
    },
  ],
  email: [
    {
      required: true,
      message: "邮箱地址不可为空！",
      trigger: "blur",
    },
  ],
  sex: [
    {
      required: true,
      message: "必须选择性别！",
      trigger: "blur",
    },
  ],
});

// 注册窗口
const dialogFormVisible = ref(false);
// 表单宽度
const formLabelWidth = "100px"; 

/**
 * 事件区
 */

// 登录
const handleSubmit = async() => {
  // 请求
  let res =await  proxy.$api.login(loginForm)
  // token持久化
  localStorage.setItem('token',res.token)
  // 更新状态管理
  userStore.updateUser(loginForm.username,loginForm.password,res.userInfo)
  
  await menuStore.updateMenu()
  ElMessage({
      message: '登录成功，欢迎您！',
      type: 'success',
  })
  // 路由跳转
  router.push({
    name : 'system:home'
  })

};

// 注册
const handleRegisterSubmit = async()=>{
  let res = await proxy.$api.register(registerForm)
  if(res === undefined){
    ElMessage({
      type : 'success',
      message: '注册成功'
    })
  }
}

</script>

<style lang="less" scoped>
/**  Global Style */
* {
  margin: 0;
  padding: 0;
  font-size: 15px;
}

html,
body {
  height: 100%;
}

.register{
  .el-form-item{
    margin-top: 20px;
  }

  .el-input .el-select{
    margin-left: 50px;
    margin-right: 50px;
  }
}

/* LOGIN */

.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #fc466b, #3f5efb);
  overflow: hidden;
}
.login-box {
  border: 1px solid #dcdfe6;
  width: 400px;
  height: 300px;
  margin: 50px auto;
  justify-content: center;
  padding: 35px 35px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
}

.login-title {
  text-align: center;
  margin: 0 auto 40px auto;
  color: #303133;
  font-size: 20px;
  font-weight:900;
}

.myform{
    margin: 20px;
    display: flex;
    justify-content: center;
    .el-button{
        width: 100px;
    }
    .el-input{
        width: 250px;
    }
}
</style>
