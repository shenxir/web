<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useItemsStore } from '@/stores/items.js'
import { getStatusInfo, getOwnerTypeLabel, getOwnerTypeTag, formatDate, daysUntilExpiry } from '@/utils/helpers.js'
import { ElMessage, ElTable, ElTableColumn, ElTag, ElButton, ElSelect, ElOption, ElInput, ElPopconfirm, ElEmpty } from 'element-plus'
import { Search, View, Bottom } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const itemsStore = useItemsStore()

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

const filters = ref({
  keyword: '',
  status: '',
  ownerType: ''
})

onMounted(async () => {
  await loadItems()
})

watch(filters, async () => {
  await loadItems()
}, { deep: true })

async function loadItems() {
  loading.value = true
  try {
    const params = {}
    if (filters.value.keyword) params.keyword = filters.value.keyword
    if (filters.value.status) params.status = filters.value.status
    if (filters.value.ownerType) params.ownerType = filters.value.ownerType

    const data = await api.get('/items', { params })
    items.value = Array.isArray(data) ? data : (data.data || [])
    items.value.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  } catch (e) {
    console.error('加载物品失败:', e)
  } finally {
    loading.value = false
  }
}

async function handleForceExpire(item) {
  await itemsStore.updateItemStatus(item.id, 'expired')
  ElMessage.success('已强制下架')
  await loadItems()
}

function goToDetail(item) {
  router.push(`/item/${item.id}`)
}

function getCategoryIcon(categoryIcon) {
  return categoryIcon || '📦'
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📦 物品管理</h2>
      <p>管理所有失物和拾物信息</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row mb-24">
      <div class="stat-item">
        <div class="stat-icon total">📊</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">全部物品</div>
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
        <div class="stat-icon expired">⏰</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.expired }}</div>
          <div class="stat-label">已过期</div>
        </div>
      </div>
    </div>

    <div class="content-card">
      <!-- 筛选 -->
      <div class="search-bar mb-16">
        <el-input
          v-model="filters.keyword"
          placeholder="搜索物品名称"
          :prefix-icon="Search"
          clearable
          style="flex: 1; min-width: 200px;"
        />
        <el-select v-model="filters.status" placeholder="状态" clearable style="width: 140px;">
          <el-option label="待认领/寻物中" value="pending" />
          <el-option label="已认领/已找到" value="claimed" />
          <el-option label="已过期" value="expired" />
        </el-select>
        <el-select v-model="filters.ownerType" placeholder="类型" clearable style="width: 120px;">
          <el-option label="失物" value="lost" />
          <el-option label="拾物" value="found" />
        </el-select>
      </div>

      <!-- 表格 -->
      <div v-if="loading" class="empty-state">
        <p>加载中...</p>
      </div>

      <el-empty v-else-if="items.length === 0" description="暂无物品数据" />

      <div v-else class="table-container">
        <el-table :data="items" stripe border style="width: 100%;">
          <!-- 类型列 -->
          <el-table-column label="类型" width="70" align="center">
            <template #default="{ row }">
              <el-tag :type="getOwnerTypeTag(row.ownerType)" size="small" effect="dark">
                {{ getOwnerTypeLabel(row.ownerType) }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 物品信息列 -->
          <el-table-column label="物品信息" min-width="150">
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

          <!-- 发布者 -->
          <el-table-column label="发布者" width="70" prop="userName" align="center" />

          <!-- 状态列 -->
          <el-table-column label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusInfo(row.status, row.ownerType).type" size="small">
                {{ getStatusInfo(row.status, row.ownerType).label }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 发布日期 -->
          <el-table-column label="发布时间" width="90" align="center">
            <template #default="{ row }">
              {{ formatDate(row.publishDate) }}
            </template>
          </el-table-column>

          <!-- 浏览量 -->
          <el-table-column label="浏览" width="50" prop="views" align="center" />

          <!-- 操作列 -->
          <el-table-column label="操作" width="160" fixed="right" align="center">
            <template #default="{ row }">
              <div class="action-cell">
                <el-button type="primary" size="small" :icon="View" @click="goToDetail(row)">
                  查看
                </el-button>
                <el-popconfirm
                  v-if="row.status === 'pending'"
                  title="确定要强制下架吗？"
                  @confirm="handleForceExpire(row)"
                >
                  <template #reference>
                    <el-button type="warning" size="small" :icon="Bottom" plain>下架</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 表格容器 */
.table-container {
  width: 100%;
  overflow-x: auto;
}

.table-container :deep(.el-table) {
  border-radius: 8px;
}

.table-container :deep(.el-table th) {
  background-color: #f5f7fa !important;
  font-weight: 600;
}

.table-container :deep(.el-table td) {
  padding: 8px 0;
}

.table-container :deep(.el-table .cell) {
  padding: 0 8px;
}

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
  min-width: 120px;
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
.stat-icon.expired { background: linear-gradient(135deg, #dfe6e9 0%, #b2bec3 100%); }

.stat-info { display: flex; flex-direction: column; }
.stat-value { font-size: 24px; font-weight: 700; color: #303133; }
.stat-label { font-size: 12px; color: #909399; }

/* 类型单元格 */
.type-cell { display: flex; align-items: center; gap: 6px; }
.type-icon { font-size: 16px; }

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

/* 状态单元格 */
.status-cell { display: flex; align-items: center; gap: 6px; }
.status-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.status-cell.pending .status-dot { background: #E6A23C; }
.status-cell.claimed .status-dot { background: #67C23A; }
.status-cell.expired .status-dot { background: #909399; }

/* 日期单元格 */
.date-cell { display: flex; align-items: center; gap: 6px; color: #606266; font-size: 13px; }
.date-icon { font-size: 14px; }

/* 浏览量 */
.views-cell { display: flex; align-items: center; gap: 4px; color: #606266; }

/* 操作单元格 */
.action-cell { display: flex; gap: 6px; }
</style>
