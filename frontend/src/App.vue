<script setup>
import {useUserStore} from '@/stores/user.js'
import { onMounted } from 'vue'
import { useGlobalStore } from './stores/global'
  const userStore = useUserStore()
  const globalStore = useGlobalStore()
  // 刷新用户，获取网站基本信息
  onMounted(async ()=>{
    userStore.flushUser()
     await globalStore.getWebInfo()
  })



</script>

<template>

  <RouterView />
</template>

<style>
  #app{
    height: 100%;
  }
</style>
