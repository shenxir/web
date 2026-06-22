<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const loading = ref(false)

async function handleChangePassword() {
  if (!form.username || !form.oldPassword || !form.newPassword || !form.confirmPassword) {
    ElMessage.warning('请填写所有字段')
    return
  }

  if (form.newPassword.length < 6) {
    ElMessage.warning('新密码长度不能少于6个字符')
    return
  }

  if (form.newPassword !== form.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }

  if (form.oldPassword === form.newPassword) {
    ElMessage.warning('新密码不能与原密码相同')
    return
  }

  loading.value = true
  try {
    // 先登录验证旧密码
    const loginResult = await api.post('/auth/login', {
      username: form.username,
      password: form.oldPassword
    })

    if (!loginResult.success) {
      ElMessage.error('用户名或原密码错误')
      loading.value = false
      return
    }

    // 修改密码
    const result = await api.post('/auth/password', {
      userId: loginResult.user.id,
      oldPassword: form.oldPassword,
      newPassword: form.newPassword
    })

    if (result.success) {
      ElMessage.success('密码修改成功，请重新登录')
      authStore.logout()
      router.push('/login')
    } else {
      ElMessage.error(result.message)
    }
  } catch (e) {
    ElMessage.error('修改密码失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">🔐</div>
        <h1 class="login-title">修改密码</h1>
        <p class="login-subtitle">Change Password</p>
      </div>

      <el-form :model="form" @keyup.enter="handleChangePassword">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.oldPassword"
            type="password"
            placeholder="请输入原密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码（至少6位）"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            style="width: 100%"
            @click="handleChangePassword"
          >
            {{ loading ? '修改中...' : '确认修改' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p>
          <router-link to="/login" style="color: #667eea; font-weight: 500;">返回登录</router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-logo {
  font-size: 48px;
  margin-bottom: 16px;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.login-subtitle {
  color: #909399;
  font-size: 14px;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
}
</style>
