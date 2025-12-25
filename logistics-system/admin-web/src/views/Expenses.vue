<template>
  <div class="expenses-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">💰</i>
        费用管理
      </h1>
      <p class="page-description">管理系统中的各项费用支出，包括运输费用、燃油费、过路费等</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input v-model="searchForm.keyword" placeholder="搜索费用描述" class="search-input" />
        <select v-model="searchForm.type" class="search-select">
          <option value="">全部类型</option>
          <option value="fuel">燃油费</option>
          <option value="toll">过路费</option>
          <option value="maintenance">维修费</option>
          <option value="salary">工资</option>
          <option value="other">其他</option>
        </select>
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="pending">待审批</option>
          <option value="approved">已审批</option>
          <option value="rejected">已拒绝</option>
        </select>
        <input type="date" v-model="searchForm.date" class="search-input" />
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">➕ 新增费用</button>
        <button @click="handleExport" class="secondary-btn">📊 导出报表</button>
      </div>
    </div>

    <div class="stats-bar">
      <div class="stat-card">
        <div class="stat-title">本月总支出</div>
        <div class="stat-value">¥{{ formatAmount(monthlyTotal) }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">今日支出</div>
        <div class="stat-value">¥{{ formatAmount(dailyTotal) }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">燃油费用</div>
        <div class="stat-value">¥{{ formatAmount(fuelTotal) }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">其他费用</div>
        <div class="stat-value">¥{{ formatAmount(otherTotal) }}</div>
      </div>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>费用类型</th>
            <th>费用描述</th>
            <th>金额</th>
            <th>关联运输</th>
            <th>负责人</th>
            <th>发生日期</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="expense in tableData" :key="expense.id">
            <td>
              <span :class="['type-tag', `type-${expense.expenseType}`]">
                {{ getTypeText(expense.expenseType) }}
              </span>
            </td>
            <td class="description-cell">{{ expense.description }}</td>
            <td class="amount-cell">¥{{ formatAmount(expense.amount) }}</td>
            <td>{{ expense.transportNumber || '-' }}</td>
            <td>{{ expense.responsiblePerson }}</td>
            <td>{{ formatDate(expense.expenseDate) }}</td>
            <td>
              <span :class="['status-tag', `status-${expense.approvalStatus}`]">
                {{ getStatusText(expense.approvalStatus) }}
              </span>
            </td>
            <td class="action-cell">
              <button @click="handleEdit(expense)" class="action-btn edit-btn">编辑</button>
              <button v-if="expense.approvalStatus === 'pending'" @click="handleApprove(expense)" class="action-btn approve-btn">通过</button>
              <button v-if="expense.approvalStatus === 'pending'" @click="handleReject(expense)" class="action-btn reject-btn">拒绝</button>
              <button @click="handleDelete(expense)" class="action-btn delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 编辑费用对话框 -->
    <div v-if="dialogVisible" class="dialog-overlay" @click="handleClose">
      <div class="dialog-box" @click.stop>
        <div class="dialog-header">
          <h3>{{ dialogTitle }}</h3>
          <button @click="handleClose" class="close-btn">✕</button>
        </div>
        
        <form @submit.prevent="handleSubmit" class="dialog-form">
          <div class="form-row">
            <div class="form-group">
              <label>费用类型 *</label>
              <select v-model="formData.expenseType" required>
                <option value="">请选择类型</option>
                <option value="fuel">燃油费</option>
                <option value="toll">过路费</option>
                <option value="maintenance">维修费</option>
                <option value="salary">工资</option>
                <option value="other">其他</option>
              </select>
            </div>
            <div class="form-group">
              <label>费用金额 *</label>
              <input type="number" step="0.01" v-model="formData.amount" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>费用描述 *</label>
              <textarea v-model="formData.description" rows="3" required></textarea>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>关联运输</label>
              <input v-model="formData.transportNumber" placeholder="运输单号">
            </div>
            <div class="form-group">
              <label>负责人 *</label>
              <input v-model="formData.responsiblePerson" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>发生日期 *</label>
              <input type="date" v-model="formData.expenseDate" required>
            </div>
            <div class="form-group">
              <label>审批状态</label>
              <select v-model="formData.approvalStatus">
                <option value="pending">待审批</option>
                <option value="approved">已审批</option>
                <option value="rejected">已拒绝</option>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { expenseApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  type: '',
  status: '',
  date: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  expenseType: '',
  description: '',
  amount: '',
  transportNumber: '',
  responsiblePerson: '',
  expenseDate: '',
  approvalStatus: 'pending'
})

const tableData = ref([])
const dialogTitle = ref('新增费用')
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
    if (searchForm.type) {
      queryParams.expenseType = searchForm.type
    }
    if (searchForm.status) {
      queryParams.approvalStatus = searchForm.status
    }
    if (searchForm.date) {
      queryParams.date = searchForm.date
    }
    
    // 调用API获取数据
    const response = await expenseApi.getExpenses(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          expenseType: 'fuel',
          description: '上海到北京运输燃油费',
          amount: 1200.50,
          transportNumber: 'TR20250101001',
          responsiblePerson: '张师傅',
          expenseDate: '2025-01-03',
          approvalStatus: 'approved'
        },
        {
          id: 2,
          expenseType: 'toll',
          description: '高速公路过路费',
          amount: 380.00,
          transportNumber: 'TR20250101001',
          responsiblePerson: '张师傅',
          expenseDate: '2025-01-03',
          approvalStatus: 'approved'
        },
        {
          id: 3,
          expenseType: 'maintenance',
          description: '车辆保养维修费用',
          amount: 800.00,
          transportNumber: null,
          responsiblePerson: '李师傅',
          expenseDate: '2025-01-02',
          approvalStatus: 'pending'
        },
        {
          id: 4,
          expenseType: 'salary',
          description: '司机工资',
          amount: 8000.00,
          transportNumber: null,
          responsiblePerson: '人事部',
          expenseDate: '2025-01-01',
          approvalStatus: 'approved'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(expense => 
          expense.description.toLowerCase().includes(keyword) ||
          expense.responsiblePerson.toLowerCase().includes(keyword) ||
          (expense.transportNumber && expense.transportNumber.toLowerCase().includes(keyword))
        )
      }
      
      if (searchForm.type) {
        filteredData = filteredData.filter(expense => expense.expenseType === searchForm.type)
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(expense => expense.approvalStatus === searchForm.status)
      }
      
      if (searchForm.date) {
        filteredData = filteredData.filter(expense => expense.expenseDate === searchForm.date)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载费用数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 计算统计数据
const monthlyTotal = computed(() => {
  return tableData.value.reduce((sum, item) => sum + item.amount, 0)
})

const dailyTotal = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return tableData.value
    .filter(item => item.expenseDate === today)
    .reduce((sum, item) => sum + item.amount, 0)
})

const fuelTotal = computed(() => {
  return tableData.value
    .filter(item => item.expenseType === 'fuel')
    .reduce((sum, item) => sum + item.amount, 0)
})

const otherTotal = computed(() => {
  return tableData.value
    .filter(item => !['fuel'].includes(item.expenseType))
    .reduce((sum, item) => sum + item.amount, 0)
})

const getTypeText = (type) => {
  const typeMap = {
    fuel: '燃油费',
    toll: '过路费',
    maintenance: '维修费',
    salary: '工资',
    other: '其他'
  }
  return typeMap[type] || type
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待审批',
    approved: '已审批',
    rejected: '已拒绝'
  }
  return statusMap[status] || status
}

