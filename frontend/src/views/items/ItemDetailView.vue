<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { getStatusInfo, getOwnerTypeLabel, getOwnerTypeTag, formatDate, daysUntilExpiry, canClaimItem } from '@/utils/helpers.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const item = ref(null)
const loading = ref(true)

// 是否是物品发布者本人
const isOwner = computed(() => {
  return authStore.isLoggedIn && item.value && authStore.userId === item.value.userId
})

// 加载物品详情
onMounted(async () => {
  await loadItem()
})

async function loadItem() {
  loading.value = true
  try {
    const data = await api.get(`/items/${route.params.id}`)
    item.value = data
  } catch (e) {
    console.error('加载物品详情失败:', e)
    ElMessage.error('加载物品详情失败')
  } finally {
    loading.value = false
  }
}

function getStatusInfoLocal(status, ownerType) {
  return getStatusInfo(status, ownerType)
}

function getOwnerTypeLabelLocal(ownerType) {
  return getOwnerTypeLabel(ownerType)
}

function getOwnerTypeTagLocal(ownerType) {
  return getOwnerTypeTag(ownerType)
}

function getRemainingDays(expiryDate) {
  return daysUntilExpiry(expiryDate)
}

function getCategoryIcon(categoryIcon) {
  return categoryIcon || '📦'
}

// 认领对话框
const claimDialogVisible = ref(false)
const claimForm = ref({ reason: '' })
const claimLoading = ref(false)

function openClaimDialog() {
  if (!authStore.isLoggedIn) {
    ElMessageBox.confirm('请先登录后再进行认领', '提示', {
      confirmButtonText: '去登录',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.push({ name: 'Login', query: { redirect: route.fullPath } })
    }).catch(() => {})
    return
  }

  if (isOwner.value) {
    ElMessage.warning('不能认领自己发布的物品')
    return
  }

  if (item.value.status !== 'pending') {
    ElMessage.warning('该物品当前不可认领')
    return
  }

  claimForm.value.reason = ''
  claimDialogVisible.value = true
}

async function submitClaim() {
  if (!claimForm.value.reason.trim()) {
    ElMessage.warning('请填写认领理由')
    return
  }

  claimLoading.value = true
  try {
    const data = await api.post('/claims', {
      itemId: item.value.id,
      claimerId: authStore.currentUser.id,
      reason: claimForm.value.reason
    })

    claimDialogVisible.value = false

    if (data.success) {
      ElMessage.success(data.message)
    } else {
      ElMessage.error(data.message)
    }
  } catch (e) {
    ElMessage.error('提交认领失败')
  } finally {
    claimLoading.value = false
  }
}

function goBack() {
  router.back()
}

