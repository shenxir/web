<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { getClaimStatusInfo, formatDateTime } from '@/utils/helpers.js'
import { ElTable, ElTableColumn, ElTag, ElButton, ElEmpty } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const authStore = useAuthStore()

const claims = ref([])
const loading = ref(false)

// 统计数据
const stats = computed(() => ({
  total: claims.value.length,
  pending: claims.value.filter(c => c.status === 'pending').length,
  approved: claims.value.filter(c => c.status === 'approved').length,
  rejected: claims.value.filter(c => c.status === 'rejected').length
}))

// 加载数据
onMounted(async () => {
  await loadClaims()
})

async function loadClaims() {
  if (!authStore.userId) return

  loading.value = true
  try {
    const data = await api.get('/claims/my', { params: { userId: authStore.userId } })
    claims.value = Array.isArray(data) ? data : (data.data || [])
  } catch (e) {
    console.error('加载认领失败:', e)
  } finally {
    loading.value = false
  }
}

function goToItem(claim) {
  router.push(`/item/${claim.itemId}`)
}

// 获取状态图标
function getStatusIcon(status) {
  const map = {
    pending: '⏳',
    approved: '✅',
    rejected: '❌',
    expired: '⏰'
  }
  return map[status] || '❓'
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📋 我的认领</h2>
      <p>查看我提交的认领申请记录</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row mb-24">
      <div class="stat-item">
        <div class="stat-icon total">📊</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">全部认领</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon pending">⏳</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.pending }}</div>
          <div class="stat-label">待审核</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon approved">✅</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.approved }}</div>
          <div class="stat-label">已通过</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon rejected">❌</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.rejected }}</div>
          <div class="stat-label">已拒绝</div>
        </div>
      </div>
    </div>

    <div class="content-card">
      <div v-if="loading" class="empty-state">
        <p>加载中...</p>
      </div>

      <div v-else-if="claims.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <p>您还没有提交过认领申请</p>
        <el-button type="primary" size="large" class="mt-16" @click="router.push('/items')">
          🔍 去浏览物品
        </el-button>
      </div>

      <el-table v-else :data="claims" style="width: 100%;" stripe>
        <!-- 物品信息列 -->
        <el-table-column label="认领物品" min-width="200">
          <template #default="{ row }">
            <div class="item-info">
              <!-- 如果有图片则显示图片，否则显示类型图标 -->
              <div class="item-thumb" :class="row.itemOwnerType">
                <img v-if="row.itemImages && row.itemImages.length > 0"
                     :src="row.itemImages[0].imageUrl"
                     class="item-thumb-img" />
                <span v-else class="item-thumb-icon">{{ row.itemOwnerType === 'lost' ? '🔍' : '📦' }}</span>
              </div>
              <div class="item-detail">
                <div class="item-title" @click="goToItem(row)">{{ row.itemTitle }}</div>
                <div class="item-meta">物品主人：{{ row.ownerName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 认领理由 -->
        <el-table-column label="认领理由" min-width="200" show-overflow-tooltip prop="reason" />

        <!-- 审核状态 -->
        <el-table-column label="审核状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getClaimStatusInfo(row.status).type" size="small">
              {{ getClaimStatusInfo(row.status).label }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 管理员备注 -->
        <el-table-column label="审核备注" min-width="160" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.adminNote || '-' }}
          </template>
        </el-table-column>

        <!-- 提交时间 -->
        <el-table-column label="提交时间" width="150">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>

        <!-- 审核时间 -->
        <el-table-column label="审核时间" width="150">
          <template #default="{ row }">
            {{ row.reviewedAt ? formatDateTime(row.reviewedAt) : '-' }}
          </template>
        </el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="View" @click="goToItem(row)">
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped>
/* 统计卡片 */
.stats-row {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  flex: 1;
  min-width: 140px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.total { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.pending { background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%); }
.stat-icon.approved { background: linear-gradient(135deg, #55efc4 0%, #00b894 100%); }
.stat-icon.rejected { background: linear-gradient(135deg, #ff7675 0%, #d63031 100%); }

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

/* 空状态 */
.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

/* 物品信息 */
.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-thumb {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
  overflow: hidden;
  position: relative;
}

.item-thumb.lost {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
}

.item-thumb.found {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
}

.item-thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-thumb-icon {
  font-size: 24px;
}

.item-detail {
  display: flex;
  flex-direction: column;
}

.item-title {
  font-weight: 600;
  color: #303133;
  cursor: pointer;
  transition: color 0.2s;
}

.item-title:hover {
  color: #667eea;
}

.item-meta {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 认领理由 */
.reason-cell {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.reason-icon {
  font-size: 14px;
  flex-shrink: 0;
  margin-top: 2px;
}

.reason-text {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 状态单元格 */
.status-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-icon {
  font-size: 16px;
}

.status-cell.pending .status-icon { animation: pulse 1.5s infinite; }
.status-cell.approved .status-icon { color: #67C23A; }
.status-cell.rejected .status-icon { color: #F56C6C; }

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 备注单元格 */
.note-cell {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.note-icon {
  font-size: 14px;
  flex-shrink: 0;
  margin-top: 2px;
}

.note-text {
  color: #606266;
  font-size: 13px;
}

.no-note {
  color: #C0C4CC;
}

/* 时间单元格 */
.time-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 13px;
}

.time-icon {
  font-size: 14px;
}
</style>
