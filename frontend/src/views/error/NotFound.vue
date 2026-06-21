<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <h1>页面不存在</h1>
      <p>抱歉，您访问的页面不存在或已被移除</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.not-found-content {
  text-align: center;
  color: white;
  padding: 40px;
}

.error-code {
  font-size: 150px;
  font-weight: bold;
  opacity: 0.8;
  line-height: 1;
  margin-bottom: 20px;
}

h1 {
  font-size: 36px;
  margin-bottom: 16px;
}

p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 32px;
}
</style>
