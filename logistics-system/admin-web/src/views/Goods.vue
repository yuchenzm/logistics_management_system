<template>
  <div class="goods-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">📦</i>
        货物管理
      </h1>
      <p class="page-description">管理系统中的所有货物信息，包括货物创建、分类管理和库存跟踪</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input 
          v-model="searchForm.keyword" 
          placeholder="搜索货物名称、SKU" 
          @keyup.enter="handleSearch"
          class="search-input" 
        />
        <select v-model="searchForm.category" class="search-select">
          <option value="">全部分类</option>
          <option value="electronics">电子产品</option>
          <option value="clothing">服装</option>
          <option value="food">食品</option>
          <option value="books">图书</option>
          <option value="furniture">家具</option>
        </select>
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="active">正常</option>
          <option value="discontinued">停产</option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">
          ➕ 新增货物
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
            <th>货物名称</th>
            <th>SKU</th>
            <th>分类</th>
            <th>重量(kg)</th>
            <th>体积(m³)</th>
            <th>单价</th>
            <th>特性</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="goods in tableData" :key="goods.id">
            <td><input type="checkbox" :value="goods.id" v-model="selectedRows"></td>
            <td class="goods-name">{{ goods.name }}</td>
            <td class="sku-code">{{ goods.sku }}</td>
            <td>
              <span :class="['category-tag', `category-${goods.category}`]">
                {{ getCategoryText(goods.category) }}
              </span>
            </td>
            <td>{{ goods.weight || '-' }}</td>
            <td>{{ goods.volume || '-' }}</td>
            <td class="price-cell">¥{{ formatPrice(goods.unitPrice) }}</td>
            <td>
              <div class="features">
                <span v-if="goods.fragile" class="feature-tag fragile">易碎</span>
                <span v-if="goods.hazardous" class="feature-tag hazardous">危险</span>
                <span v-if="goods.temperatureRequirements !== 'normal'" class="feature-tag temp">
                  {{ getTempText(goods.temperatureRequirements) }}
                </span>
              </div>
            </td>
            <td>
              <span :class="['status-tag', `status-${goods.status}`]">
                {{ getStatusText(goods.status) }}
              </span>
            </td>
            <td>{{ formatDateTime(goods.createdAt) }}</td>
            <td class="action-cell">
              <button @click="handleEdit(goods)" class="action-btn edit-btn">编辑</button>
              <button @click="handleView(goods)" class="action-btn view-btn">详情</button>
              <button @click="handleDelete(goods)" class="action-btn delete-btn">删除</button>
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
              <label>货物名称 *</label>
              <input v-model="formData.name" required>
            </div>
            <div class="form-group">
              <label>SKU编码</label>
              <input v-model="formData.sku">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>分类 *</label>
              <select v-model="formData.category" required>
                <option value="">请选择分类</option>
                <option value="electronics">电子产品</option>
                <option value="clothing">服装</option>
                <option value="food">食品</option>
                <option value="books">图书</option>
                <option value="furniture">家具</option>
              </select>
            </div>
            <div class="form-group">
              <label>单价</label>
              <input type="number" step="0.01" v-model="formData.unitPrice">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>重量(kg)</label>
              <input type="number" step="0.01" v-model="formData.weight">
            </div>
            <div class="form-group">
              <label>体积(m³)</label>
              <input type="number" step="0.01" v-model="formData.volume">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>描述</label>
              <textarea v-model="formData.description" rows="3"></textarea>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>温度要求</label>
              <select v-model="formData.temperatureRequirements">
                <option value="normal">常温</option>
                <option value="frozen">冷冻</option>
                <option value="refrigerated">冷藏</option>
              </select>
            </div>
            <div class="form-group">
              <label>状态</label>
              <select v-model="formData.status">
                <option value="active">正常</option>
                <option value="discontinued">停产</option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>
                <input type="checkbox" v-model="formData.fragile"> 易碎品
              </label>
            </div>
            <div class="form-group">
              <label>
                <input type="checkbox" v-model="formData.hazardous"> 危险品
              </label>
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
import { goodsApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  category: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  name: '',
  sku: '',
  category: '',
  description: '',
  weight: '',
  volume: '',
  unitPrice: '',
  fragile: false,
  hazardous: false,
  temperatureRequirements: 'normal',
  status: 'active'
})

