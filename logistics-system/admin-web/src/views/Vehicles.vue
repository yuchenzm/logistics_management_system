<template>
  <div class="vehicles-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Van /></el-icon>
        车辆管理
      </h1>
      <p class="page-description">管理系统中的所有车辆信息，包括车辆基本信息、载重信息和司机分配</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索车牌号或品牌型号"
          @keyup.enter="handleSearch"
          clearable
          style="width: 300px;"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchForm.vehicleType"
          placeholder="车辆类型"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="货车" value="truck" />
          <el-option label="面包车" value="van" />
          <el-option label="皮卡" value="pickup" />
          <el-option label="集装箱" value="container" />
        </el-select>
        <el-select
          v-model="searchForm.status"
          placeholder="车辆状态"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="可用" value="available" />
          <el-option label="使用中" value="in_use" />
          <el-option label="维修中" value="maintenance" />
          <el-option label="停用" value="inactive" />
        </el-select>
        <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
        <el-button @click="resetSearch" :icon="Refresh">重置</el-button>
      </div>
      
      <div class="button-section">
        <el-button type="primary" @click="handleAdd" :icon="Plus">
          新增车辆
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
        <el-table-column prop="licensePlate" label="车牌号" width="120">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.licensePlate }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="品牌型号" width="180">
          <template #default="{ row }">
            <div class="vehicle-info">
              <div class="brand">{{ row.brand }} {{ row.model }}</div>
              <div class="year">{{ row.yearManufactured }}年</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="vehicleType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getVehicleTypeColor(row.vehicleType)"
              size="small"
            >
              {{ getVehicleTypeText(row.vehicleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="载重/容积" width="140">
          <template #default="{ row }">
            <div class="capacity-info">
              <div>{{ row.capacityWeight }}吨</div>
              <div class="volume">{{ row.capacityVolume }}m³</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="fuelType" label="燃料类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getFuelTypeColor(row.fuelType)"
              size="small"
            >
              {{ getFuelTypeText(row.fuelType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分配司机" width="120">
          <template #default="{ row }">
            <span v-if="row.driverName">{{ row.driverName }}</span>
            <el-text v-else type="info">未分配</el-text>
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
                  <el-dropdown-item command="assign">分配司机</el-dropdown-item>
                  <el-dropdown-item command="maintenance">维修记录</el-dropdown-item>
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
      width="700px"
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
            <el-form-item label="车牌号" prop="license_plate">
              <el-input v-model="formData.license_plate" placeholder="请输入车牌号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车辆类型" prop="vehicle_type">
              <el-select v-model="formData.vehicle_type" placeholder="请选择">
                <el-option label="货车" value="truck" />
                <el-option label="面包车" value="van" />
                <el-option label="皮卡" value="pickup" />
                <el-option label="集装箱" value="container" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="formData.brand" placeholder="请输入品牌" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="formData.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="载重(吨)" prop="capacity_weight">
              <el-input-number
                v-model="formData.capacity_weight"
                :min="0"
                :max="100"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="容积(m³)" prop="capacity_volume">
              <el-input-number
                v-model="formData.capacity_volume"
                :min="0"
                :max="200"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="燃料类型" prop="fuel_type">
              <el-select v-model="formData.fuel_type" placeholder="请选择">
                <el-option label="汽油" value="gasoline" />
                <el-option label="柴油" value="diesel" />
                <el-option label="电动" value="electric" />
                <el-option label="混合动力" value="hybrid" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产年份" prop="year_manufactured">
              <el-date-picker
                v-model="formData.year_manufactured"
                type="year"
                placeholder="选择生产年份"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分配司机" prop="driver_id">
              <el-select
                v-model="formData.driver_id"
                placeholder="选择司机"
                clearable
                filterable
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
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio value="available">可用</el-radio>
                <el-radio value="in_use">使用中</el-radio>
                <el-radio value="maintenance">维修中</el-radio>
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
  Van, Search, Refresh, Plus, Download, Edit, ArrowDown
} from '@element-plus/icons-vue'
import { vehicleApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])
const driverOptions = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  vehicleType: '',
  status: ''
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
  license_plate: '',
  brand: '',
  model: '',
  vehicle_type: 'truck',
  capacity_weight: 0,
  capacity_volume: 0,
  fuel_type: 'diesel',
  year_manufactured: new Date().getFullYear(),
  driver_id: null,
  status: 'available'
})

// 表单引用
const formRef = ref()

// 表单验证规则
const formRules = {
  license_plate: [{ required: true, message: '请输入车牌号', trigger: 'blur' }],
  brand: [{ required: true, message: '请输入品牌', trigger: 'blur' }],
  model: [{ required: true, message: '请输入型号', trigger: 'blur' }],
  vehicle_type: [{ required: true, message: '请选择车辆类型', trigger: 'change' }],
  capacity_weight: [{ required: true, message: '请输入载重', trigger: 'blur' }],
  capacity_volume: [{ required: true, message: '请输入容积', trigger: 'blur' }],
  fuel_type: [{ required: true, message: '请选择燃料类型', trigger: 'change' }]
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
  fetchDriverOptions()
})

// 获取车辆数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      keyword: searchForm.keyword,
      vehicleType: searchForm.vehicleType,
      status: searchForm.status
    }
    const response = await vehicleApi.getVehicles(params)
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取车辆数据失败:', error)
    ElMessage.error('获取车辆数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 获取司机选项
const fetchDriverOptions = async () => {
  try {
    const response = await vehicleApi.getDriversByPage({ page: 1, size: 100 })
    driverOptions.value = response.data.records || []
  } catch (error) {
    console.error('获取司机选项失败', error)
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
    vehicleType: '',
    status: ''
  })
  handleSearch()
}

