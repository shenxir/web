<script setup>
import { ref, reactive } from 'vue'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: { type: Boolean, default: false }
})

const emit = defineEmits(['update:visible'])

const authStore = useAuthStore()

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const loading = ref(false)

function handleClose() {
  form.oldPassword = ''
  form.newPassword = ''
  form.confirmPassword = ''
  emit('update:visible', false)
}

async function handleSubmit() {
  if (!form.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }
  if (!form.newPassword) {
    ElMessage.warning('请输入新密码')
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
  const result = await authStore.changePassword(form.oldPassword, form.newPassword)
  loading.value = false

  if (result.success) {
    ElMessage.success(result.message)
    handleClose()
  } else {
    ElMessage.error(result.message)
  }
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    title="修改密码"
    width="400px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="原密码">
        <el-input
          v-model="form.oldPassword"
          type="password"
          placeholder="请输入原密码"
          show-password
        />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input
          v-model="form.newPassword"
          type="password"
          placeholder="不少于6个字符"
          show-password
        />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="请再次输入新密码"
          show-password
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        {{ loading ? '提交中...' : '确认修改' }}
      </el-button>
    </template>
  </el-dialog>
</template>
