import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useItemsStore = defineStore('items', () => {
  const items = ref([])
  const loading = ref(false)

  // ===== 查询 =====
  const pendingItems = computed(() => items.value.filter(i => i.status === 'pending'))
  const claimedItems = computed(() => items.value.filter(i => i.status === 'claimed'))
  const expiredItems = computed(() => items.value.filter(i => i.status === 'expired'))
  const lostItems = computed(() => items.value.filter(i => i.ownerType === 'lost'))
  const foundItems = computed(() => items.value.filter(i => i.ownerType === 'found'))

  // 从后端加载所有物品
  async function loadItems() {
    loading.value = true
    try {
      const data = await api.get('/items/all')
      items.value = Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('加载物品失败:', e)
      items.value = []
    } finally {
      loading.value = false
    }
  }

  // 自动过期
  async function runAutoExpire() {
    await loadItems()
  }

  // 获取物品详情
  async function getItemById(id) {
    try {
      const data = await api.get(`/items/${id}`)
      return data
    } catch (e) {
      console.error('获取物品详情失败:', e)
      return null
    }
  }

  // 获取用户物品
  async function getUserItems(userId) {
    try {
      const data = await api.get(`/items/user/${userId}`)
      return Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('获取用户物品失败:', e)
      return []
    }
  }

  // 搜索物品
  async function searchItems({ keyword = '', categoryId = '', locationId = '', status = '', ownerType = '' } = {}) {
    try {
      const params = {}
      if (keyword) params.keyword = keyword
      if (categoryId) params.categoryId = categoryId
      if (locationId) params.locationId = locationId
      if (status) params.status = status
      if (ownerType) params.ownerType = ownerType

      const data = await api.get('/items', { params })
      return Array.isArray(data) ? data : (data.data || [])
    } catch (e) {
      console.error('搜索物品失败:', e)
      return []
    }
  }

  // 发布物品
  async function publishItem({ title, description, categoryId, locationId, ownerType, userId, userName, phone, images = [] }) {
    try {
      const data = await api.post('/items', {
        title,
        description,
        categoryId,
        locationId,
        ownerType,
        userId,
        userName,
        phone,
        images
      })
      return data
    } catch (e) {
      console.error('发布物品失败:', e)
      return { success: false, message: '发布失败' }
    }
  }

  // 删除物品
  async function deleteItem(itemId) {
    try {
      const data = await api.delete(`/items/${itemId}`)
      return data
    } catch (e) {
      console.error('删除物品失败:', e)
      return { success: false, message: '删除失败' }
    }
  }

  // 更新物品状态
  async function updateItemStatus(itemId, status) {
    try {
      const data = await api.put(`/items/${itemId}/status`, { status })
      return data
    } catch (e) {
      console.error('更新物品状态失败:', e)
      return { success: false, message: '更新失败' }
    }
  }

  return {
    items,
    loading,
    pendingItems,
    claimedItems,
    expiredItems,
    lostItems,
    foundItems,
    loadItems,
    runAutoExpire,
    getItemById,
    getUserItems,
    searchItems,
    publishItem,
    deleteItem,
    updateItemStatus
  }
})
