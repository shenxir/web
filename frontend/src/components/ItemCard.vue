<script setup>
import { computed } from 'vue'
import { getStatusInfo, getOwnerTypeLabel, getOwnerTypeTag, formatDate, daysUntilExpiry } from '../utils/helpers.js'

const props = defineProps({
  item: { type: Object, required: true }
})

const emit = defineEmits(['click'])

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
      <img v-if="hasImage" :src="firstImage" class="item-thumbnail" />
      <span v-else>{{ categoryIcon }}</span>
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
}
</style>
