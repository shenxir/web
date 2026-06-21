import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useClaimsStore = defineStore('claims', () => {
  const claims = ref([])
  const loading = ref(false)

  // ===== 查询 =====
  const pendingClaims = computed(() => claims.value.filter(c => c.status === 'pending'))

  // 加载所有认领
  async function loadClaims() {
    loading.value = true
    try {
      const data = await api.get('/claims')
      claims.value = Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('加载认领失败:', e)
      claims.value = []
    } finally {
      loading.value = false
    }
  }

  // 自动过期
  async function runAutoExpire() {
    await loadClaims()
  }

  // 获取用户认领
  async function getUserClaims(userId) {
    try {
      const data = await api.get('/claims/my', { params: { userId } })
      return Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('获取用户认领失败:', e)
      return []
    }
  }

  // 获取物品认领
  async function getItemClaims(itemId) {
    try {
      const data = await api.get('/claims', { params: { itemId } })
      return Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('获取物品认领失败:', e)
      return []
    }
  }

  // 获取认领详情
  async function getClaimById(id) {
    try {
      const data = await api.get(`/claims/${id}`)
      return data
    } catch (e) {
      console.error('获取认领详情失败:', e)
      return null
    }
  }

  // 提交认领
  async function submitClaim({ itemId, claimerId, reason }) {
    try {
      const data = await api.post('/claims', { itemId, claimerId, reason })
      return data
    } catch (e) {
      console.error('提交认领失败:', e)
      return { success: false, message: '提交失败' }
    }
  }

  // 审核认领
  async function reviewClaim(claimId, { status, adminNote, reviewedBy }) {
    try {
      const data = await api.put(`/claims/${claimId}/review`, { status, adminNote, reviewedBy })
      return data
    } catch (e) {
      console.error('审核认领失败:', e)
      return { success: false, message: '审核失败' }
    }
  }

  // 删除认领
  async function deleteClaim(claimId) {
    try {
      const data = await api.delete(`/claims/${claimId}`)
      return data
    } catch (e) {
      console.error('删除认领失败:', e)
      return { success: false, message: '删除失败' }
    }
  }

  return {
    claims,
    loading,
    pendingClaims,
    loadClaims,
    runAutoExpire,
    getUserClaims,
    getItemClaims,
    getClaimById,
    submitClaim,
    reviewClaim,
    deleteClaim
  }
})
