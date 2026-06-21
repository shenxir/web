<script setup>
import { useRoute } from 'vue-router'
import { Odometer, Box, Document } from '@element-plus/icons-vue'

const route = useRoute()
</script>

<template>
  <el-aside width="220px" class="admin-sidebar">
    <div class="sidebar-header">
      <span>🔧</span>
      <span>管理后台</span>
    </div>
    <el-menu
      :default-active="route.path"
      router
      class="sidebar-menu"
    >
      <el-menu-item index="/admin">
        <el-icon><Odometer /></el-icon>
        <span>数据概览</span>
      </el-menu-item>
      <el-menu-item index="/admin/items">
        <el-icon><Box /></el-icon>
        <span>物品管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/claims">
        <el-icon><Document /></el-icon>
        <span>认领管理</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style scoped>
.admin-sidebar {
  background: #fff;
  border-right: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
}

.sidebar-menu {
  border-right: none;
  flex: 1;
}
</style>