const formatAmount = (amount) => {
  return amount ? parseFloat(amount).toLocaleString() : '0.00'
}

const formatDate = (date) => {
  if (!date) return '-'
  try {
    const dateObj = new Date(date)
    if (isNaN(dateObj.getTime())) return '-'
    return dateObj.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
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
    status: '',
    date: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增费用'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑费用'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleApprove = async (row) => {
  try {
    // 构建完整的费用对象，只更新审批状态
    const updatedExpense = {
      ...row,
      approvalStatus: 'approved'
    }
    await expenseApi.updateExpenseStatus(row.id, updatedExpense)
    ElMessage.success('审批成功')
    loadData()
  } catch (error) {
    console.error('审批失败:', error)
    ElMessage.error('审批失败: ' + (error.message || '网络错误'))
  }
}

const handleReject = async (row) => {
  if (confirm(`确定要拒绝费用记录 ${row.description} 吗？`)) {
    try {
      const updatedExpense = {
        ...row,
        approvalStatus: 'rejected'
      }
      await expenseApi.updateExpenseStatus(row.id, updatedExpense)
      ElMessage.success('拒绝成功')
      loadData()
    } catch (error) {
      console.error('拒绝审批失败:', error)
      ElMessage.error('拒绝审批失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除费用记录 ${row.description} 吗？`)) {
    try {
      await expenseApi.deleteExpense(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除费用失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await expenseApi.saveExpense(formData)
    ElMessage.success(isAdd.value ? '费用创建成功' : '费用更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存费用失败:', error)
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
    expenseType: '',
    description: '',
    amount: '',
    transportNumber: '',
    responsiblePerson: '',
    expenseDate: '',
    approvalStatus: 'pending'
  })
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.expenses-container {
  width: 100%;
  min-height: 100%;
  background: #f5f7fa;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  overflow-y: auto;
  margin: 0;
  box-sizing: border-box;
}

.page-header {
  background: white;
  padding: 25px 30px;
  border-bottom: 1px solid #e1e8ed;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon {
  font-size: 32px;
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
}

.search-section {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input, .search-select {
  height: 40px;
  padding: 0 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.search-btn, .primary-btn, .secondary-btn {
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
  background: #4285f4;
  color: white;
}

.reset-btn {
  background: #f8f9fa;
  color: #5f6368;
  border: 1px solid #dadce0;
}

.reset-btn:hover {
  background: #e8eaed;
}

.primary-btn {
  background: linear-gradient(135deg, #4285f4 0%, #34a853 100%);
  color: white;
}

.secondary-btn {
  background: white;
  color: #5f6368;
  border: 1px solid #dadce0;
  margin-left: 8px;
}

.secondary-btn:hover {
  background: #f8f9fa;
}

.stats-bar {
  background: white;
  padding: 20px 30px;
  border-bottom: 1px solid #e1e8ed;
  display: flex;
  gap: 20px;
}

.stat-card {
  flex: 1;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: white;
  text-align: center;
}

.stat-title {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
}

.table-container {
  background: white;
  margin: 0;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.data-table th {
  background: #f8f9fa;
  padding: 15px 12px;
  text-align: left;
  font-weight: 600;
  color: #202124;
  border-bottom: 2px solid #e8eaed;
}

.data-table td {
  padding: 12px;
  border-bottom: 1px solid #e8eaed;
}

.data-table tr:hover {
  background: #f8f9fa;
}

.description-cell {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.amount-cell {
  font-weight: 600;
  color: #c62828;
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

.type-fuel { background: #fff3e0; color: #ef6c00; }
.type-toll { background: #e3f2fd; color: #1565c0; }
.type-maintenance { background: #f3e5f5; color: #7b1fa2; }
.type-salary { background: #e8f5e8; color: #2e7d32; }
.type-other { background: #f3f4f6; color: #6b7280; }

.status-pending { background: #fef7e0; color: #b06000; }
.status-approved { background: #e8f5e8; color: #2e7d32; }
.status-rejected { background: #ffebee; color: #c62828; }

.action-btn {
  padding: 4px 8px;
  margin: 0 2px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-btn {
  background: #2196f3;
  color: white;
}

.edit-btn:hover {
  background: #1976d2;
}

.approve-btn {
  background: #4caf50;
  color: white;
}

.approve-btn:hover {
  background: #388e3c;
}

.reject-btn {
  background: #ff9800;
  color: white;
}

.reject-btn:hover {
  background: #f57c00;
}

.delete-btn {
  background: #f44336;
  color: white;
}

.delete-btn:hover {
  background: #d32f2f;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 100%;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dialog-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.dialog-form {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.form-group {
  flex: 1;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
}

.form-group select, .form-group input, .form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-group.full-width {
  flex: 1;
}

.form-group.full-width textarea {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.cancel-btn, .submit-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background: #f44336;
  color: white;
  margin-right: 8px;
}

.submit-btn {
  background: #4285f4;
  color: white;
}
</style>
