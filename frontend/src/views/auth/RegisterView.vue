<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage } from 'element-plus'
import { User, Lock, UserFilled, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: ''
})
const loading = ref(false)

async function handleRegister() {
  // 表单校验
  if (!form.username || !form.username.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (form.username.trim().length < 3 || form.username.trim().length > 20) {
    ElMessage.warning('用户名长度需在3-20个字符之间')
    return
  }
  if (!form.password) {
    ElMessage.warning('请输入密码')
    return
  }
  if (form.password.length < 6) {
    ElMessage.warning('密码长度不能少于6个字符')
    return
  }
  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  if (!form.realName || !form.realName.trim()) {
    ElMessage.warning('请输入真实姓名')
    return
  }

  loading.value = true

  const result = await authStore.register({
    username: form.username.trim(),
    password: form.password,
    realName: form.realName.trim(),
    phone: form.phone.trim() || undefined
  })

  loading.value = false

  if (result.success) {
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } else {
    ElMessage.error(result.message)
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">📦</div>
        <h1 class="login-title">用户注册</h1>
        <p class="login-subtitle">校园失物招领系统</p>
      </div>

      <el-form :model="form" @keyup.enter="handleRegister">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名（3-20个字符）"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码（不少于6个字符）"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.realName"
            placeholder="真实姓名（必填）"
            size="large"
            :prefix-icon="UserFilled"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.phone"
            placeholder="手机号（选填）"
            size="large"
            :prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            style="width: 100%"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p>
          已有账号？
          <router-link to="/login" style="color: #667eea; font-weight: 500;">去登录</router-link>
        </p>
      </div>
    </div>
  </div>
</template>
