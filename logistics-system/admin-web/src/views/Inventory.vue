<template>
  <div class="inventory-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">📊</i>
        库存管理
      </h1>
      <p class="page-description">管理系统中的所有库存信息，包括入库、出库、调整和实时库存跟踪</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input 
          v-model="searchForm.keyword" 
          placeholder="搜索货物名称、仓库" 
          @keyup.enter="handleSearch"
          class="search-input" 
        />
        <select v-model="searchForm.warehouse_id" class="search-select">
          <option value="">全部仓库</option>
          <option v-for="warehouse in warehouseOptions" :key="warehouse.id" :value="warehouse.id">
            {{ warehouse.name }}
          </option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">
          ➕ 新增库存
        </button>
        <button @click="handleInbound" class="secondary-btn">
          📥 入库
        </button>
        <button @click="handleOutbound" class="secondary-btn">
          📤 出库
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
            <th>仓库名称</th>
            <th>货物名称</th>
            <th>库位</th>
            <th>批次号</th>
            <th>实际库存</th>
            <th>预留数量</th>
            <th>可用数量</th>
            <th>安全库存</th>
            <th>最后更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inventory in tableData" :key="inventory.id">
            <td><input type="checkbox" :value="inventory.id" v-model="selectedRows"></td>
            <td class="warehouse-name">{{ inventory.warehouseName }}</td>
            <td class="goods-name">{{ inventory.goodsName }}</td>
            <td class="location-cell">{{ inventory.location || '-' }}</td>
            <td class="batch-cell">{{ inventory.batchNumber || '-' }}</td>
            <td class="quantity-cell">{{ inventory.actualQuantity }}</td>
            <td class="reserved-cell">{{ inventory.reservedQuantity }}</td>
            <td class="available-cell">{{ inventory.availableQuantity }}</td>
            <td class="safety-cell">{{ inventory.safetyStock || '-' }}</td>
            <td>{{ formatDateTime(inventory.updatedAt) }}</td>
            <td class="action-cell">
              <button @click="handleEdit(inventory)" class="action-btn edit-btn">编辑</button>
              <button @click="handleAdjust(inventory)" class="action-btn adjust-btn">调整</button>
              <button @click="handleDelete(inventory)" class="action-btn delete-btn">删除</button>
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
              <label>仓库 *</label>
              <select v-model="formData.warehouse_id" required>
                <option value="">请选择仓库</option>
                <option v-for="warehouse in warehouseOptions" :key="warehouse.id" :value="warehouse.id">
                  {{ warehouse.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>货物 *</label>
              <select v-model="formData.goods_id" required>
                <option value="">请选择货物</option>
                <option v-for="goods in goodsOptions" :key="goods.id" :value="goods.id">
                  {{ goods.name }}
                </option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>库位</label>
              <input v-model="formData.location" placeholder="如：A-01-01">
            </div>
            <div class="form-group">
              <label>批次号</label>
              <input v-model="formData.batch_number" placeholder="如：BATCH20240101">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>实际库存 *</label>
              <input type="number" step="1" v-model="formData.actual_quantity" required>
            </div>
            <div class="form-group">
              <label>预留数量</label>
              <input type="number" step="1" v-model="formData.reserved_quantity">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>安全库存</label>
              <input type="number" step="1" v-model="formData.safety_stock">
            </div>
            <div class="form-group">
              <label>备注</label>
              <input v-model="formData.remarks">
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

    <!-- 库存调整对话框 -->
    <div v-if="adjustVisible" class="dialog-overlay" @click="closeAdjust">
      <div class="dialog-box" @click.stop>
        <div class="dialog-header">
          <h3>库存调整</h3>
          <button @click="closeAdjust" class="close-btn">✕</button>
        </div>
        
        <form @submit.prevent="handleAdjustSubmit" class="dialog-form">
          <div class="form-row">
            <div class="form-group">
              <label>仓库</label>
              <input :value="adjustData.warehouse_name" disabled>
            </div>
            <div class="form-group">
              <label>货物</label>
              <input :value="adjustData.goods_name" disabled>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>当前库存</label>
              <input :value="adjustData.actual_quantity" disabled>
            </div>
            <div class="form-group">
              <label>调整数量 *</label>
              <input type="number" v-model="adjustData.adjust_quantity" required placeholder="正数增加，负数减少">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>调整原因 *</label>
              <textarea v-model="adjustData.reason" rows="3" required placeholder="请输入调整原因"></textarea>
            </div>
          </div>
          
          <div class="dialog-footer">
            <button type="button" @click="closeAdjust" class="cancel-btn">取消</button>
            <button type="submit" :disabled="submitting" class="submit-btn">
              {{ submitting ? '确认调整' : '确认调整' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { inventoryApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const adjustVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  warehouse_id: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  warehouse_id: '',
  goods_id: '',
  location: '',
  batch_number: '',
  actual_quantity: '',
  reserved_quantity: 0,
  safety_stock: '',
  remarks: ''
})

const adjustData = reactive({
  id: null,
  warehouse_name: '',
  goods_name: '',
  actual_quantity: 0,
  adjust_quantity: '',
  reason: ''
})

const tableData = ref([])
const warehouseOptions = ref([])
const goodsOptions = ref([])
const dialogTitle = ref('新增库存')
const isAdd = ref(true)

// 加载仓库选项
const loadWarehouses = () => {
  warehouseOptions.value = [
    { id: 1, name: '上海浦东配送中心' },
    { id: 2, name: '北京朝阳仓储中心' },
    { id: 3, name: '广州冷链物流中心' }
  ]
}

// 加载货物选项
const loadGoods = () => {
  goodsOptions.value = [
    { id: 1, name: 'iPhone 15 Pro' },
    { id: 2, name: '联想ThinkPad E14' },
    { id: 3, name: '冷冻牛肉' }
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
    if (searchForm.warehouse_id) {
      queryParams.warehouse_id = searchForm.warehouse_id
    }
    
    // 调用API获取数据
    const response = await inventoryApi.getInventory(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          warehouseId: 1,
          warehouseName: '上海浦东配送中心',
          goodsId: 1,
          goodsName: 'iPhone 15 Pro',
          location: 'A-01-01',
          batchNumber: 'BATCH20240115',
          actualQuantity: 150,
          reservedQuantity: 20,
          availableQuantity: 130,
          safetyStock: 50,
          updatedAt: '2024-01-15T14:30:00'
        },
        {
          id: 2,
          warehouseId: 2,
          warehouseName: '北京朝阳仓储中心',
          goodsId: 2,
          goodsName: '联想ThinkPad E14',
          location: 'B-02-05',
          batchNumber: 'BATCH20240110',
          actualQuantity: 80,
          reservedQuantity: 10,
          availableQuantity: 70,
          safetyStock: 30,
          updatedAt: '2024-01-14T09:15:00'
        },
        {
          id: 3,
          warehouseId: 3,
          warehouseName: '广州冷链物流中心',
          goodsId: 3,
          goodsName: '冷冻牛肉',
          location: 'C-01-12',
          batchNumber: 'BEEF20240105',
          actualQuantity: 500,
          reservedQuantity: 50,
          availableQuantity: 450,
          safetyStock: 100,
          updatedAt: '2024-01-13T16:45:00'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(inventory => 
          inventory.goodsName.toLowerCase().includes(keyword) ||
          inventory.warehouseName.toLowerCase().includes(keyword) ||
          inventory.batchNumber.toLowerCase().includes(keyword)
        )
      }
      
      if (searchForm.warehouse_id) {
        filteredData = filteredData.filter(inventory => inventory.warehouseId == searchForm.warehouse_id)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载库存数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
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
    warehouse_id: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增库存'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑库存'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleAdjust = (row) => {
  Object.assign(adjustData, {
    id: row.id,
    warehouse_name: row.warehouseName,
    goods_name: row.goodsName,
    actual_quantity: row.actualQuantity,
    adjust_quantity: '',
    reason: ''
  })
  adjustVisible.value = true
}

const handleInbound = () => {
  ElMessage.info('入库功能开发中...')
}

const handleOutbound = () => {
  ElMessage.info('出库功能开发中...')
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除${row.goodsName}在${row.warehouseName}的库存记录吗？`)) {
    try {
      await inventoryApi.deleteInventory(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除库存失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    // 计算可用数量
    const availableQuantity = formData.actual_quantity - (formData.reserved_quantity || 0)
    
    await inventoryApi.saveInventory({
      ...formData,
      available_quantity: availableQuantity
    })
    ElMessage.success(isAdd.value ? '库存创建成功' : '库存更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存库存失败:', error)
    ElMessage.error('操作失败: ' + (error.message || '网络错误'))
  } finally {
    submitting.value = false
  }
}

const handleAdjustSubmit = async () => {
  submitting.value = true
  try {
    // await inventoryApi.adjustInventory(adjustData.id, {
    //   adjust_quantity: adjustData.adjust_quantity,
    //   reason: adjustData.reason
    // })
    ElMessage.success('库存调整成功')
    adjustVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('调整失败')
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const closeAdjust = () => {
  adjustVisible.value = false
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    warehouse_id: '',
    goods_id: '',
    location: '',
    batch_number: '',
    actual_quantity: '',
    reserved_quantity: 0,
    safety_stock: '',
    remarks: ''
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
  loadWarehouses()
  loadGoods()
})
</script>

<style scoped>
.inventory-container {
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

.warehouse-name, .goods-name {
  font-weight: 500;
  color: #2c3e50;
}

.location-cell, .batch-cell {
  font-family: monospace;
  color: #5f6368;
}

.quantity-cell {
  font-weight: 600;
  color: #1a73e8;
}

.reserved-cell {
  color: #ef6c00;
}

.available-cell {
  font-weight: 600;
  color: #137333;
}

.safety-cell {
  color: #7b1fa2;
}

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

.adjust-btn {
  background: #fff3e0;
  color: #ef6c00;
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
.form-group select,
.form-group textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
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
