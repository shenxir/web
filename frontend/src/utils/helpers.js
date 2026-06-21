// ==================== 日期与过期 ====================

/**
 * 获取今天的日期字符串 YYYY-MM-DD
 */
export function getToday() {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

/**
 * 计算过期日期
 * @param {string} publishDate - 发布日期 YYYY-MM-DD
 * @param {string} ownerType - lost 或 found
 * @returns {string} 过期日期 YYYY-MM-DD
 */
export function calcExpiryDate(publishDate, ownerType) {
  const days = ownerType === 'lost' ? 30 : 15
  const d = new Date(publishDate)
  d.setDate(d.getDate() + days)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

/**
 * 检查物品是否已过期
 */
export function isExpired(item) {
  return item.expiryDate < getToday()
}

/**
 * 自动过期处理：将过期物品状态改为 expired
 */
export function autoExpireItems(items) {
  const today = getToday()
  let changed = false
  items.forEach(item => {
    if (item.status === 'pending' && item.expiryDate < today) {
      item.status = 'expired'
      changed = true
    }
  })
  return changed
}

/**
 * 自动过期认领记录：超过7天未处理的认领自动过期
 */
export function autoExpireClaims(claims) {
  const today = getToday()
  let changed = false
  claims.forEach(claim => {
    if (claim.status === 'pending') {
      const created = new Date(claim.createdAt)
      const diffDays = Math.floor((new Date(today) - created) / (1000 * 60 * 60 * 24))
      if (diffDays > 7) {
        claim.status = 'expired'
        changed = true
      }
    }
  })
  return changed
}

// ==================== 重复发布校验 ====================

/**
 * 检查重复发布：同一用户24小时内不可发布相同标题的物品
 * @returns {object|null} 重复的物品，无重复返回 null
 */
export function checkDuplicate(userId, title, items) {
  const now = new Date()
  return items.find(item => {
    if (item.userId !== userId) return false
    if (item.title !== title) return false
    const diff = now - new Date(item.createdAt)
    return diff < 24 * 60 * 60 * 1000 // 24小时内
  }) || null
}

// ==================== 权限判断 ====================

/**
 * 检查是否可以认领该物品
 * @returns {object} { canClaim, reason }
 */
export function canClaimItem(item, currentUser) {
  if (!currentUser) {
    return { canClaim: false, reason: '请先登录后再进行认领' }
  }
  if (currentUser.role === 'admin') {
    return { canClaim: false, reason: '管理员不能参与认领' }
  }
  if (item.userId === currentUser.id) {
    return { canClaim: false, reason: '不能认领自己发布的物品' }
  }
  if (item.status !== 'pending') {
    return { canClaim: false, reason: '该物品当前不可认领' }
  }
  return { canClaim: true, reason: '' }
}

/**
 * 检查用户是否已对该物品提交过认领申请
 */
export function hasClaimed(itemId, userId, claims) {
  return claims.some(c => c.itemId === itemId && c.claimerId === userId && c.status !== 'rejected')
}

// ==================== 状态显示 ====================

/**
 * 获取物品状态标签（区分失物和拾物）
 * @param {string} status - 物品状态
 * @param {string} ownerType - 物品类型：lost-失物，found-拾物
 */
export function getStatusInfo(status, ownerType = 'lost') {
  if (ownerType === 'lost') {
    const map = {
      pending: { label: '寻物中', type: 'warning', color: '#E6A23C' },
      claimed: { label: '已找到', type: 'success', color: '#67C23A' },
      expired: { label: '已过期', type: 'info', color: '#909399' }
    }
    return map[status] || { label: '未知', type: 'info', color: '#909399' }
  } else {
    const map = {
      pending: { label: '待认领', type: 'warning', color: '#E6A23C' },
      claimed: { label: '已认领', type: 'success', color: '#67C23A' },
      expired: { label: '已过期', type: 'info', color: '#909399' }
    }
    return map[status] || { label: '未知', type: 'info', color: '#909399' }
  }
}

/**
 * 获取认领状态标签
 */
export function getClaimStatusInfo(status) {
  const map = {
    pending: { label: '待审核', type: 'warning' },
    approved: { label: '已通过', type: 'success' },
    rejected: { label: '已拒绝', type: 'danger' },
    expired: { label: '已过期', type: 'info' }
  }
  return map[status] || { label: '未知', type: 'info' }
}

/**
 * 获取物品类型标签
 */
export function getOwnerTypeLabel(ownerType) {
  return ownerType === 'lost' ? '失物' : '拾物'
}

/**
 * 获取物品类型标签样式
 */
export function getOwnerTypeTag(ownerType) {
  return ownerType === 'lost' ? 'danger' : 'success'
}

// ==================== 格式化 ====================

/**
 * 格式化日期显示
 */
export function formatDate(dateStr) {
  if (!dateStr) return '-'
  return dateStr.split('T')[0]
}

/**
 * 格式化日期时间
 */
export function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

/**
 * 计算剩余天数
 */
export function daysUntilExpiry(expiryDate) {
  const today = new Date(getToday())
  const expiry = new Date(expiryDate)
  const diff = Math.ceil((expiry - today) / (1000 * 60 * 60 * 24))
  return diff
}
