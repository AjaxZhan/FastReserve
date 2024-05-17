<template>
  <el-col
    :span="15"
    style="margin-top: 50px; margin-left: auto; margin-right: auto"
  >
    <el-card shadow="hover" style="">
      <div
        class="web-info"
        style="
          margin-top: 20px;
          text-align: center;
          font-size: 20px;
          font-weight: 700;
        "
      >
        <div class="web-info" style="margin-top: 10px">
          <h3
            class="name"
            style="
              font-size: 24px;
              font-weight: 900;
              text-align: center;
              margin-bottom: 20px;
            "
          >
            网站基本信息
          </h3>
        </div>
        <el-form :model="webInfoForm.webInfo" label-width="120px">
          <el-form-item label="网站标题">
            <el-input v-model="webInfoForm.webInfo.title" />
          </el-form-item>
          <el-form-item label="注册后需激活">
            <el-switch v-model="webInfoForm.webInfo.registerFlag" />
          </el-form-item>
          <el-form-item label="最大预约时长">
            <el-input-number
              v-model="webInfoForm.webInfo.maxTime"
            ></el-input-number>(小时)
          </el-form-item>
          <el-form-item label="用户GPU卡限制">
            <el-input-number
              v-model="webInfoForm.webInfo.cardLimit"
            ></el-input-number> (张)
          </el-form-item>
          <el-form-item label="最新公告">
            <el-input
              v-model="webInfoForm.webInfo.bulletin"
              :rows="10"
              type="textarea"
              placeholder="(支持markdown格式)"
              size="large"
            />
          </el-form-item>
        </el-form>
        <el-row
          style="margin-top: 30px; display: flex; justify-content: center"
        >
          <el-button color="#ab3724" size="large" @click="handleSubmit"
            >确认修改</el-button
          >
        </el-row>
      </div>
    </el-card>
  </el-col>
</template>

<script setup>
import { getCurrentInstance, onMounted, reactive } from "vue";
import { useGlobalStore } from "@/stores/global.js";
import { ElMessage } from "element-plus";

const { proxy } = getCurrentInstance();
const globalStore = useGlobalStore();

/**
 * 数据区
 */

// 网站基本信息
const webInfoForm = reactive({
  webInfo: {
    title: "",
    registerFlag: false,
    bulletin: "",
    maxTime: 60,
    cardLimit : 1,
  },
});

/**
 * 事件区
 */

// 加载时执行
onMounted(async () => {
  globalStore.getWebInfo();
  webInfoForm.webInfo = globalStore.webInfo;
});

// 更新网站信息
const handleSubmit = async () => {
  let submitInfo = webInfoForm.webInfo;
  submitInfo.registerFlag = submitInfo.registerFlag === false ? "0" : "1";
  await proxy.$api.updateGlobal(submitInfo);
  ElMessage({
    message: "更新成功",
    type: "success",
  });
  globalStore.getWebInfo();
};
</script>