// 新增车辆
const handleAdd = () => {
  dialogTitle.value = '新增车辆'
  resetFormData()
  dialogVisible.value = true
}

// 编辑车辆
const handleEdit = (row) => {
  dialogTitle.value = '编辑车辆'
  // 正确映射后端驼峰字段到前端下划线字段
  Object.assign(formData, {
    id: row.id,
    license_plate: row.licensePlate,
    vehicle_type: row.vehicleType,
    brand: row.brand,
    model: row.model,
    year_manufactured: row.yearManufactured,
    capacity_weight: row.capacityWeight,
    capacity_volume: row.capacityVolume,
    fuel_type: row.fuelType,
    driver_id: row.driverId,
    status: row.status,
    registration_date: row.registrationDate,
    maintenance_date: row.maintenanceDate,
    insurance_expiry: row.insuranceExpiry
  })
  dialogVisible.value = true
}

// 处理操作
const handleAction = async (command, row) => {
  switch (command) {
    case 'view':
      ElMessage.info('查看详情功能开发中')
      break
    case 'assign':
      ElMessage.info('分配司机功能开发中')
      break
    case 'maintenance':
      ElMessage.info('维修记录功能开发中')
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

// 删除车辆
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除车辆"${row.licensePlate}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await vehicleApi.deleteVehicle(row.id)
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
    const vehicleData = {
      id: formData.id,
      licensePlate: formData.license_plate,
      vehicleType: formData.vehicle_type,
      brand: formData.brand,
      model: formData.model,
      yearManufactured: parseInt(formData.year_manufactured),
      capacityWeight: parseFloat(formData.capacity_weight),
      capacityVolume: parseFloat(formData.capacity_volume),
      fuelType: formData.fuel_type,
      driverId: formData.driver_id ? parseInt(formData.driver_id) : null,
      status: formData.status,
      registrationDate: formData.registration_date,
      maintenanceDate: formData.maintenance_date,
      insuranceExpiry: formData.insurance_expiry
    }
    
    await vehicleApi.saveVehicle(vehicleData)
    ElMessage.success(formData.id ? '更新成功' : '创建成功')
    
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    console.error('保存车辆失败:', error)
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
    license_plate: '',
    brand: '',
    model: '',
    vehicle_type: 'truck',
    capacity_weight: 0,
    capacity_volume: 0,
    fuel_type: 'diesel',
    year_manufactured: new Date().getFullYear(),
    driver_id: null,
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
const getVehicleTypeColor = (type) => {
  const colors = {
    truck: 'primary',
    van: 'success',
    pickup: 'warning',
    container: 'danger'
  }
  return colors[type] || 'info'
}

const getVehicleTypeText = (type) => {
  const texts = {
    truck: '货车',
    van: '面包车',
    pickup: '皮卡',
    container: '集装箱'
  }
  return texts[type] || type
}

const getFuelTypeColor = (type) => {
  const colors = {
    gasoline: 'warning',
    diesel: 'primary',
    electric: 'success',
    hybrid: 'info'
  }
  return colors[type] || 'info'
}

const getFuelTypeText = (type) => {
  const texts = {
    gasoline: '汽油',
    diesel: '柴油',
    electric: '电动',
    hybrid: '混动'
  }
  return texts[type] || type
}

const getStatusType = (status) => {
  const types = {
    available: 'success',
    in_use: 'warning',
    maintenance: 'danger',
    inactive: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    available: '可用',
    in_use: '使用中',
    maintenance: '维修中',
    inactive: '停用'
  }
  return texts[status] || status
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}
</script>

<style scoped>
.vehicles-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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

.vehicle-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.brand {
  font-weight: 500;
  color: #333;
}

.year {
  font-size: 12px;
  color: #999;
}

.capacity-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.volume {
  font-size: 12px;
  color: #666;
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