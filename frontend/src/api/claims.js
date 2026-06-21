import api from './index'

/**
 * 获取认领列表
 * @param {object} params - { page, pageSize, status, itemId }
 * @returns {Promise}
 */
export function getClaims(params) {
  return api.get('/claims', { params })
}

/**
 * 获取认领详情
 * @param {number|string} id - 认领ID
 * @returns {Promise}
 */
export function getClaimById(id) {
  return api.get(`/claims/${id}`)
}

/**
 * 提交认领申请
 * @param {object} data - { itemId, description, contact }
 * @returns {Promise}
 */
export function submitClaim(data) {
  return api.post('/claims', data)
}

/**
 * 审核认领（管理员）
 * @param {number|string} id - 认领ID
 * @param {object} data - { status, reviewNote }
 * @returns {Promise}
 */
export function reviewClaim(id, data) {
  return api.put(`/claims/${id}/review`, data)
}

/**
 * 删除认领
 * @param {number|string} id - 认领ID
 * @returns {Promise}
 */
export function deleteClaim(id) {
  return api.delete(`/claims/${id}`)
}

/**
 * 获取用户的认领列表
 * @param {number|string} userId - 用户ID
 * @param {object} params - { page, pageSize, status }
 * @returns {Promise}
 */
export function getUserClaims(userId, params) {
  return api.get(`/claims/user/${userId}`, { params })
}

/**
 * 获取物品的认领列表
 * @param {number|string} itemId - 物品ID
 * @param {object} params - { page, pageSize }
 * @returns {Promise}
 */
export function getItemClaims(itemId, params) {
  return api.get(`/claims/item/${itemId}`, { params })
}

/**
 * 批量审核认领（管理员）
 * @param {number[]} ids - 认领ID列表
 * @param {object} data - { status, reviewNote }
 * @returns {Promise}
 */
export function batchReviewClaims(ids, data) {
  return api.patch('/claims/batch/review', { ids, ...data })
}

export default {
  getClaims,
  getClaimById,
  submitClaim,
  reviewClaim,
  deleteClaim,
  getUserClaims,
  getItemClaims,
  batchReviewClaims
}