// 管理员操作：下架物品
async function handleForceExpire() {
  try {
    await api.put(`/items/${item.value.id}/status`, { status: 'expired' })
    ElMessage.success('已强制下架')
    await loadItem()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}
</script>

<template>
  <div class="page-container">
    <!-- 加载中 -->
    <div v-if="loading" class="content-card">
      <div class="empty-state">
        <p>加载中...</p>
      </div>
    </div>

    <!-- 物品不存在 -->
    <div v-else-if="!item" class="content-card">
      <el-empty description="物品不存在或已删除">
        <el-button type="primary" @click="goBack">返回列表</el-button>
      </el-empty>
    </div>

    <!-- 物品详情 -->
    <template v-else>
      <!-- 返回按钮 -->
      <div class="mb-16">
        <el-button @click="goBack">← 返回</el-button>
      </div>

      <el-row :gutter="24">
        <!-- 左侧：物品信息 -->
        <el-col :xs="24" :md="16">
          <div class="content-card mb-24">
            <!-- 标题区 -->
            <div class="flex-between mb-16">
              <div>
                <h2 style="font-size: 24px; font-weight: 600; margin-bottom: 8px;">
                  {{ getCategoryIcon(item.categoryIcon) }} {{ item.title }}
                </h2>
                <div class="flex gap-8">
                  <el-tag :type="getOwnerTypeTagLocal(item.ownerType)" effect="dark" size="small">
                    {{ getOwnerTypeLabelLocal(item.ownerType) }}
                  </el-tag>
                  <el-tag :type="getStatusInfoLocal(item.status, item.ownerType).type" effect="plain" size="small">
                    {{ getStatusInfoLocal(item.status, item.ownerType).label }}
                  </el-tag>
                  <el-tag size="small">{{ item.categoryName }}</el-tag>
                </div>
              </div>
            </div>

            <!-- 图片展示 -->
            <div v-if="item.images && item.images.length > 0" class="detail-section">
              <h3>📸 实物图片</h3>
              <div class="image-list">
                <el-image
                  v-for="(img, index) in item.images"
                  :key="img.id || index"
                  :src="img.imageUrl"
                  :preview-src-list="item.images.map(i => i.imageUrl)"
                  :initial-index="index"
                  class="item-image"
                  fit="cover"
                />
              </div>
            </div>

            <!-- 详情信息 -->
            <div class="detail-section">
              <h3>📝 详细描述</h3>
              <p style="line-height: 1.8; color: #606266;">{{ item.description }}</p>
            </div>

            <div class="detail-section">
              <h3>📋 基本信息</h3>
              <div class="info-row">
                <span class="info-label">发布日期</span>
                <span class="info-value">{{ formatDate(item.publishDate) }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">过期日期</span>
                <span class="info-value">
                  {{ formatDate(item.expiryDate) }}
                  <span v-if="item.status === 'pending' && getRemainingDays(item.expiryDate) > 0" style="color: #E6A23C; margin-left: 8px;">
                    （剩余 {{ getRemainingDays(item.expiryDate) }} 天）
                  </span>
                  <span v-else-if="item.status === 'expired'" style="color: #F56C6C; margin-left: 8px;">
                    已过期
                  </span>
                </span>
              </div>
              <div class="info-row">
                <span class="info-label">相关地点</span>
                <span class="info-value">📍 {{ item.locationName }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">浏览次数</span>
                <span class="info-value">👁 {{ item.views }} 次</span>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：联系方式 + 操作 -->
        <el-col :xs="24" :md="8">
          <!-- 联系信息 -->
          <div class="content-card mb-16">
            <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 12px;">📞 联系信息</h3>
            <div class="info-row">
              <span class="info-label">发布者</span>
              <span class="info-value">{{ item.userName }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">联系电话</span>
              <span class="info-value">{{ item.phone }}</span>
            </div>
          </div>

          <!-- 操作区 -->
          <div class="content-card">
            <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px;">⚡ 操作</h3>

            <div class="action-buttons">
              <!-- 管理员操作 -->
              <template v-if="authStore.isAdmin">
                <el-popconfirm
                  v-if="item.status === 'pending'"
                  title="确定要下架该物品吗？"
                  @confirm="handleForceExpire"
                >
                  <template #reference>
                    <el-button type="warning" class="action-btn">
                      ⬇️ 强制下架
                    </el-button>
                  </template>
                </el-popconfirm>
              </template>

              <!-- 普通用户操作 -->
              <template v-else>
                <template v-if="item.status === 'pending' && !isOwner">
                  <el-button
                    type="primary"
                    class="action-btn"
                    @click="openClaimDialog"
                  >
                    {{ item.ownerType === 'lost' ? '🙋 申请认领' : '🙋 申请招领' }}
                  </el-button>
                </template>

                <template v-if="item.status === 'pending' && isOwner">
                  <el-result
                    icon="info"
                    title="这是您发布的物品"
                    sub-title="其他人可以申请认领此物品"
                  />
                </template>

                <template v-else-if="item.status === 'claimed'">
                  <el-result
                    icon="success"
                    :title="item.ownerType === 'lost' ? '该物品已找到' : '该物品已被认领'"
                    sub-title="感谢您的关注"
                  />
                </template>

                <template v-else-if="item.status === 'expired'">
                  <el-result
                    icon="info"
                    title="该物品已过期"
                    sub-title="信息已自动下架"
                  />
                </template>
              </template>
            </div>
          </div>
        </el-col>
      </el-row>
    </template>

    <!-- 认领对话框 -->
    <el-dialog
      v-model="claimDialogVisible"
      :title="item?.ownerType === 'lost' ? '申请认领' : '申请招领'"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="mb-16" style="background: #f5f7fa; padding: 12px; border-radius: 8px;">
        <p style="font-weight: 600; margin-bottom: 4px;">{{ item?.ownerType === 'lost' ? '认领物品' : '招领物品' }}：{{ item?.title }}</p>
        <p style="font-size: 13px; color: #909399;">请详细描述{{ item?.ownerType === 'lost' ? '认领' : '招领' }}理由，以便管理员审核</p>
      </div>
      <el-form>
        <el-form-item :label="item?.ownerType === 'lost' ? '认领理由' : '招领说明'" required>
          <el-input
            v-model="claimForm.reason"
            type="textarea"
            :rows="4"
            :placeholder="item?.ownerType === 'lost' ? '请详细描述认领理由，如物品特征、丢失时间地点等...' : '请详细描述招领说明，如拾到物品的时间地点、物品特征等...'"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="claimLoading" @click="submitClaim">
          {{ claimLoading ? '提交中...' : (item?.ownerType === 'lost' ? '提交认领' : '提交招领') }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-btn {
  width: 100%;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 12px;
}

.item-image {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s;
}

.item-image:hover {
  transform: scale(1.05);
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
}

.info-row {
  display: flex;
  padding: 8px 0;
  border-bottom: 1px dashed #ebeef5;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  width: 80px;
  color: #909399;
  flex-shrink: 0;
}

.info-value {
  color: #303133;
}
</style>
