<template>
  <div class="drivers-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Coordinate /></el-icon>
        司机管理
      </h1>
      <p class="page-description">管理系统中的所有司机信息，包括司机档案、证件信息和工作状态</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索司机姓名或电话"
          @keyup.enter="handleSearch"
          clearable
          style="width: 300px;"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchForm.status"
          placeholder="工作状态"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="空闲" value="available" />
          <el-option label="忙碌" value="busy" />
          <el-option label="休假" value="off_duty" />
          <el-option label="停用" value="inactive" />
        </el-select>
        <el-select
          v-model="searchForm.licenseType"
          placeholder="驾照类型"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="A2" value="A2" />
          <el-option label="B2" value="B2" />
          <el-option label="C1" value="C1" />
        </el-select>
        <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
        <el-button @click="resetSearch" :icon="Refresh">重置</el-button>
      </div>
      
      <div class="button-section">
        <el-button type="primary" @click="handleAdd" :icon="Plus">
          新增司机
        </el-button>
        <el-button @click="exportData" :icon="Download">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        height="600"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="司机姓名" width="120" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="licenseNumber" label="驾照号码" width="140" />
        <el-table-column prop="licenseType" label="驾照类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getLicenseTypeColor(row.licenseType)"
              size="small"
            >
              {{ row.licenseType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="experienceYears" label="驾龄" width="80">
          <template #default="{ row }">
            {{ row.experienceYears }}年
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="100">
          <template #default="{ row }">
            <el-rate
              v-model="row.rating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.status)"
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hireDate" label="入职日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.hireDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              :icon="Edit"
            >
              编辑
            </el-button>
            <el-dropdown @command="(command) => handleAction(command, row)">
              <el-button size="small" type="info">
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">查看详情</el-dropdown-item>
                  <el-dropdown-item command="tasks">任务记录</el-dropdown-item>
                  <el-dropdown-item command="status">状态管理</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="司机姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入司机姓名" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="驾照号码" prop="licenseNumber">
              <el-input v-model="formData.licenseNumber" placeholder="请输入驾照号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="驾照类型" prop="licenseType">
              <el-select v-model="formData.licenseType" placeholder="请选择">
                <el-option label="A2" value="A2" />
                <el-option label="B2" value="B2" />
                <el-option label="C1" value="C1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="驾龄" prop="experienceYears">
              <el-input-number
                v-model="formData.experienceYears"
                :min="0"
                :max="50"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="hireDate">
              <el-date-picker
                v-model="formData.hireDate"
                type="date"
                placeholder="选择入职日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input
            v-model="formData.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评分" prop="rating">
              <el-rate
                v-model="formData.rating"
                :max="5"
                allow-half
                show-text
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio value="available">空闲</el-radio>
                <el-radio value="busy">忙碌</el-radio>
                <el-radio value="off_duty">休假</el-radio>
                <el-radio value="inactive">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Coordinate, Search, Refresh, Plus, Download, Edit, ArrowDown
} from '@element-plus/icons-vue'
import { driverApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  status: '',
  licenseType: ''
})

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 表单数据
const formData = reactive({
  id: null,
  name: '',
  phone: '',
  email: '',
  licenseNumber: '',
  licenseType: 'B2',
  experienceYears: 0,
  hireDate: '',
  address: '',
  rating: 0,
  status: 'available'
})

// 表单引用
const formRef = ref()

// 表单验证规则
const formRules = {
  name: [{ required: true, message: '请输入司机姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  licenseNumber: [{ required: true, message: '请输入驾照号码', trigger: 'blur' }],
  licenseType: [{ required: true, message: '请选择驾照类型', trigger: 'change' }],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})

// 获取司机数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      keyword: searchForm.keyword,
      status: searchForm.status,
      licenseType: searchForm.licenseType
    }
    const response = await driverApi.getDrivers(params)
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取司机数据失败:', error)
    ElMessage.error('获取司机数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: '',
    licenseType: ''
  })
  handleSearch()
}

// 新增司机
const handleAdd = () => {
  dialogTitle.value = '新增司机'
  resetFormData()
  dialogVisible.value = true
}

// 编辑司机
const handleEdit = (row) => {
  dialogTitle.value = '编辑司机'
  // 正确映射后端驼峰字段到前端下划线字段
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    phone: row.phone,
    email: row.email,
    licenseNumber: row.licenseNumber,
    licenseType: row.licenseType,
    experienceYears: row.experienceYears || 0,
    hireDate: row.hireDate,
    address: row.address,
    rating: row.rating || 0,
    status: row.status
  })
  dialogVisible.value = true
}

// 处理操作
const handleAction = async (command, row) => {
  switch (command) {
    case 'view':
      ElMessage.info('查看详情功能开发中')
      break
    case 'tasks':
      ElMessage.info('任务记录功能开发中')
      break
    case 'status':
      ElMessage.info('状态管理功能开发中')
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

// 删除司机
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除司机"${row.name}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await driverApi.deleteDriver(row.id)
    ElMessage.success('删除成功')
    await fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 表单提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // 将前端下划线字段映射到后端驼峰字段
    const driverData = {
      id: formData.id,
      name: formData.name,
      phone: formData.phone,
      email: formData.email,
      licenseNumber: formData.licenseNumber,
      licenseType: formData.licenseType,
      experienceYears: parseInt(formData.experienceYears) || 0,
      hireDate: formData.hireDate,
      address: formData.address,
      rating: parseFloat(formData.rating) || 0,
      status: formData.status
    }
    
    await driverApi.saveDriver(driverData)
    ElMessage.success(formData.id ? '更新成功' : '创建成功')
    
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    console.error('保存司机失败:', error)
    ElMessage.error('操作失败: ' + (error.message || '网络错误'))
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleClose = (done) => {
  ElMessageBox.confirm('确定关闭？未保存的数据将丢失')
    .then(() => done())
    .catch(() => {})
}

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    phone: '',
    email: '',
    licenseNumber: '',
    licenseType: 'B2',
    experienceYears: 0,
    hireDate: '',
    address: '',
    rating: 0,
    status: 'available'
  })
}

// 分页相关
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchData()
}

// 选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 导出数据
const exportData = () => {
  ElMessage.info('导出功能开发中')
}

// 工具函数
const getLicenseTypeColor = (type) => {
  const colors = { A2: 'danger', B2: 'warning', C1: 'success' }
  return colors[type] || 'info'
}

const getStatusType = (status) => {
  const types = {
    available: 'success',
    busy: 'warning',
    off_duty: 'info',
    inactive: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    available: '空闲',
    busy: '忙碌',
    off_duty: '休假',
    inactive: '停用'
  }
  return texts[status] || status
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}
</script>

<style scoped>
.drivers-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  color: white;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.page-title {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-description {
  margin: 10px 0 0 0;
  font-size: 16px;
  opacity: 0.9;
}

.action-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-section {
  display: flex;
  align-items: center;
}

.button-section {
  display: flex;
  gap: 10px;
}

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  margin-top: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  color: #333;
  font-weight: 600;
}

:deep(.el-button--small) {
  padding: 5px 8px;
  font-size: 12px;
}

:deep(.el-rate) {
  height: 20px;
  line-height: 1;
}
</style> 