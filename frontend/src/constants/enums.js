/**
 * 物品状态枚举
 */
export const ItemStatus = {
  PENDING: 'pending',
  CLAIMED: 'claimed',
  EXPIRED: 'expired'
}

/**
 * 认领状态枚举
 */
export const ClaimStatus = {
  PENDING: 'pending',
  APPROVED: 'approved',
  REJECTED: 'rejected',
  EXPIRED: 'expired'
}

/**
 * 物品类型枚举
 */
export const ItemType = {
  LOST: 'lost',
  FOUND: 'found'
}

/**
 * 用户角色枚举
 */
export const UserRole = {
  USER: 'user',
  ADMIN: 'admin'
}

/**
 * 物品状态标签映射（区分失物和拾物）
 */
export const ItemStatusMap = {
  [ItemStatus.PENDING]: { label: '待认领', type: 'warning' },
  [ItemStatus.CLAIMED]: { label: '已认领', type: 'success' },
  [ItemStatus.EXPIRED]: { label: '已过期', type: 'info' }
}

/**
 * 失物状态标签映射
 */
export const LostItemStatusMap = {
  [ItemStatus.PENDING]: { label: '寻物中', type: 'warning' },
  [ItemStatus.CLAIMED]: { label: '已找到', type: 'success' },
  [ItemStatus.EXPIRED]: { label: '已过期', type: 'info' }
}

/**
 * 拾物状态标签映射
 */
export const FoundItemStatusMap = {
  [ItemStatus.PENDING]: { label: '待认领', type: 'warning' },
  [ItemStatus.CLAIMED]: { label: '已认领', type: 'success' },
  [ItemStatus.EXPIRED]: { label: '已过期', type: 'info' }
}

/**
 * 认领状态标签映射
 */
export const ClaimStatusMap = {
  [ClaimStatus.PENDING]: { label: '待审核', type: 'warning' },
  [ClaimStatus.APPROVED]: { label: '已通过', type: 'success' },
  [ClaimStatus.REJECTED]: { label: '已拒绝', type: 'danger' },
  [ClaimStatus.EXPIRED]: { label: '已过期', type: 'info' }
}

/**
 * 物品类型标签映射
 */
export const ItemTypeMap = {
  [ItemType.LOST]: { label: '失物', type: 'danger' },
  [ItemType.FOUND]: { label: '拾物', type: 'success' }
}

/**
 * 用户角色标签映射
 */
export const UserRoleMap = {
  [UserRole.USER]: { label: '普通用户', type: 'info' },
  [UserRole.ADMIN]: { label: '管理员', type: 'danger' }
}

/**
 * 过期天数配置
 */
export const ExpiryConfig = {
  LOST_DAYS: 30,
  FOUND_DAYS: 15,
  CLAIM_DAYS: 7
}
