<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useItemsStore } from '@/stores/items.js'
import { useClaimsStore } from '@/stores/claims.js'
import { useAuthStore } from '@/stores/auth.js'
import { getStatusInfo } from '@/utils/helpers.js'
import { ElRow, ElCol } from 'element-plus'
import ItemCard from '@/components/ItemCard.vue'
import api from '@/api'

const router = useRouter()
const itemsStore = useItemsStore()
const claimsStore = useClaimsStore()
const authStore = useAuthStore()

const announcements = ref([])
const expandedAnnouncement = ref(null)

onMounted(async () => {
  // 加载数据
  await Promise.all([
    itemsStore.loadItems(),
    claimsStore.loadClaims(),
    loadAnnouncements()
  ])
})

async function loadAnnouncements() {
  try {
    const res = await api.get('/announcements')
    announcements.value = res.data || res
  } catch (e) {
    console.error('加载公告失败:', e)
  }
}

function toggleAnnouncement(ann) {
  expandedAnnouncement.value = expandedAnnouncement.value === ann.id ? null : ann.id
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

const stats = computed(() => ({
  total: itemsStore.items.length,
  pending: itemsStore.pendingItems.length,
  claimed: itemsStore.claimedItems.length,
  expired: itemsStore.expiredItems.length,
  lost: itemsStore.lostItems.length,
  found: itemsStore.foundItems.length,
  pendingClaims: claimsStore.pendingClaims.length
}))

const latestItems = computed(() => {
  return itemsStore.items
    .filter(i => i.status === 'pending')
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 6)
})

function goToItem(item) {
  router.push(`/item/${item.id}`)
}
</script>

<template>
  <div class="page-container">
    <!-- 欢迎横幅 + 快捷操作 -->
    <div class="welcome-banner content-card mb-24">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>👋 欢迎使用校园失物招领系统</h2>
          <p>在这里发布失物/拾物信息，帮助校园物品互助寻回</p>
        </div>
        <div class="quick-actions">
          <el-button type="primary" size="large" @click="router.push('/publish')">
            📝 发布失物
          </el-button>
          <el-button type="success" size="large" @click="router.push('/publish')">
            📦 发布拾物
          </el-button>
          <el-button size="large" @click="router.push('/items')">
            🔍 浏览物品
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计数据 -->
    <el-row :gutter="16" class="mb-24">
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #F56C6C">{{ stats.lost }}</div>
          <div class="stat-label">失物数量</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #67C23A">{{ stats.found }}</div>
          <div class="stat-label">拾物数量</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #E6A23C">{{ stats.pendingClaims }}</div>
          <div class="stat-label">待审核认领</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #909399">{{ stats.expired }}</div>
          <div class="stat-label">已过期物品</div>
        </div>
      </el-col>
    </el-row>

    <!-- 系统公告 -->
    <div class="content-card mb-24">
      <div class="section-header">
        <h3 style="font-size: 18px; font-weight: 600;">📢 系统公告</h3>
      </div>
      <div v-if="announcements.length === 0" class="empty-state" style="padding: 20px;">
        <p>暂无公告</p>
      </div>
      <div v-else class="announcement-list">
        <div
          v-for="ann in announcements"
          :key="ann.id"
          class="announcement-item"
          :class="{ expanded: expandedAnnouncement === ann.id }"
          @click="toggleAnnouncement(ann)"
        >
          <div class="announcement-dot" :class="ann.type"></div>
          <div class="announcement-content">
            <div class="announcement-header">
              <span class="announcement-title">{{ ann.title }}</span>
              <span class="announcement-type" :class="ann.type">
                {{ ann.type === 'important' ? '重要' : '普通' }}
              </span>
            </div>
            <div class="announcement-date">{{ formatDate(ann.createdAt) }}</div>
            <div v-if="expandedAnnouncement === ann.id" class="announcement-detail">
              <p>{{ ann.content }}</p>
            </div>
          </div>
          <div class="announcement-arrow">
            {{ expandedAnnouncement === ann.id ? '▼' : '▶' }}
          </div>
        </div>
      </div>
    </div>

    <!-- 最新发布 -->
    <div class="content-card">
      <div class="section-header">
        <h3 style="font-size: 18px; font-weight: 600;">📋 最新发布</h3>
        <el-button link type="primary" @click="router.push('/items')">查看全部 →</el-button>
      </div>

      <div v-if="latestItems.length === 0" class="empty-state">
        <div class="icon">📭</div>
        <p>暂无待认领物品</p>
      </div>

      <el-row v-else :gutter="16">
        <el-col
          v-for="item in latestItems"
          :key="item.id"
          :xs="24" :sm="12" :md="8"
          class="mb-16"
        >
          <ItemCard :item="item" @click="goToItem" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.welcome-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.welcome-text h2 {
  font-size: 28px;
  margin-bottom: 8px;
  color: #fff;
}

.welcome-text p {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.quick-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.announcement-item {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
}

.announcement-item:hover {
  background: #ecf5ff;
}

.announcement-item.expanded {
  background: #f0f2ff;
}

.announcement-dot {
  width: 10px;
  height: 10px;
  background: #667eea;
  border-radius: 50%;
  margin-right: 12px;
  margin-top: 4px;
  flex-shrink: 0;
}

.announcement-dot.important {
  background: #F56C6C;
}

.announcement-content {
  flex: 1;
}

.announcement-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.announcement-title {
  font-size: 15px;
  color: #303133;
  font-weight: 600;
}

.announcement-type {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 500;
}

.announcement-type.normal {
  background: #e8f4fd;
  color: #409EFF;
}

.announcement-type.important {
  background: #fef0f0;
  color: #F56C6C;
}

.announcement-date {
  font-size: 12px;
  color: #909399;
}

.announcement-detail {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed #dcdfe6;
}

.announcement-detail p {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  margin: 0;
}

.announcement-arrow {
  color: #909399;
  font-size: 12px;
  margin-left: 12px;
  margin-top: 4px;
  transition: transform 0.3s;
  flex-shrink: 0;
}
</style>
