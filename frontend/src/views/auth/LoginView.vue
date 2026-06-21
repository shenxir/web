<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: ''
})
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true

  const result = await authStore.login(form.username, form.password)
  loading.value = false

  if (result.success) {
    ElMessage.success(result.message)
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } else {
    ElMessage.error(result.message)
  }
}

function fillAdmin() {
  form.username = 'admin'
  form.password = 'admin123'
}

function fillUser() {
  form.username = 'zhangsan'
  form.password = '123456'
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">📦</div>
        <h1 class="login-title">校园失物招领系统</h1>
        <p class="login-subtitle">Campus Lost & Found System</p>
      </div>

      <el-form :model="form" @keyup.enter="handleLogin">
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
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
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
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <div class="demo-hint">
          <p><strong>演示账号</strong></p>
          <p>
            管理员：
            <el-button link type="primary" @click="fillAdmin">admin / admin123</el-button>
          </p>
          <p>
            普通用户：
            <el-button link type="primary" @click="fillUser">zhangsan / 123456</el-button>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
