import api from './index'

/**
 * 获取物品列表
 * @param {object} params - { keyword, categoryId, locationId, status, ownerType, userId }
 * @returns {Promise}
 */
export function getItems(params) {
  return api.get('/items', { params })
}

/**
 * 获取物品详情
 * @param {number|string} id - 物品ID
 * @returns {Promise}
 */
export function getItemById(id) {
  return api.get(`/items/${id}`)
}

/**
 * 发布物品
 * @param {object} data - 物品信息
 * @returns {Promise}
 */
export function publishItem(data) {
  return api.post('/items', data)
}

/**
 * 上传图片
 * @param {File} file - 图片文件
 * @returns {Promise}
 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return api.post('/items/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除物品
 * @param {number|string} id - 物品ID
 * @returns {Promise}
 */
export function deleteItem(id) {
  return api.delete(`/items/${id}`)
}

/**
 * 更新物品状态
 * @param {number|string} id - 物品ID
 * @param {string} status - 新状态
 * @returns {Promise}
 */
export function updateItemStatus(id, status) {
  return api.put(`/items/${id}/status`, { status })
}

/**
 * 获取用户的物品列表
 * @param {number|string} userId - 用户ID
 * @returns {Promise}
 */
export function getUserItems(userId) {
  return api.get(`/items/user/${userId}`)
}

/**
 * 获取物品统计信息
 * @returns {Promise}
 */
export function getItemStats() {
  return api.get('/items/stats')
}

export default {
  getItems,
  getItemById,
  publishItem,
  uploadImage,
  deleteItem,
  updateItemStatus,
  getUserItems,
  getItemStats
}
