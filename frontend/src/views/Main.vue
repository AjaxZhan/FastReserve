<!-- 
  Vue程序的根节点
 -->
<template>
  <div class="common-layout">
    <el-container class="lay-container">
        <!-- 侧边 -->
      <common-aside />
      <el-container>

        <!-- 头部 -->
        <common-header />

        <!-- 核心组件：路由 -->
        <el-main class="right-main">
          <router-view />
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import CommonHeader from "../components/CommonHeader.vue";
import CommonAside from "../components/CommonAside.vue";
</script>

<style lang="less" scoped>
.el-container{
    // 换行
    flex-wrap: wrap;
    align-items: flex-start;
    height: 100%;
}
.common-layout{
  height: 100%;
  & > .el-container{
    height: 100%;
    &> .el-aside{
      height: 100%;
      background: #545c64;
    } 
  }
  .lay-container{
    flex-wrap: nowrap;
    .right-main{
      height: calc(100% - 124px) !important;
    }
  }
}
</style>