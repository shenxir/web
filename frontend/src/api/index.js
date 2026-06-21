import axios from 'axios'
import { getItem, removeItem } from '@/utils/storage'
import { ElMessage } from 'element-plus'
import router from '@/router'

/**
 * axios 实例配置
 */
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 请求拦截器
 */
api.interceptors.request.use(
  (config) => {
    const token = getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 后端直接返回数据，不包装 { code, data } 格式
 */
api.interceptors.response.use(
  (response) => {
    // 后端直接返回数据，直接返回 response.data
    return response.data
  },
  (error) => {
    console.error('API请求错误:', error)

    if (error.response) {
      const { status, data } = error.response
      const message = data?.message || '请求失败'

      switch (status) {
        case 401:
          removeItem('token')
          removeItem('currentUser')
          router.push({
            path: '/login',
            query: { redirect: router.currentRoute.value.fullPath }
          })
          ElMessage.error('登录已过期，请重新登录')
          break
        case 403:
          ElMessage.error('没有权限执行此操作')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(message)
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请稍后重试')
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }

    return Promise.reject(error)
  }
)

export default api
