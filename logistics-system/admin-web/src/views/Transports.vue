<template>
  <div class="transports-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Van /></el-icon>
        运输管理
      </h1>
      <p class="page-description">管理系统中的所有运输任务，包括运输计划、进度跟踪和费用管理</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索运输单号或订单号"
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
          placeholder="运输状态"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="计划中" value="planned" />
          <el-option label="进行中" value="in_progress" />
          <el-option label="已完成" value="completed" />
          <el-option label="已取消" value="cancelled" />
          <el-option label="延迟" value="delayed" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 200px; margin-left: 10px;"
        />
        <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
        <el-button @click="resetSearch" :icon="Refresh">重置</el-button>
      </div>
      
      <div class="button-section">
        <el-button type="primary" @click="handleAdd" :icon="Plus">
          新增运输
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
        <el-table-column prop="transportNumber" label="运输单号" width="160">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.transportNumber }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="关联订单" width="140">
          <template #default="{ row }">
            <el-tag v-if="row.orderNumber" type="info" size="small">
              {{ row.orderNumber }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="车辆/司机" width="160">
          <template #default="{ row }">
            <div class="transport-info">
              <div class="vehicle" v-if="row.vehicleLicensePlate">
                <el-icon><Van /></el-icon>
                {{ row.vehicleLicensePlate }}
              </div>
              <div class="driver" v-if="row.driverName">
                <el-icon><User /></el-icon>
                {{ row.driverName }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="距离/时长" width="120">
          <template #default="{ row }">
            <div class="distance-info">
              <div>{{ row.distance }}km</div>
              <div class="duration">{{ formatDuration(row.estimatedDuration) }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="费用" width="100">
          <template #default="{ row }">
            <span class="cost">¥{{ row.totalCost }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="transportStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.transportStatus)"
              size="small"
            >
              {{ getStatusText(row.transportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="160">
          <template #default="{ row }">
            <span v-if="row.endTime">{{ formatDateTime(row.endTime) }}</span>
            <el-text v-else type="info">未完成</el-text>
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
                  <el-dropdown-item command="track">运输跟踪</el-dropdown-item>
                  <el-dropdown-item command="status">状态更新</el-dropdown-item>
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
      width="800px"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运输单号" prop="transportNumber">
              <el-input 
                v-model="formData.transportNumber" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联订单" prop="orderId">
              <el-select
                v-model="formData.orderId"
                placeholder="选择订单"
                filterable
                clearable
              >
                <el-option
                  v-for="order in orderOptions"
                  :key="order.id"
                  :label="order.orderNumber"
                  :value="order.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="车辆" prop="vehicleId">
              <el-select
                v-model="formData.vehicleId"
                placeholder="选择车辆"
                filterable
                clearable
              >
                <el-option
                  v-for="vehicle in vehicleOptions"
                  :key="vehicle.id"
                  :label="`${vehicle.licensePlate} (${vehicle.brand} ${vehicle.model})`"
                  :value="vehicle.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="司机" prop="driverId">
              <el-select
                v-model="formData.driverId"
                placeholder="选择司机"
                filterable
                clearable
              >
                <el-option
                  v-for="driver in driverOptions"
                  :key="driver.id"
                  :label="driver.name"
                  :value="driver.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="formData.startTime"
                type="datetime"
                placeholder="选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计时长(分钟)" prop="estimatedDuration">
              <el-input-number
                v-model="formData.estimatedDuration"
                :min="0"
                :max="10080"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="距离(km)" prop="distance">
              <el-input-number
                v-model="formData.distance"
                :min="0"
                :max="10000"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="transportStatus">
              <el-radio-group v-model="formData.transportStatus">
                <el-radio value="planned">计划中</el-radio>
                <el-radio value="in_progress">进行中</el-radio>
                <el-radio value="completed">已完成</el-radio>
                <el-radio value="cancelled">已取消</el-radio>
                <el-radio value="delayed">延迟</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="燃油费" prop="fuelCost">
              <el-input-number
                v-model="formData.fuelCost"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="过路费" prop="tollCost">
              <el-input-number
                v-model="formData.tollCost"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="其他费用" prop="otherCosts">
              <el-input-number
                v-model="formData.otherCosts"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="formData.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Refresh, Plus, Download, Edit, ArrowDown, Van, User
} from '@element-plus/icons-vue'
import { transportApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])
const orderOptions = ref([])
const vehicleOptions = ref([])
const driverOptions = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  status: '',
  dateRange: [],
  transportType: ''
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
  transportNumber: '',
  orderId: null,
  vehicleId: null,
  driverId: null,
  startTime: '',
  estimatedDuration: 0,
  distance: 0,
  fuelCost: 0,
  tollCost: 0,
  otherCosts: 0,
  transportStatus: 'planned',
  notes: ''
})

// 计算总费用
const totalCost = computed(() => {
  return (formData.fuelCost || 0) + (formData.tollCost || 0) + (formData.otherCosts || 0)
})

// 表单引用
const formRef = ref()

// 表单验证规则
const formRules = {
  orderId: [{ required: true, message: '请选择关联订单', trigger: 'change' }],
  vehicleId: [{ required: true, message: '请选择车辆', trigger: 'change' }],
  driverId: [{ required: true, message: '请选择司机', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  distance: [{ required: true, message: '请输入距离', trigger: 'blur' }]
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
  fetchOptions()
})

// 获取运输数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      keyword: searchForm.keyword,
      status: searchForm.status,
      transportType: searchForm.transportType
    }
    const response = await transportApi.getTransports(params)
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取运输数据失败:', error)
    ElMessage.error('获取运输数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 获取选项数据
const fetchOptions = async () => {
  try {
    const [ordersRes, vehiclesRes, driversRes] = await Promise.all([
      transportApi.getOrdersByPage({ page: 1, size: 100 }),
      transportApi.getVehiclesByPage({ page: 1, size: 100 }),
      transportApi.getDriversByPage({ page: 1, size: 100 })
    ])
    
    orderOptions.value = ordersRes.data.records || []
    vehicleOptions.value = vehiclesRes.data.records || []
    driverOptions.value = driversRes.data.records || []
  } catch (error) {
    console.error('获取选项数据失败', error)
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
    dateRange: [],
    transportType: ''
  })
  handleSearch()
}

// 新增运输
const handleAdd = () => {
  dialogTitle.value = '新增运输'
  resetFormData()
  generateTransportNumber()
  dialogVisible.value = true
}

// 编辑运输
const handleEdit = (row) => {
  dialogTitle.value = '编辑运输'
  // 正确映射后端驼峰字段到前端下划线字段
  Object.assign(formData, {
    id: row.id,
    transportNumber: row.transportNumber,
    orderId: row.orderId,
    vehicleId: row.vehicleId,
    driverId: row.driverId,
    startTime: row.startTime,
    estimatedDuration: row.estimatedDuration || 0,
    distance: row.distance || 0,
    fuelCost: row.fuelCost || 0,
    tollCost: row.tollCost || 0,
    otherCosts: row.otherCosts || 0,
    transportStatus: row.transportStatus,
    notes: row.notes || ''
  })
  dialogVisible.value = true
}

// 查看详情
const handleViewDetail = (row) => {
  ElMessage.info('查看详情功能开发中')
}

// 处理操作
const handleAction = async (command, row) => {
  switch (command) {
    case 'view':
      handleViewDetail(row)
      break
    case 'track':
      ElMessage.info('运输跟踪功能开发中')
      break
    case 'status':
      ElMessage.info('状态更新功能开发中')
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

// 删除运输
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除运输单"${row.transportNumber}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await transportApi.deleteTransport(row.id)
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
    
    // 将前端字段映射到后端字段
    const transportData = {
      id: formData.id,
      transportNumber: formData.transportNumber,
      orderId: formData.orderId ? parseInt(formData.orderId) : null,
      vehicleId: formData.vehicleId ? parseInt(formData.vehicleId) : null,
      driverId: formData.driverId ? parseInt(formData.driverId) : null,
      startTime: formData.startTime,
      estimatedDuration: parseInt(formData.estimatedDuration) || 0,
      distance: parseFloat(formData.distance) || 0,
      fuelCost: parseFloat(formData.fuelCost) || 0,
      tollCost: parseFloat(formData.tollCost) || 0,
      otherCosts: parseFloat(formData.otherCosts) || 0,
      totalCost: totalCost.value,
      transportStatus: formData.transportStatus,
      notes: formData.notes
    }
    
    await transportApi.saveTransport(transportData)
    ElMessage.success(formData.id ? '更新成功' : '创建成功')
    
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    console.error('保存运输失败:', error)
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
    transportNumber: '',
    orderId: null,
    vehicleId: null,
    driverId: null,
    startTime: '',
    estimatedDuration: 0,
    distance: 0,
    fuelCost: 0,
    tollCost: 0,
    otherCosts: 0,
    transportStatus: 'planned',
    notes: ''
  })
}

// 生成运输单号
const generateTransportNumber = () => {
  const now = new Date()
  const dateStr = now.toISOString().slice(0, 10).replace(/-/g, '')
  const timeStr = now.getTime().toString().slice(-6)
  formData.transportNumber = `TRP${dateStr}${timeStr}`
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
const getStatusType = (status) => {
  const types = {
    planned: 'info',
    in_progress: 'warning',
    completed: 'success',
    cancelled: 'danger',
    delayed: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    planned: '计划中',
    in_progress: '进行中',
    completed: '已完成',
    cancelled: '已取消',
    delayed: '延迟'
  }
  return texts[status] || status
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

const formatDuration = (minutes) => {
  if (!minutes) return '0分钟'
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  if (hours > 0) {
    return `${hours}小时${mins}分钟`
  }
  return `${mins}分钟`
}
</script>

<style scoped>
.transports-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
  color: #333;
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
  opacity: 0.8;
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

.transport-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.vehicle, .driver {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #666;
}

.distance-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.duration {
  font-size: 12px;
  color: #999;
}

.cost {
  font-weight: 600;
  color: #f56c6c;
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
</style> 