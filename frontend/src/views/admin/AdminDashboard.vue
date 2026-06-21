<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useItemsStore } from '@/stores/items.js'
import { useClaimsStore } from '@/stores/claims.js'
import { ElRow, ElCol } from 'element-plus'

const router = useRouter()
const itemsStore = useItemsStore()
const claimsStore = useClaimsStore()

const stats = ref({
  total: 0,
  pending: 0,
  claimed: 0,
  expired: 0,
  lost: 0,
  found: 0,
  pendingClaims: 0,
  totalClaims: 0
})

const recentClaims = ref([])

onMounted(async () => {
  // 加载数据
  await Promise.all([
    itemsStore.loadItems(),
    claimsStore.loadClaims()
  ])

  // 计算统计
  stats.value = {
    total: itemsStore.items.length,
    pending: itemsStore.pendingItems.length,
    claimed: itemsStore.claimedItems.length,
    expired: itemsStore.expiredItems.length,
    lost: itemsStore.lostItems.length,
    found: itemsStore.foundItems.length,
    pendingClaims: claimsStore.pendingClaims.length,
    totalClaims: claimsStore.claims.length
  }

  // 获取最近的待审核认领
  recentClaims.value = claimsStore.claims
    .filter(c => c.status === 'pending')
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 5)
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📊 数据概览</h2>
      <p>系统运行数据一览</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="mb-24">
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #667eea">{{ stats.total }}</div>
          <div class="stat-label">物品总数</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #E6A23C">{{ stats.pending }}</div>
          <div class="stat-label">待认领</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #67C23A">{{ stats.claimed }}</div>
          <div class="stat-label">已认领</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-number" style="color: #F56C6C">{{ stats.expired }}</div>
          <div class="stat-label">已过期</div>
        </div>
      </el-col>
    </el-row>

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
          <div class="stat-number" style="color: #909399">{{ stats.totalClaims }}</div>
          <div class="stat-label">认领总数</div>
        </div>
      </el-col>
    </el-row>

    <!-- 待处理认领 -->
    <div class="content-card">
      <div class="flex-between mb-16">
        <h3 style="font-size: 18px; font-weight: 600;">⏳ 待审核认领</h3>
        <el-button link type="primary" @click="router.push('/admin/claims')">查看全部 →</el-button>
      </div>

      <div v-if="recentClaims.length === 0" class="empty-state" style="padding: 30px;">
        <p>🎉 暂无待审核认领</p>
      </div>

      <div v-else>
        <div v-for="claim in recentClaims" :key="claim.id" class="claim-card">
          <div class="claim-header">
            <div>
              <strong>{{ claim.claimerName }}</strong> 申请认领
              <el-button link type="primary" @click="router.push(`/item/${claim.itemId}`)">
                「{{ claim.itemTitle }}」
              </el-button>
            </div>
            <el-tag type="warning" size="small">待审核</el-tag>
          </div>
          <div class="claim-info">
            <p>理由：{{ claim.reason }}</p>
            <p style="color: #909399; font-size: 12px;">
              提交时间：{{ claim.createdAt }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
