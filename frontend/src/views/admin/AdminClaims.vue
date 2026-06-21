<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useClaimsStore } from '@/stores/claims.js'
import { getClaimStatusInfo, formatDateTime } from '@/utils/helpers.js'
import { ElMessage, ElTable, ElTableColumn, ElTag, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElPopconfirm, ElEmpty } from 'element-plus'
import { View, Edit } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const claimsStore = useClaimsStore()

const claims = ref([])
const loading = ref(false)

// 统计数据
const stats = computed(() => ({
  total: claims.value.length,
  pending: claims.value.filter(c => c.status === 'pending').length,
  approved: claims.value.filter(c => c.status === 'approved').length,
  rejected: claims.value.filter(c => c.status === 'rejected').length
}))

const filters = ref({
  status: ''
})

onMounted(async () => {
  await loadClaims()
})

watch(filters, async () => {
  await loadClaims()
}, { deep: true })

async function loadClaims() {
  loading.value = true
  try {
    const params = {}
    if (filters.value.status) params.status = filters.value.status

    const data = await api.get('/claims', { params })
    claims.value = Array.isArray(data) ? data : (data.data || [])
    claims.value.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  } catch (e) {
    console.error('加载认领失败:', e)
  } finally {
    loading.value = false
  }
}

// 审核对话框
const reviewDialogVisible = ref(false)
const currentClaim = ref(null)
const reviewForm = ref({
  status: 'approved',
  adminNote: ''
})

function openReviewDialog(claim) {
  currentClaim.value = claim
  reviewForm.value = { status: 'approved', adminNote: '' }
  reviewDialogVisible.value = true
}

async function submitReview() {
  if (!reviewForm.value.adminNote.trim()) {
    ElMessage.warning('请填写审核备注')
    return
  }

  const result = await claimsStore.reviewClaim(currentClaim.value.id, {
    status: reviewForm.value.status,
    adminNote: reviewForm.value.adminNote
  })

  reviewDialogVisible.value = false

  if (result.success) {
    ElMessage.success(result.message)
    await loadClaims()
  } else {
    ElMessage.error(result.message)
  }
}

function goToItem(claim) {
  router.push(`/item/${claim.itemId}`)
}

