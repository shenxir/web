<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

const isLoggedIn = computed(() => authStore.isLoggedIn)
const isAdmin = computed(() => authStore.isAdmin)
const userName = computed(() => authStore.getDisplayName())

function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  }).catch(() => {})
}
</script>

<template>
  <el-header class="app-navbar" height="60px">
    <div class="navbar-spacer"></div>

    <div class="navbar-right">
      <template v-if="isLoggedIn">
        <el-dropdown trigger="click">
          <span class="user-info">
            <el-avatar :size="32" style="background-color: #667eea">
              {{ userName.charAt(0) }}
            </el-avatar>
            <span class="user-name">{{ userName }}</span>
            <el-tag v-if="isAdmin" size="small" type="danger" style="margin-left: 6px">管理员</el-tag>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-if="!isAdmin" @click="router.push('/my/items')">我的发布</el-dropdown-item>
              <el-dropdown-item v-if="!isAdmin" @click="router.push('/my/claims')">我的认领</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" @click="router.push('/login')">
          <el-icon><User /></el-icon>
          登录
        </el-button>
      </template>
    </div>
  </el-header>
</template>

<style scoped>
.app-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 0 24px;
  z-index: 100;
}

.navbar-spacer {
  flex: 1;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f5f7fa;
}

.user-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}
</style>
