<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import DefaultLayout from '@/components/layout/DefaultLayout.vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'

const route = useRoute()
const router = useRouter()

// 根据路由meta.layout选择布局
const currentLayout = computed(() => {
  const layout = route.meta?.layout || 'default'
  return layout
})
</script>

<template>
  <!-- 空白布局（登录页、404页） -->
  <template v-if="currentLayout === 'blank'">
    <router-view />
  </template>

  <!-- 管理后台布局 -->
  <AdminLayout v-else-if="currentLayout === 'admin'" />

  <!-- 默认布局 -->
  <DefaultLayout v-else />
</template>