function getStatusIcon(status) {
  const map = { pending: '⏳', approved: '✅', rejected: '❌', expired: '⏰' }
  return map[status] || '❓'
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>✅ 认领管理</h2>
      <p>审核和管理所有认领申请</p>
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
      <!-- 筛选 -->
      <div class="search-bar mb-16">
        <el-select v-model="filters.status" placeholder="审核状态" clearable style="width: 140px;">
          <el-option label="待审核" value="pending" />
          <el-option label="已通过" value="approved" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
      </div>

      <!-- 表格 -->
      <div v-if="loading" class="empty-state">
        <p>加载中...</p>
      </div>

      <el-empty v-else-if="claims.length === 0" description="暂无认领记录" />

      <el-table v-else :data="claims" style="width: 100%;" stripe>
        <!-- 认领物品 -->
        <el-table-column label="认领物品" min-width="200">
          <template #default="{ row }">
            <div class="item-info">
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

        <!-- 认领人 -->
        <el-table-column label="认领人" width="100" prop="claimerName" />

        <!-- 认领理由 -->
        <el-table-column label="认领理由" min-width="180" show-overflow-tooltip prop="reason" />

        <!-- 审核状态 -->
        <el-table-column label="审核状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getClaimStatusInfo(row.status).type" size="small">
              {{ getClaimStatusInfo(row.status).label }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 审核备注 -->
        <el-table-column label="审核备注" min-width="150" show-overflow-tooltip>
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

        <!-- 操作列 -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="action-cell">
              <el-button
                v-if="row.status === 'pending'"
                type="primary"
                size="small"
                :icon="Edit"
                @click="openReviewDialog(row)"
              >
                审核
              </el-button>
              <el-button
                v-else
                type="info"
                size="small"
                :icon="View"
                @click="openReviewDialog(row)"
              >
                查看
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <!-- 审核对话框 -->
  <el-dialog
    v-model="reviewDialogVisible"
    title="审核认领申请"
    width="500px"
    :close-on-click-modal="false"
  >
    <div v-if="currentClaim" class="claim-info-card">
      <div class="info-row">
        <span class="info-label">认领人：</span>
        <span class="info-value">{{ currentClaim.claimerName }}</span>
      </div>
      <div class="info-row">
        <span class="info-label">认领物品：</span>
        <span class="info-value">{{ currentClaim.itemTitle }}</span>
      </div>
      <div class="info-row">
        <span class="info-label">认领理由：</span>
        <span class="info-value">{{ currentClaim.reason }}</span>
      </div>
    </div>

    <el-form>
      <el-form-item label="审核结果" required>
        <el-radio-group v-model="reviewForm.status">
          <el-radio value="approved">✅ 通过</el-radio>
          <el-radio value="rejected">❌ 拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核备注" required>
        <el-input
          v-model="reviewForm.adminNote"
          type="textarea"
          :rows="3"
          placeholder="请填写审核备注..."
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="reviewDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitReview">
        {{ reviewForm.status === 'approved' ? '确认通过' : '确认拒绝' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
/* 统计卡片 */
.stats-row { display: flex; gap: 16px; flex-wrap: wrap; }
.stat-item {
  display: flex; align-items: center; gap: 12px;
  padding: 16px 20px; background: white; border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06); flex: 1; min-width: 120px;
}
.stat-icon {
  width: 48px; height: 48px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center; font-size: 24px;
}
.stat-icon.total { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.pending { background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%); }
.stat-icon.approved { background: linear-gradient(135deg, #55efc4 0%, #00b894 100%); }
.stat-icon.rejected { background: linear-gradient(135deg, #ff7675 0%, #d63031 100%); }
.stat-info { display: flex; flex-direction: column; }
.stat-value { font-size: 24px; font-weight: 700; color: #303133; }
.stat-label { font-size: 12px; color: #909399; }

/* 物品信息 */
.item-info { display: flex; align-items: center; gap: 12px; }
.item-thumb {
  width: 48px; height: 48px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; flex-shrink: 0; overflow: hidden;
}
.item-thumb.lost { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
.item-thumb.found { background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%); }
.item-thumb-img { width: 100%; height: 100%; object-fit: cover; }
.item-thumb-icon { font-size: 24px; }
.item-detail { display: flex; flex-direction: column; }
.item-title { font-weight: 600; color: #303133; cursor: pointer; }
.item-title:hover { color: #667eea; }
.item-meta { font-size: 12px; color: #909399; margin-top: 4px; }

/* 用户单元格 */
.user-cell { display: flex; align-items: center; gap: 4px; }
.user-icon { font-size: 14px; }

/* 认领理由 */
.reason-cell { display: flex; align-items: flex-start; gap: 8px; }
.reason-icon { font-size: 14px; flex-shrink: 0; margin-top: 2px; }
.reason-text { color: #606266; font-size: 13px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }

/* 状态单元格 */
.status-cell { display: flex; align-items: center; gap: 6px; }
.status-icon { font-size: 16px; }
.status-cell.pending .status-icon { animation: pulse 1.5s infinite; }
@keyframes pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }

/* 备注单元格 */
.note-cell { display: flex; align-items: flex-start; gap: 8px; }
.note-icon { font-size: 14px; flex-shrink: 0; margin-top: 2px; }
.note-text { color: #606266; font-size: 13px; }
.no-note { color: #C0C4CC; }

/* 时间单元格 */
.time-cell { display: flex; align-items: center; gap: 6px; color: #606266; font-size: 13px; }
.time-icon { font-size: 14px; }

/* 操作单元格 */
.action-cell { display: flex; gap: 6px; }

/* 认领信息卡片 */
.claim-info-card {
  background: #f5f7fa; padding: 16px; border-radius: 8px; margin-bottom: 16px;
}
.info-row { display: flex; padding: 6px 0; }
.info-label { width: 80px; color: #909399; flex-shrink: 0; }
.info-value { color: #303133; }
</style>
