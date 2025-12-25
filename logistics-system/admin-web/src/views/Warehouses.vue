<template>
  <div class="warehouses-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">🏢</i>
        仓库管理
      </h1>
      <p class="page-description">管理系统中的所有仓库信息，包括仓库创建、编辑和状态管理</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input 
          v-model="searchForm.keyword" 
          placeholder="搜索仓库名称、编码" 
          @keyup.enter="handleSearch"
          class="search-input" 
        />
        <select v-model="searchForm.type" class="search-select">
          <option value="">全部类型</option>
          <option value="storage">存储仓库</option>
          <option value="distribution">配送中心</option>
          <option value="cold_storage">冷库</option>
          <option value="hazardous">危险品仓库</option>
        </select>
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="active">正常运营</option>
          <option value="inactive">停用</option>
          <option value="maintenance">维护中</option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">
          ➕ 新增仓库
        </button>
        <button @click="exportData" class="secondary-btn">
          📊 导出数据
        </button>
      </div>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="selectAll"></th>
            <th>仓库编码</th>
            <th>仓库名称</th>
            <th>类型</th>
            <th>城市</th>
            <th>地址</th>
            <th>容量</th>
            <th>负责人</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="warehouse in tableData" :key="warehouse.id">
            <td><input type="checkbox" :value="warehouse.id" v-model="selectedRows"></td>
            <td>{{ warehouse.code }}</td>
            <td>{{ warehouse.name }}</td>
            <td>{{ getTypeText(warehouse.warehouseType) }}</td>
            <td>{{ warehouse.city }}</td>
            <td>{{ warehouse.address }}</td>
            <td>{{ warehouse.capacity }}m³</td>
            <td>{{ warehouse.managerName || '未分配' }}</td>
            <td>
              <span :class="'status-badge status-' + warehouse.status">
                {{ getStatusText(warehouse.status) }}
              </span>
            </td>
            <td>{{ formatDateTime(warehouse.createdAt) }}</td>
            <td class="action-cell">
              <button @click="handleEdit(warehouse)" class="action-btn edit-btn">编辑</button>
              <button @click="handleView(warehouse)" class="action-btn view-btn">详情</button>
              <button @click="handleDelete(warehouse)" class="action-btn delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="loading" class="loading-overlay">
        <div class="loading-spinner">加载中...</div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <div class="pagination-info">
        共 {{ pagination.total }} 条记录，第 {{ pagination.currentPage }} / {{ Math.ceil(pagination.total / pagination.pageSize) }} 页
      </div>
      <div class="pagination-controls">
        <button @click="goToPage(pagination.currentPage - 1)" :disabled="pagination.currentPage <= 1">上一页</button>
        <span class="page-numbers">
          <button 
            v-for="page in getPageNumbers()" 
            :key="page" 
            @click="goToPage(page)"
            :class="{ active: page === pagination.currentPage }"
          >
            {{ page }}
          </button>
        </span>
        <button @click="goToPage(pagination.currentPage + 1)" :disabled="pagination.currentPage >= Math.ceil(pagination.total / pagination.pageSize)">下一页</button>
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <div v-if="dialogVisible" class="dialog-overlay" @click="handleClose">
      <div class="dialog-box" @click.stop>
        <div class="dialog-header">
          <h3>{{ dialogTitle }}</h3>
          <button @click="handleClose" class="close-btn">✕</button>
        </div>
        
        <form @submit.prevent="handleSubmit" class="dialog-form">
          <div class="form-row">
            <div class="form-group">
              <label>仓库编码 *</label>
              <input v-model="formData.code" :disabled="!isAdd" required>
            </div>
            <div class="form-group">
              <label>仓库名称 *</label>
              <input v-model="formData.name" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>仓库类型 *</label>
              <select v-model="formData.warehouseType" required>
                <option value="">请选择类型</option>
                <option value="storage">存储仓库</option>
                <option value="distribution">配送中心</option>
                <option value="cold_storage">冷库</option>
                <option value="hazardous">危险品仓库</option>
              </select>
            </div>
            <div class="form-group">
              <label>容量(m³)</label>
              <input type="number" step="0.01" v-model="formData.capacity">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>城市 *</label>
              <select v-model="formData.city" required>
                <option value="">请选择城市</option>
                <option value="北京">北京</option>
                <option value="上海">上海</option>
                <option value="广州">广州</option>
                <option value="深圳">深圳</option>
                <option value="杭州">杭州</option>
                <option value="成都">成都</option>
                <option value="武汉">武汉</option>
                <option value="西安">西安</option>
              </select>
            </div>
            <div class="form-group">
              <label>邮政编码</label>
              <input v-model="formData.postalCode">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>详细地址 *</label>
              <input v-model="formData.address" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>负责人</label>
              <select v-model="formData.managerId">
                <option value="">请选择负责人</option>
                <option v-for="manager in managerOptions" :key="manager.id" :value="manager.id">
                  {{ manager.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>状态</label>
              <select v-model="formData.status">
                <option value="active">正常运营</option>
                <option value="inactive">停用</option>
                <option value="maintenance">维护中</option>
              </select>
            </div>
          </div>
          
          <div class="dialog-footer">
            <button type="button" @click="handleClose" class="cancel-btn">取消</button>
            <button type="submit" :disabled="submitting" class="submit-btn">
              {{ submitting ? '提交中...' : '确定' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { warehouseApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  type: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  code: '',
  name: '',
  warehouseType: '',
  address: '',
  city: '',
  postalCode: '',
  capacity: '',
  managerId: '',
  status: 'active'
})

const tableData = ref([])
const managerOptions = ref([])
const dialogTitle = ref('新增仓库')
const isAdd = ref(true)

// 模拟管理员数据
const loadManagers = () => {
  managerOptions.value = [
    { id: 1, name: '张三' },
    { id: 2, name: '李四' },
    { id: 3, name: '王五' },
    { id: 4, name: '赵六' },
    { id: 5, name: '钱七' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    // 构建查询参数
    const queryParams = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize
    }
    
    // 添加搜索条件
    if (searchForm.keyword) {
      queryParams.keyword = searchForm.keyword
    }
    if (searchForm.type) {
      queryParams.warehouseType = searchForm.type
    }
    if (searchForm.status) {
      queryParams.status = searchForm.status
    }
    
    // 调用API获取数据
    const response = await warehouseApi.getWarehouses(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          code: 'WH001',
          name: '上海浦东配送中心',
          warehouseType: 'distribution',
          address: '上海市浦东新区张杨路888号',
          city: '上海',
          postalCode: '200120',
          capacity: 10000.0,
          managerId: 1,
          managerName: '张经理',
          status: 'active',
          createdAt: '2024-01-10T09:00:00'
        },
        {
          id: 2,
          code: 'WH002',
          name: '北京朝阳仓储中心',
          warehouseType: 'storage',
          address: '北京市朝阳区建国路88号',
          city: '北京',
          postalCode: '100020',
          capacity: 15000.0,
          managerId: 2,
          managerName: '李主管',
          status: 'active',
          createdAt: '2024-01-12T14:30:00'
        },
        {
          id: 3,
          code: 'WH003',
          name: '广州冷链物流中心',
          warehouseType: 'cold_storage',
          address: '广州市天河区珠江新城华夏路16号',
          city: '广州',
          postalCode: '510623',
          capacity: 5000.0,
          managerId: 3,
          managerName: '王总监',
          status: 'maintenance',
          createdAt: '2024-01-15T11:20:00'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(warehouse => 
          warehouse.name.toLowerCase().includes(keyword) ||
          warehouse.code.toLowerCase().includes(keyword) ||
          warehouse.address.toLowerCase().includes(keyword)
        )
      }
      
      if (searchForm.type) {
        filteredData = filteredData.filter(warehouse => warehouse.warehouseType === searchForm.type)
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(warehouse => warehouse.status === searchForm.status)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载仓库数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

const getTypeText = (type) => {
  const typeMap = {
    storage: '存储仓库',
    distribution: '配送中心',
    cold_storage: '冷库',
    hazardous: '危险品仓库'
  }
  return typeMap[type] || type
}

const getStatusText = (status) => {
  const statusMap = {
    active: '正常运营',
    inactive: '停用',
    maintenance: '维护中'
  }
  return statusMap[status] || status
}

const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  try {
    const date = new Date(datetime)
    if (isNaN(date.getTime())) return '-'
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化错误:', error)
    return '-'
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    type: '',
    status: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增仓库'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑仓库'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessage.info(`查看仓库详情: ${row.name}`)
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除仓库 ${row.name} 吗？`)) {
    try {
      await warehouseApi.deleteWarehouse(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除仓库失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await warehouseApi.saveWarehouse(formData)
    ElMessage.success(isAdd.value ? '仓库创建成功' : '仓库更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存仓库失败:', error)
    ElMessage.error('操作失败: ' + (error.message || '网络错误'))
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    code: '',
    name: '',
    warehouseType: '',
    address: '',
    city: '',
    postalCode: '',
    capacity: '',
    managerId: '',
    status: 'active'
  })
}

const selectAll = (event) => {
  if (event.target.checked) {
    selectedRows.value = tableData.value.map(item => item.id)
  } else {
    selectedRows.value = []
  }
}

const goToPage = (page) => {
  if (page >= 1 && page <= Math.ceil(pagination.total / pagination.pageSize)) {
    pagination.currentPage = page
    loadData()
  }
}

const getPageNumbers = () => {
  const total = Math.ceil(pagination.total / pagination.pageSize)
  const current = pagination.currentPage
  const pages = []
  
  const start = Math.max(1, current - 2)
  const end = Math.min(total, current + 2)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  
  return pages
}

const exportData = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadData()
  loadManagers()
})
</script>

<style scoped>
.warehouses-container {
  width: 100%;
  min-height: 100%;
  background: #f5f7fa;
  padding: 20px;
}

.page-header {
  background: white;
  padding: 20px 30px;
  border-bottom: 1px solid #e1e8ed;
  margin-bottom: 0;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.icon {
  font-size: 28px;
}

.page-description {
  color: #7f8c8d;
  margin: 0;
  font-size: 14px;
}

.action-bar {
  background: white;
  padding: 20px 30px;
  border-bottom: 1px solid #e1e8ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-input, .search-select {
  height: 40px;
  padding: 0 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.search-input {
  width: 200px;
}

.search-select {
  min-width: 120px;
}

.search-btn, .reset-btn, .primary-btn, .secondary-btn {
  height: 40px;
  padding: 0 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn {
  background: linear-gradient(135deg, #4285f4 0%, #34a853 100%);
  color: white;
}

.reset-btn {
  background: #f8f9fa;
  color: #5f6368;
  border: 1px solid #dadce0;
}

.button-section {
  display: flex;
  gap: 12px;
}

.primary-btn {
  background: linear-gradient(135deg, #4285f4 0%, #34a853 100%);
  color: white;
}

.secondary-btn {
  background: white;
  color: #5f6368;
  border: 1px solid #dadce0;
}

.table-container {
  background: white;
  margin: 0;
  position: relative;
  min-height: 400px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  padding: 12px;
  background: #f8f9fa;
  font-weight: 600;
  text-align: left;
  color: #202124;
  border-bottom: 2px solid #e8eaed;
}

.data-table td {
  padding: 12px;
  border-bottom: 1px solid #e8eaed;
}

.warehouse-code {
  font-family: monospace;
  font-weight: 600;
  color: #1a73e8;
}

.warehouse-name {
  font-weight: 500;
  color: #2c3e50;
}

.address-cell {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.capacity-cell {
  font-weight: 500;
  color: #137333;
}

.type-tag, .status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  min-width: 60px;
}

.type-storage { background: #e3f2fd; color: #1565c0; }
.type-distribution { background: #e8f5e8; color: #2e7d32; }
.type-cold_storage { background: #e0f2fe; color: #0277bd; }
.type-hazardous { background: #fff3e0; color: #ef6c00; }

.status-active { background: #e8f5e8; color: #2e7d32; }
.status-inactive { background: #ffebee; color: #c62828; }
.status-maintenance { background: #fff3e0; color: #ef6c00; }

.action-cell {
  white-space: nowrap;
}

.action-btn {
  margin-right: 8px;
  padding: 4px 8px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.edit-btn {
  background: #e3f2fd;
  color: #1565c0;
}

.view-btn {
  background: #f3e5f5;
  color: #7b1fa2;
}

.delete-btn {
  background: #ffebee;
  color: #c62828;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 20;
}

.pagination-container {
  background: white;
  padding: 20px 30px;
  border-top: 1px solid #e1e8ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-controls button {
  padding: 8px 12px;
  border: 1px solid #dadce0;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.pagination-controls button.active {
  background: #4285f4;
  color: white;
  border-color: #4285f4;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-box {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 90%;
  max-width: 700px;
  max-height: 90vh;
  overflow-y: auto;
}

.dialog-header {
  padding: 25px 30px;
  border-bottom: 1px solid #e1e8ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-form {
  padding: 30px;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.form-group.full-width {
  width: 100%;
}

.form-group label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
  font-size: 14px;
}

.form-group input,
.form-group select {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.dialog-footer {
  padding: 20px 30px;
  border-top: 1px solid #e1e8ed;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.cancel-btn, .submit-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.cancel-btn {
  background: #f8f9fa;
  color: #5f6368;
  border: 1px solid #dadce0;
}

.submit-btn {
  background: linear-gradient(135deg, #4285f4 0%, #34a853 100%);
  color: white;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