const tableData = ref([])
const dialogTitle = ref('新增货物')
const isAdd = ref(true)

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
    if (searchForm.category) {
      queryParams.goodsCategory = searchForm.category
    }
    if (searchForm.status) {
      queryParams.goodsStatus = searchForm.status
    }
    
    // 调用API获取数据
    const response = await goodsApi.getGoods(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          name: 'iPhone 15 Pro',
          sku: 'IPH15PRO001',
          category: 'electronics',
          description: '苹果iPhone 15 Pro 256GB',
          weight: 0.187,
          volume: 0.0001,
          unitPrice: 8999.00,
          fragile: true,
          hazardous: false,
          temperatureRequirements: 'normal',
          status: 'active',
          createdAt: '2024-01-15T09:00:00'
        },
        {
          id: 2,
          name: '联想ThinkPad E14',
          sku: 'LNV-TP001',
          category: 'electronics',
          description: '联想ThinkPad E14 商务笔记本',
          weight: 1.64,
          volume: 0.003,
          unitPrice: 4999.00,
          fragile: true,
          hazardous: false,
          temperatureRequirements: 'normal',
          status: 'active',
          createdAt: '2024-02-10T14:30:00'
        },
        {
          id: 3,
          name: '冷冻牛肉',
          sku: 'BEEF-FROZEN',
          category: 'food',
          description: '进口冷冻牛肉 1kg装',
          weight: 1.0,
          volume: 0.001,
          unitPrice: 89.00,
          fragile: false,
          hazardous: false,
          temperatureRequirements: 'frozen',
          status: 'active',
          createdAt: '2024-03-05T11:20:00'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(goods => 
          goods.name.toLowerCase().includes(keyword) ||
          goods.sku.toLowerCase().includes(keyword) ||
          goods.description.toLowerCase().includes(keyword)
        )
      }
      
      if (searchForm.category) {
        filteredData = filteredData.filter(goods => goods.category === searchForm.category)
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(goods => goods.status === searchForm.status)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const getCategoryText = (category) => {
  const categoryMap = {
    electronics: '电子产品',
    clothing: '服装',
    food: '食品',
    books: '图书',
    furniture: '家具'
  }
  return categoryMap[category] || category
}

const getTempText = (temp) => {
  const tempMap = {
    normal: '常温',
    frozen: '冷冻',
    refrigerated: '冷藏'
  }
  return tempMap[temp] || temp
}

const getStatusText = (status) => {
  const statusMap = {
    active: '正常',
    discontinued: '停产'
  }
  return statusMap[status] || status
}

const formatPrice = (price) => {
  return Number(price || 0).toFixed(2)
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
    category: '',
    status: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增货物'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑货物'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessage.info(`查看货物详情: ${row.name}`)
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除货物 ${row.name} 吗？`)) {
    try {
      await goodsApi.deleteGoods(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除货物失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await goodsApi.saveGoods(formData)
    ElMessage.success(isAdd.value ? '货物创建成功' : '货物更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存货物失败:', error)
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
    name: '',
    sku: '',
    category: '',
    description: '',
    weight: '',
    volume: '',
    unitPrice: '',
    fragile: false,
    hazardous: false,
    temperatureRequirements: 'normal',
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
})
</script>

<style scoped>
.goods-container {
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

.goods-name {
  font-weight: 500;
  color: #2c3e50;
}

.sku-code {
  font-family: monospace;
  font-weight: 600;
  color: #1a73e8;
}

.price-cell {
  font-weight: 500;
  color: #137333;
}

.category-tag, .status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  min-width: 60px;
}

.category-electronics { background: #e3f2fd; color: #1565c0; }
.category-clothing { background: #f3e5f5; color: #7b1fa2; }
.category-food { background: #e8f5e8; color: #2e7d32; }
.category-books { background: #fff3e0; color: #ef6c00; }
.category-furniture { background: #fce4ec; color: #c2185b; }

.status-active { background: #e8f5e8; color: #2e7d32; }
.status-discontinued { background: #ffebee; color: #c62828; }

.features {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.feature-tag {
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 10px;
  font-weight: 500;
}

.feature-tag.fragile { background: #fff3e0; color: #ef6c00; }
.feature-tag.hazardous { background: #ffebee; color: #c62828; }
.feature-tag.temp { background: #e0f2fe; color: #0277bd; }

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
.form-group select,
.form-group textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group input[type="checkbox"] {
  width: auto;
  margin-right: 8px;
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
