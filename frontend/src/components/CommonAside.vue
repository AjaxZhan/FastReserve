<!-- 
  侧边栏实现
 -->
<template>
  <el-aside :width="menuStore.isCollapse.is ? '180px' : '64px'" style="background-color:#002140 ;">
    <el-menu
      class="el-menu-vertical-demo"
      background-color="#002140"
      text-color="#fff"
      :collapse="!menuStore.isCollapse.is"
      :collapse-transition="false"
    >
      <div class="logo" style="display: flex;justify-content: center; margin-top: 20px;margin-bottom: 20px;" v-show="menuStore.isCollapse.is">
          <img
            style="width: 100px; height: 100px;cursor: pointer;"
            src="/logo-nobg.png"
            fit="scale-down"
            @click="router.push({name : 'system:home'})"
          />
      </div>

      <el-menu-item
        :index="item.path"
        v-for="item in noChildren()"
        :key="item.path"
        @click="handleClickMenu(item)"
      >
        <el-icon>
          <component class="icons" :is="item.icon"></component>
        </el-icon>
        <span>{{ item.label }}</span>
      </el-menu-item>
      <el-sub-menu
        :index="item.label"
        v-for="item in hasChildren()"
        :key="item.path"
      >
        <template #title>
          <!-- 这里不包裹el-icon的话，折叠后无法显示图标。-->
          <el-icon>
            <component class="icons" :is="item.icon"></component>
          </el-icon>
          <span>{{ item.label }}</span>
        </template>

        <el-menu-item-group>
          <el-menu-item
            :index="subItem.path"
            v-for="(subItem, subIndex) in item.children"
            :key="subIndex"
            @click="handleClickMenu(subItem)"
          >
            <component class="icons" :is="subItem.icon"></component>
            <span>{{ subItem.label }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useMenuStore } from "@/stores/menu.js";
import {ref} from 'vue'

const menuStore = useMenuStore();
const router = useRouter();

/**
 * 数据区
 */

// 菜单
const menus = JSON.parse(localStorage.getItem("menus"))

// 头像
const imgSrc = ref("../assets/img/logo.png");

/**
 * 工具区
 */

// 找到父子菜单
const noChildren = () => {
  return menus.filter((item) => !item.children);
};
const hasChildren = () => {
  return menus.filter((item) => item.children);
};

// 获取头像
const getImgSrc = () => {
  return new URL(imgSrc.value, import.meta.url).href;
};

/**
 * 事件区
 */

// 菜单点击
const handleClickMenu = (item) => {
  router.push({
    name: item.name,
  });
};


</script>

<style lang="less" scoped>

// ICON 

.icons {
  width: 18px;
  height: 18px;
}
.el-menu {
  border-right: none;
  h3 {
    line-height: 48px;
    color: #fff;
    text-align: center;
  }
}


</style>
