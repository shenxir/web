<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useItemsStore } from '@/stores/items.js'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const itemsStore = useItemsStore()
const authStore = useAuthStore()

// 分类和地点数据
const categories = ref([])
const locations = ref([])

// 加载分类和地点
onMounted(async () => {
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
})

const form = reactive({
  ownerType: 'lost',
  title: '',
  description: '',
  categoryId: null,
  locationId: null,
  phone: authStore.currentUser?.phone || ''
})

// 图片相关
const fileList = ref([])
const imageUrls = ref([])
const uploading = ref(false)

const loading = ref(false)
const rules = {
  title: [{ required: true, message: '请输入物品标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入物品描述', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择物品分类', trigger: 'change' }],
  locationId: [{ required: true, message: '请选择丢失/拾到地点', trigger: 'change' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const formRef = ref(null)

// 图片上传前的校验
function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

// 自定义上传处理
async function customUpload(options) {
  const file = options.file
  uploading.value = true

  try {
    const formData = new FormData()
    formData.append('file', file)

    const res = await api.post('/items/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    const data = res.data || res

    if (data.success) {
      imageUrls.value.push(data.url)
      ElMessage.success('图片上传成功')
      options.onSuccess(data)
    } else {
      ElMessage.error(data.message || '上传失败')
      options.onError(new Error(data.message))
    }
  } catch (e) {
    ElMessage.error('上传失败')
    options.onError(e)
  } finally {
    uploading.value = false
  }
}

// 删除图片
function handleRemove(file) {
  // file.url 是 el-upload 组件设置的 URL
  // 或者从 response 中获取的 URL
  const url = file.url || file.response?.url
  if (url) {
    const index = imageUrls.value.findIndex(u => u === url)
    if (index > -1) {
      imageUrls.value.splice(index, 1)
    }
  }
}

async function handleSubmit() {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true

    const result = await itemsStore.publishItem({
      title: form.title,
      description: form.description,
      categoryId: form.categoryId,
      locationId: form.locationId,
      ownerType: form.ownerType,
      userId: authStore.currentUser.id,
      userName: authStore.getDisplayName(),
      phone: form.phone,
      images: imageUrls.value
    })

    loading.value = false

    if (result.success) {
      ElMessage.success(result.message)
      router.push('/items')
    } else {
      ElMessageBox.alert(result.message, '发布失败', {
        confirmButtonText: '确定',
        type: 'warning'
      })
    }
  })
}

function resetForm() {
  form.title = ''
  form.description = ''
  form.categoryId = null
  form.locationId = null
  form.phone = authStore.currentUser?.phone || ''
  fileList.value = []
  imageUrls.value = []
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📝 发布信息</h2>
      <p>发布失物招领或拾物信息</p>
    </div>

    <el-row :gutter="24">
      <el-col :xs="24" :md="16">
        <div class="content-card">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            size="large"
          >
            <!-- 信息类型 -->
            <el-form-item label="信息类型" prop="ownerType">
              <el-radio-group v-model="form.ownerType" size="large">
                <el-radio-button value="lost">
                  🔍 我丢了东西（发布失物）
                </el-radio-button>
                <el-radio-button value="found">
                  📦 我捡到东西（发布拾物）
                </el-radio-button>
              </el-radio-group>
            </el-form-item>

            <!-- 物品标题 -->
            <el-form-item label="物品标题" prop="title">
              <el-input
                v-model="form.title"
                placeholder="如：丢失黑色钱包、捡到蓝色书包"
                maxlength="15"
                show-word-limit
              />
            </el-form-item>

            <!-- 物品描述 -->
            <el-form-item label="详细描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="5"
                placeholder="请详细描述物品特征、丢失/拾到的时间地点等信息..."
                maxlength="1000"
                show-word-limit
              />
            </el-form-item>

            <!-- 物品图片 -->
            <el-form-item label="实物图片">
              <div class="upload-container">
                <el-upload
                  v-model:file-list="fileList"
                  class="image-uploader"
                  :http-request="customUpload"
                  :before-upload="beforeUpload"
                  :on-remove="handleRemove"
                  list-type="picture-card"
                  :limit="5"
                  accept="image/*"
                >
                  <Plus class="upload-icon" />
                  <template #tip>
                    <div class="upload-tip">最多上传5张图片，单张不超过5MB</div>
                  </template>
                </el-upload>
              </div>
            </el-form-item>

            <!-- 物品分类 -->
            <el-form-item label="物品分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择物品分类" style="width: 100%;">
                <el-option
                  v-for="cat in categories"
                  :key="cat.id"
                  :label="cat.icon + ' ' + cat.name"
                  :value="cat.id"
                />
              </el-select>
            </el-form-item>

            <!-- 相关地点 -->
            <el-form-item label="相关地点" prop="locationId">
              <el-select v-model="form.locationId" placeholder="请选择丢失/拾到地点" style="width: 100%;">
                <el-option
                  v-for="loc in locations"
                  :key="loc.id"
                  :label="loc.name"
                  :value="loc.id"
                />
              </el-select>
            </el-form-item>

            <!-- 联系电话 -->
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>

            <!-- 提交按钮 -->
            <el-form-item>
              <el-button type="primary" :loading="loading || uploading" @click="handleSubmit" size="large">
                {{ loading ? '发布中...' : (uploading ? '上传图片中...' : '发布信息') }}
              </el-button>
              <el-button @click="resetForm" size="large">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>

      <!-- 右侧提示 -->
      <el-col :xs="24" :md="8">
        <div class="content-card">
          <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 12px;">📋 发布须知</h3>
          <div style="font-size: 14px; color: #606266; line-height: 2;">
            <p>✅ 请如实填写物品信息</p>
            <p>✅ 描述越详细越有助于找回</p>
            <p>📸 建议上传物品实物照片</p>
            <p>✅ 请保持联系方式畅通</p>
            <p>⚠️ 同一物品24小时内请勿重复发布</p>
            <p>⏰ 失物信息有效期为30天</p>
            <p>⏰ 拾物信息有效期为15天</p>
            <p>🔒 管理员将审核您的发布信息</p>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.upload-container {
  width: 100%;
}

.image-uploader {
  width: 100%;
}

.image-uploader :deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
}

.image-uploader :deep(.el-upload-list__item) {
  width: 120px;
  height: 120px;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
