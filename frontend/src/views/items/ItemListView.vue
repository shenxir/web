<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useItemsStore } from '@/stores/items.js'
import { ElRow, ElCol, ElInput, ElSelect, ElOption, ElTabs, ElTabPane, ElEmpty } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import ItemCard from '@/components/ItemCard.vue'
import api from '@/api'

const router = useRouter()
const route = useRoute()
const itemsStore = useItemsStore()

// 分类和地点数据
const categories = ref([])
const locations = ref([])
const loading = ref(false)
const itemList = ref([])

// 加载数据
onMounted(async () => {
  // 从 URL 参数初始化筛选
  if (route.query.ownerType) {
    filters.value.ownerType = route.query.ownerType
  }

  // 加载分类和地点
  try {
    const [catRes, locRes] = await Promise.all([
      api.get('/categories'),
      api.get('/locations')
    ])
    categories.value = catRes.data || catRes
    locations.value = locRes.data || locRes
  } catch (e) {
    console.error('加载数据失败:', e)
  }

  // 加载物品列表
  await loadItems()
})

const filters = ref({
  keyword: '',
  categoryId: null,
  locationId: null,
  status: 'pending',
  ownerType: ''
})

// 监听筛选条件变化
watch(filters, async () => {
  await loadItems()
}, { deep: true })

async function loadItems() {
  loading.value = true
  try {
    const params = {}
    if (filters.value.keyword) params.keyword = filters.value.keyword
    if (filters.value.categoryId) params.categoryId = filters.value.categoryId
    if (filters.value.locationId) params.locationId = filters.value.locationId
    if (filters.value.status) params.status = filters.value.status
    if (filters.value.ownerType) params.ownerType = filters.value.ownerType

    const res = await api.get('/items', { params })
    itemList.value = res.data || res
  } catch (e) {
    console.error('加载物品失败:', e)
  } finally {
    loading.value = false
  }
}

function goToItem(item) {
  router.push(`/item/${item.id}`)
}

function resetFilters() {
  filters.value = {
    keyword: '',
    categoryId: null,
    locationId: null,
    status: 'pending',
    ownerType: ''
  }
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>🔍 物品列表</h2>
      <p>浏览所有失物和拾物信息</p>
    </div>

    <!-- 搜索与筛选 -->
    <div class="content-card mb-24">
      <div class="search-bar">
        <el-input
          v-model="filters.keyword"
          placeholder="搜索物品名称或描述"
          :prefix-icon="Search"
          clearable
          style="flex: 1; min-width: 200px;"
        />
        <el-select v-model="filters.categoryId" placeholder="物品分类" clearable style="width: 140px;">
          <el-option
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.icon + ' ' + cat.name"
            :value="cat.id"
          />
        </el-select>
        <el-select v-model="filters.locationId" placeholder="丢失地点" clearable style="width: 140px;">
          <el-option
            v-for="loc in locations"
            :key="loc.id"
            :label="loc.name"
            :value="loc.id"
          />
        </el-select>
        <el-button @click="resetFilters">重置</el-button>
      </div>

      <!-- 标签页筛选 -->
      <el-tabs v-model="filters.status" class="mb-16">
        <el-tab-pane
          :label="filters.ownerType === 'found' ? '待认领' : '寻物中'"
          name="pending"
        />
        <el-tab-pane
          :label="filters.ownerType === 'found' ? '已认领' : '已找到'"
          name="claimed"
        />
        <el-tab-pane label="已过期" name="expired" />
        <el-tab-pane label="全部" name="" />
      </el-tabs>

      <!-- 类型快捷筛选 -->
      <div class="flex gap-8 mb-16">
        <el-button
          :type="filters.ownerType === '' ? 'primary' : ''"
          size="small"
          @click="filters.ownerType = ''"
        >
          全部
        </el-button>
        <el-button
          :type="filters.ownerType === 'lost' ? 'danger' : ''"
          size="small"
          @click="filters.ownerType = 'lost'"
        >
          🔍 失物
        </el-button>
        <el-button
          :type="filters.ownerType === 'found' ? 'success' : ''"
          size="small"
          @click="filters.ownerType = 'found'"
        >
          📦 拾物
        </el-button>
      </div>
    </div>

    <!-- 物品列表 -->
    <div v-if="loading" class="content-card">
      <div class="empty-state">
        <p>加载中...</p>
      </div>
    </div>

    <div v-else-if="itemList.length === 0" class="content-card">
      <el-empty description="暂无符合条件的物品" />
    </div>

    <el-row v-else :gutter="16">
      <el-col
        v-for="item in itemList"
        :key="item.id"
        :xs="24" :sm="12" :md="8" :lg="6"
        class="mb-16"
      >
        <ItemCard :item="item" @click="goToItem" />
      </el-col>
    </el-row>
  </div>
</template>
