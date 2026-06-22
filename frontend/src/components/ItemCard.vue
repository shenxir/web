<script setup>
import { ref, computed, watch } from 'vue'
import { getStatusInfo, getOwnerTypeLabel, getOwnerTypeTag, formatDate, daysUntilExpiry } from '../utils/helpers.js'

const props = defineProps({
  item: { type: Object, required: true }
})

const emit = defineEmits(['click'])

// 图片预览状态
const previewVisible = ref(false)
const previewIndex = ref(0)

const statusInfo = computed(() => getStatusInfo(props.item.status, props.item.ownerType))
const typeLabel = computed(() => getOwnerTypeLabel(props.item.ownerType))
const typeTag = computed(() => getOwnerTypeTag(props.item.ownerType))
const remainingDays = computed(() => daysUntilExpiry(props.item.expiryDate))

// 分类图标
const categoryIcon = computed(() => {
  return props.item.categoryIcon || '📦'
})

// 检查是否有图片
const hasImage = computed(() => {
  return props.item.images && props.item.images.length > 0
})

// 获取第一张图片
const firstImage = computed(() => {
  if (hasImage.value) {
    return props.item.images[0].imageUrl
  }
  return null
})

// 获取所有图片URL列表（用于预览）
const imageList = computed(() => {
  if (hasImage.value) {
    return props.item.images.map(img => img.imageUrl)
  }
  return []
})

// 打开图片预览
function openPreview(index = 0) {
  if (hasImage.value) {
    previewIndex.value = index
    previewVisible.value = true
  }
}

// 关闭图片预览
function closePreview() {
  previewVisible.value = false
}

// 上一张图片
function prevImage() {
  if (previewIndex.value > 0) {
    previewIndex.value--
  } else {
    previewIndex.value = imageList.value.length - 1
  }
}

// 下一张图片
function nextImage() {
  if (previewIndex.value < imageList.value.length - 1) {
    previewIndex.value++
  } else {
    previewIndex.value = 0
  }
}

// 根据类型返回背景色
const bgColor = computed(() => {
  return props.item.ownerType === 'lost'
    ? 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)'
    : 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)'
})
</script>

<template>
  <el-card class="item-card" shadow="hover" @click="emit('click', item)">
    <div class="item-card-image" :style="{ background: bgColor }">
      <!-- 如果有图片则显示图片，否则显示分类图标 -->
      <img
        v-if="hasImage"
        :src="firstImage"
        class="item-thumbnail"
        @click.stop="openPreview(0)"
      />
      <span v-else>{{ categoryIcon }}</span>
      <!-- 图片预览组件 -->
      <teleport to="body">
        <div v-if="previewVisible" class="image-viewer-overlay" @click.self="closePreview">
          <div class="image-viewer-container">
            <div class="image-viewer-header">
              <span class="image-counter">{{ previewIndex + 1 }} / {{ imageList.length }}</span>
              <button class="close-btn" @click="closePreview">&times;</button>
            </div>
            <div class="image-viewer-body">
              <button v-if="imageList.length > 1" class="nav-btn prev-btn" @click="prevImage">&lt;</button>
              <img :src="imageList[previewIndex]" class="preview-image" />
              <button v-if="imageList.length > 1" class="nav-btn next-btn" @click="nextImage">&gt;</button>
            </div>
          </div>
        </div>
      </teleport>
      <el-tag class="type-badge" :type="typeTag" size="small" effect="dark">
        {{ typeLabel }}
      </el-tag>
      <el-tag
        class="status-badge"
        :type="statusInfo.type"
        size="small"
        effect="plain"
        style="position: absolute; top: 8px; right: 8px;"
      >
        {{ statusInfo.label }}
      </el-tag>
    </div>
    <div class="item-card-body">
      <div class="item-card-title">{{ item.title }}</div>
      <div class="item-card-desc">{{ item.description }}</div>
      <div class="item-card-meta">
        <span>📍 {{ item.locationName }}</span>
        <span>{{ formatDate(item.publishDate) }}</span>
      </div>
      <div class="item-card-meta mt-12">
        <span>👁 {{ item.views }}次浏览</span>
        <span v-if="item.status === 'pending' && remainingDays > 0" style="color: #E6A23C">
          剩余{{ remainingDays }}天
        </span>
        <span v-else-if="item.status === 'expired'" style="color: #909399">已过期</span>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.status-badge {
  position: absolute;
  top: 8px;
  right: 8px;
}

.item-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
  cursor: pointer;
}

.item-thumbnail:hover {
  opacity: 0.9;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  font-size: 48px;
}

/* 图片预览窗口样式 */
.image-viewer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.9);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-viewer-container {
  position: relative;
  max-width: 90vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.image-viewer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  color: white;
}

.image-counter {
  font-size: 14px;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 32px;
  cursor: pointer;
  padding: 0 10px;
  line-height: 1;
}

.close-btn:hover {
  color: #ccc;
}

.image-viewer-body {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image {
  max-width: 80vw;
  max-height: 80vh;
  object-fit: contain;
}

.nav-btn {
  position: absolute;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 24px;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 4px;
  z-index: 10;
}

.nav-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.prev-btn {
  left: 10px;
}

.next-btn {
  right: 10px;
}
</style>
