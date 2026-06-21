import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'
import { getItem, setItem, removeItem } from '@/utils/storage'

export const useAuthStore = defineStore('auth', () => {
  // 初始化时从 sessionStorage 恢复登录状态
  const currentUser = ref(getItem('currentUser'))

  const isLoggedIn = computed(() => currentUser.value !== null)
  const isAdmin = computed(() => currentUser.value?.role === 'admin')
  const userId = computed(() => currentUser.value?.id)

  /**
   * 登录
   */
  async function login(username, password) {
    try {
      const data = await api.post('/auth/login', { username, password })

      if (data.success) {
        currentUser.value = data.user
        setItem('currentUser', data.user)
        return { success: true, message: data.message }
      } else {
        return { success: false, message: data.message }
      }
    } catch (e) {
      console.error('登录失败:', e)
      return { success: false, message: '登录失败，请重试' }
    }
  }

  /**
   * 登出
   */
  function logout() {
    currentUser.value = null
    removeItem('currentUser')
  }

  /**
   * 获取用户显示名
   */
  function getDisplayName() {
    return currentUser.value?.realName || currentUser.value?.username || '未登录'
  }

  return {
    currentUser,
    isLoggedIn,
    isAdmin,
    userId,
    login,
    logout,
    getDisplayName
  }
})
