<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { getStatusInfo, getOwnerTypeLabel, getOwnerTypeTag, formatDate } from '@/utils/helpers.js'
import { ElMessage, ElTable, ElTableColumn, ElTag, ElButton, ElPopconfirm, ElEmpty } from 'element-plus'
import { View, Delete } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const authStore = useAuthStore()

const items = ref([])
const loading = ref(false)

// 统计数据
const stats = computed(() => ({
  total: items.value.length,
  lost: items.value.filter(i => i.ownerType === 'lost').length,
  found: items.value.filter(i => i.ownerType === 'found').length,
  pending: items.value.filter(i => i.status === 'pending').length,
  claimed: items.value.filter(i => i.status === 'claimed').length,
  expired: items.value.filter(i => i.status === 'expired').length
}))

// 加载数据
onMounted(async () => {
  await loadItems()
})

async function loadItems() {
  if (!authStore.userId) return

  loading.value = true
  try {
    const data = await api.get(`/items/user/${authStore.userId}`)
    items.value = Array.isArray(data) ? data : (data.data || [])
  } catch (e) {
    console.error('加载物品失败:', e)
  } finally {
    loading.value = false
  }
}

function goToDetail(item) {
  router.push(`/item/${item.id}`)
}

async function handleDelete(item) {
  await api.delete(`/items/${item.id}`)
  ElMessage.success('已删除')
  await loadItems()
}

// 获取分类图标
function getCategoryIcon(categoryIcon) {
  return categoryIcon || '📦'
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📋 我的发布</h2>
      <p>管理我发布的失物和拾物信息</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row mb-24">
      <div class="stat-item">
        <div class="stat-icon total">📊</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">全部发布</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon lost">🔍</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.lost }}</div>
          <div class="stat-label">失物</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon found">📦</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.found }}</div>
          <div class="stat-label">拾物</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon pending">⏳</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.pending }}</div>
          <div class="stat-label">待认领</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon claimed">✅</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.claimed }}</div>
          <div class="stat-label">已认领</div>
        </div>
      </div>
    </div>

    <div class="content-card">
      <div v-if="loading" class="empty-state">
        <p>加载中...</p>
      </div>

      <div v-else-if="items.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <p>您还没有发布过任何信息</p>
        <el-button type="primary" size="large" class="mt-16" @click="router.push('/publish')">
          去发布
        </el-button>
      </div>

      <el-table v-else :data="items" style="width: 100%;" stripe>
        <!-- 物品信息列 -->
        <el-table-column label="物品信息" min-width="220">
          <template #default="{ row }">
            <div class="item-info">
              <div class="item-thumb" :class="row.ownerType">
                <img v-if="row.images && row.images.length > 0"
                     :src="row.images[0].imageUrl"
                     class="item-thumb-img" />
                <span v-else class="item-thumb-icon">{{ getCategoryIcon(row.categoryIcon) }}</span>
              </div>
              <div class="item-detail">
                <div class="item-title" @click="goToDetail(row)">{{ row.title }}</div>
                <div class="item-meta">{{ row.categoryName }} · {{ row.locationName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 状态列 -->
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusInfo(row.status, row.ownerType).type" size="small">
              {{ getStatusInfo(row.status, row.ownerType).label }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 发布日期 -->
        <el-table-column label="发布时间" width="110">
          <template #default="{ row }">
            {{ formatDate(row.publishDate) }}
          </template>
        </el-table-column>

        <!-- 浏览量 -->
        <el-table-column label="浏览" width="70" prop="views" />

        <!-- 操作列 -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="action-cell">
              <el-button type="primary" size="small" :icon="View" @click="goToDetail(row)">
                查看
              </el-button>
              <el-popconfirm
                title="确定要删除这条发布吗？"
                confirm-button-text="确定"
                cancel-button-text="取消"
                @confirm="handleDelete(row)"
              >
                <template #reference>
                  <el-button type="danger" size="small" :icon="Delete" plain>
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
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
.stat-icon.lost { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
.stat-icon.found { background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%); }
.stat-icon.pending { background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%); }
.stat-icon.claimed { background: linear-gradient(135deg, #55efc4 0%, #00b894 100%); }

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

/* 操作单元格 */
.action-cell {
  display: flex;
  gap: 8px;
}
</style>
