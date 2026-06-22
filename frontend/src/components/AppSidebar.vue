<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth.js'
import { HomeFilled, Document, Edit, User, Odometer, Box, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// 侧边栏收起状态
const isCollapsed = ref(true) // 默认收起

const isLoggedIn = computed(() => authStore.isLoggedIn)
const isAdmin = computed(() => authStore.isAdmin)

// 所有菜单项
const menuItems = computed(() => {
  const items = [
    { path: '/', label: '首页', icon: HomeFilled },
    { path: '/items', label: '物品列表', icon: Document },
  ]

  // 管理员没有发布信息功能
  if (!isAdmin.value) {
    items.push(
      { path: '/publish', label: '发布信息', icon: Edit },
      { path: '/my/items', label: '我的发布', icon: User },
      { path: '/my/claims', label: '我的认领', icon: Document }
    )
  }

  if (isAdmin.value) {
    items.push(
      { path: '/admin', label: '数据概览', icon: Odometer },
      { path: '/admin/items', label: '物品管理', icon: Box },
      { path: '/admin/claims', label: '认领管理', icon: Document },
      { path: '/admin/announcements', label: '公告管理', icon: Document }
    )
  }

  return items
})

function goTo(path) {
  router.push(path)
}

function toggleSidebar() {
  isCollapsed.value = !isCollapsed.value
}

// 暴露给父组件
defineExpose({ isCollapsed, toggleSidebar })
</script>

<template>
  <div class="app-sidebar" :class="{ collapsed: isCollapsed }">
    <div class="sidebar-logo" @click="goTo('/')">
      <span style="font-size: 24px">📦</span>
      <span v-if="!isCollapsed" class="logo-text">失物招领</span>
    </div>

    <div class="sidebar-menu">
      <div
        v-for="item in menuItems"
        :key="item.path"
        class="menu-item"
        :class="{ active: route.path === item.path }"
        @click="goTo(item.path)"
      >
        <el-icon><component :is="item.icon" /></el-icon>
        <span v-if="!isCollapsed">{{ item.label }}</span>
      </div>
    </div>

    <!-- 收起/展开按钮 -->
    <div class="sidebar-toggle" @click="toggleSidebar">
      <el-icon v-if="isCollapsed"><ArrowRight /></el-icon>
      <el-icon v-else><ArrowLeft /></el-icon>
    </div>
  </div>
</template>

<style scoped>
.app-sidebar {
  width: 180px;
  height: 100vh;
  background: #fff;
  border-right: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
  transition: width 0.3s ease;
}

.app-sidebar.collapsed {
  width: 60px;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 20px 16px;
  cursor: pointer;
  border-bottom: 1px solid #ebeef5;
  min-height: 64px;
}

.logo-text {
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  white-space: nowrap;
}

.sidebar-menu {
  flex: 1;
  padding: 12px 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  margin: 4px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
  overflow: hidden;
}

.collapsed .menu-item {
  justify-content: center;
  padding: 12px;
  margin: 4px;
}

.menu-item:hover {
  background: #f0f2ff;
  color: #667eea;
}

.menu-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.menu-item .el-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.sidebar-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  cursor: pointer;
  border-top: 1px solid #ebeef5;
  color: #909399;
  transition: all 0.2s;
}

.sidebar-toggle:hover {
  background: #f5f7fa;
  color: #667eea;
}
</style>
