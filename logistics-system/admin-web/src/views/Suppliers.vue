<template>
  <div class="suppliers-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">🏢</i>
        供应商管理
      </h1>
      <p class="page-description">管理系统中的所有供应商信息，包括供应商创建、联系信息和合作状态管理</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input 
          v-model="searchForm.keyword" 
          placeholder="搜索供应商名称、联系人" 
          @keyup.enter="handleSearch"
          class="search-input" 
        />
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="active">正常合作</option>
          <option value="suspended">暂停合作</option>
          <option value="blacklisted">黑名单</option>
        </select>
        <select v-model="searchForm.city" class="search-select">
          <option value="">全部城市</option>
          <option value="北京">北京</option>
          <option value="上海">上海</option>
          <option value="广州">广州</option>
          <option value="深圳">深圳</option>
          <option value="杭州">杭州</option>
          <option value="成都">成都</option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">
          ➕ 新增供应商
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
            <th>供应商名称</th>
            <th>联系人</th>
            <th>联系电话</th>
            <th>邮箱</th>
            <th>城市</th>
            <th>地址</th>
            <th>合作状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="supplier in tableData" :key="supplier.id">
            <td><input type="checkbox" :value="supplier.id" v-model="selectedRows"></td>
            <td class="supplier-name">{{ supplier.name }}</td>
            <td class="contact-name">{{ supplier.contact_person }}</td>
            <td class="phone-cell">{{ supplier.phone }}</td>
            <td class="email-cell">{{ supplier.email || '-' }}</td>
            <td>{{ supplier.city }}</td>
            <td class="address-cell">{{ supplier.address }}</td>
            <td>
              <span :class="['status-tag', `status-${supplier.status}`]">
                {{ getStatusText(supplier.status) }}
              </span>
            </td>
            <td>{{ formatDateTime(supplier.created_at) }}</td>
            <td class="action-cell">
              <button @click="handleEdit(supplier)" class="action-btn edit-btn">编辑</button>
              <button @click="handleView(supplier)" class="action-btn view-btn">详情</button>
              <button @click="handleDelete(supplier)" class="action-btn delete-btn">删除</button>
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
              <label>供应商名称 *</label>
              <input v-model="formData.name" required>
            </div>
            <div class="form-group">
              <label>联系人 *</label>
              <input v-model="formData.contact_person" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>联系电话 *</label>
              <input v-model="formData.phone" required>
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <input type="email" v-model="formData.email">
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
              <input v-model="formData.postal_code">
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
              <label>合作状态</label>
              <select v-model="formData.status">
                <option value="active">正常合作</option>
                <option value="suspended">暂停合作</option>
                <option value="blacklisted">黑名单</option>
              </select>
            </div>
            <div class="form-group">
              <label>信用等级</label>
              <select v-model="formData.credit_rating">
                <option value="A">A级</option>
                <option value="B">B级</option>
                <option value="C">C级</option>
                <option value="D">D级</option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>备注</label>
              <textarea v-model="formData.remarks" rows="3"></textarea>
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
import { supplierApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  status: '',
  city: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  name: '',
  contact_person: '',
  phone: '',
  email: '',
  city: '',
  postal_code: '',
  address: '',
  status: 'active',
  credit_rating: 'A',
  remarks: ''
})

const tableData = ref([])
const dialogTitle = ref('新增供应商')
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
    if (searchForm.status) {
      queryParams.status = searchForm.status
    }
    if (searchForm.city) {
      queryParams.city = searchForm.city
    }
    
    // 调用API获取数据
    const response = await supplierApi.getSuppliers(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          name: '顺丰速运有限公司',
          contact_person: '李经理',
          phone: '13912345678',
          email: 'li.manager@sf-express.com',
          city: '深圳',
          postal_code: '518000',
          address: '深圳市福田区彩田路3069号星河世纪大厦',
          status: 'active',
          credit_rating: 'A',
          remarks: '合作多年，服务优质',
          created_at: '2024-01-10T09:00:00'
        },
        {
          id: 2,
          name: '中通快递股份有限公司',
          contact_person: '王总监',
          phone: '13876543210',
          email: 'wang.director@zto.com',
          city: '上海',
          postal_code: '200000',
          address: '上海市青浦区华新镇华徐公路3029弄28号',
          status: 'active',
          credit_rating: 'B',
          remarks: '价格合理，覆盖面广',
          created_at: '2024-01-08T14:20:00'
        },
        {
          id: 3,
          name: '上海物流配送服务公司',
          contact_person: '张主任',
          phone: '13698745632',
          email: '',
          city: '上海',
          postal_code: '200000',
          address: '浦东新区张江高科技园区',
          status: 'suspended',
          credit_rating: 'C',
          remarks: '因质量问题暂停合作',
          created_at: '2024-01-05T11:20:00'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(supplier => 
          supplier.name.toLowerCase().includes(keyword) ||
          supplier.contact_person.toLowerCase().includes(keyword) ||
          supplier.phone.includes(keyword)
        )
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(supplier => supplier.status === searchForm.status)
      }
      
      if (searchForm.city) {
        filteredData = filteredData.filter(supplier => supplier.city === searchForm.city)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载供应商数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

const getStatusText = (status) => {
  const statusMap = {
    active: '正常合作',
    suspended: '暂停合作',
    blacklisted: '黑名单'
  }
  return statusMap[status] || status
}

const formatDateTime = (datetime) => {
  return new Date(datetime).toLocaleString('zh-CN')
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: '',
    city: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增供应商'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑供应商'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessage.info(`查看供应商详情: ${row.name}`)
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除供应商 ${row.name} 吗？`)) {
    try {
      await supplierApi.deleteSupplier(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除供应商失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await supplierApi.saveSupplier(formData)
    ElMessage.success(isAdd.value ? '供应商创建成功' : '供应商更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存供应商失败:', error)
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
    contact_person: '',
    phone: '',
    email: '',
    city: '',
    postal_code: '',
    address: '',
    status: 'active',
    credit_rating: 'A',
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
})
</script>

<style scoped>
.suppliers-container {
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

.supplier-name {
  font-weight: 500;
  color: #2c3e50;
}

.contact-name {
  color: #5f6368;
}

.phone-cell {
  font-family: monospace;
  color: #1a73e8;
}

.email-cell {
  font-family: monospace;
  color: #7b1fa2;
}

.address-cell {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  min-width: 60px;
}

.status-active { background: #e8f5e8; color: #2e7d32; }
.status-suspended { background: #fff3e0; color: #ef6c00; }
.status-blacklisted { background: #ffebee; color: #c62828; }

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
