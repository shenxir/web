<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElTable, ElTableColumn, ElTag, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElPopconfirm, ElEmpty } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import api from '@/api'

const announcements = ref([])
const loading = ref(false)

// 统计数据
const stats = computed(() => ({
  total: announcements.value.length,
  active: announcements.value.filter(a => a.status === 1).length,
  inactive: announcements.value.filter(a => a.status === 0).length
}))

// 加载数据
onMounted(async () => {
  await loadAnnouncements()
})

async function loadAnnouncements() {
  loading.value = true
  try {
    const data = await api.get('/announcements')
    announcements.value = Array.isArray(data) ? data : (data.data || [])
  } catch (e) {
    console.error('加载公告失败:', e)
  } finally {
    loading.value = false
  }
}

// 对话框
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  title: '',
  content: '',
  type: 'normal',
  status: 1
})
const formRules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}
const formRef = ref(null)

function openAddDialog() {
  isEdit.value = false
  form.value = { id: null, title: '', content: '', type: 'normal', status: 1 }
  dialogVisible.value = true
}

function openEditDialog(announcement) {
  isEdit.value = true
  form.value = { ...announcement }
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      if (isEdit.value) {
        await api.put(`/announcements/${form.value.id}`, form.value)
        ElMessage.success('更新成功')
      } else {
        await api.post('/announcements', form.value)
        ElMessage.success('发布成功')
      }
      dialogVisible.value = false
      await loadAnnouncements()
    } catch (e) {
      ElMessage.error('操作失败')
    }
  })
}

async function handleDelete(announcement) {
  try {
    await api.delete(`/announcements/${announcement.id}`)
    ElMessage.success('删除成功')
    await loadAnnouncements()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

function getTypeLabel(type) {
  return type === 'important' ? '重要' : '普通'
}

function getTypeTag(type) {
  return type === 'important' ? 'danger' : 'info'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return dateStr.replace('T', ' ').substring(0, 16)
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-left">
        <h2>📢 公告管理</h2>
        <p>发布和管理系统公告</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">
        发布公告
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row mb-24">
      <div class="stat-item">
        <div class="stat-icon total">📊</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">全部公告</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon active">✅</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.active }}</div>
          <div class="stat-label">已发布</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon inactive">⏸️</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.inactive }}</div>
          <div class="stat-label">已下架</div>
        </div>
      </div>
    </div>

    <div class="content-card">
      <div v-if="loading" class="empty-state">
        <p>加载中...</p>
      </div>

      <el-empty v-else-if="announcements.length === 0" description="暂无公告" />

      <el-table v-else :data="announcements" style="width: 100%;" stripe>
        <!-- 标题 -->
        <el-table-column label="公告标题" min-width="200" prop="title" />

        <!-- 类型 -->
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)" size="small">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 状态 -->
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 内容预览 -->
        <el-table-column label="内容预览" min-width="250" show-overflow-tooltip prop="content" />

        <!-- 创建时间 -->
        <el-table-column label="发布时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>

        <!-- 操作 -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="action-cell">
              <el-button type="primary" size="small" :icon="Edit" @click="openEditDialog(row)">
                编辑
              </el-button>
              <el-popconfirm
                title="确定要删除该公告吗？"
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

  <!-- 添加/编辑对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑公告' : '发布公告'"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入公告标题" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="5"
          placeholder="请输入公告内容"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="form.type" style="width: 100%;">
          <el-option label="普通公告" value="normal" />
          <el-option label="重要公告" value="important" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        <span style="margin-left: 8px; color: #909399;">
          {{ form.status === 1 ? '发布' : '下架' }}
        </span>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ isEdit ? '保存' : '发布' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

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
.stat-icon.active { background: linear-gradient(135deg, #55efc4 0%, #00b894 100%); }
.stat-icon.inactive { background: linear-gradient(135deg, #dfe6e9 0%, #b2bec3 100%); }
.stat-info { display: flex; flex-direction: column; }
.stat-value { font-size: 24px; font-weight: 700; color: #303133; }
.stat-label { font-size: 12px; color: #909399; }

/* 标题单元格 */
.title-cell { display: flex; align-items: center; gap: 8px; }
.title-icon { font-size: 16px; }
.title-text { font-weight: 600; color: #303133; }

/* 内容预览 */
.content-preview {
  color: #606266; font-size: 13px;
  display: -webkit-box; -webkit-line-clamp: 2;
  -webkit-box-orient: vertical; overflow: hidden;
}

/* 时间单元格 */
.time-cell { display: flex; align-items: center; gap: 6px; color: #606266; font-size: 13px; }
.time-icon { font-size: 14px; }

/* 操作单元格 */
.action-cell { display: flex; gap: 6px; }
</style>
