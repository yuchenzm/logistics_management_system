<template>
  <div class="orders-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">📋</i>
        订单管理
      </h1>
      <p class="page-description">管理系统中的所有订单信息，包括订单创建、状态跟踪和优先级管理</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <input
          v-model="searchForm.keyword"
          placeholder="搜索订单号、客户名称"
          @keyup.enter="handleSearch"
          class="search-input"
        />
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="pending">待确认</option>
          <option value="confirmed">已确认</option>
          <option value="picked_up">已取货</option>
          <option value="in_transit">运输中</option>
          <option value="delivered">已送达</option>
          <option value="cancelled">已取消</option>
        </select>
        <select v-model="searchForm.priority" class="search-select">
          <option value="">全部优先级</option>
          <option value="urgent">紧急</option>
          <option value="high">高</option>
          <option value="normal">普通</option>
          <option value="low">低</option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
        <button @click="resetSearch" class="reset-btn">重置</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">
          ➕ 新增订单
        </button>
        <button @click="exportData" class="secondary-btn">
          📊 导出数据
        </button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="selectAll"></th>
            <th>订单号</th>
            <th>客户名称</th>
            <th>发货地</th>
            <th>收货地</th>
            <th>货物描述</th>
            <th>总金额</th>
            <th>优先级</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in tableData" :key="order.id" :class="{ 'loading-row': loading }">
            <td><input type="checkbox" :value="order.id" v-model="selectedRows"></td>
            <td class="order-number">{{ order.orderNumber }}</td>
            <td class="customer-name">{{ order.customerName }}</td>
            <td>{{ order.originCity }}</td>
            <td>{{ order.destinationCity }}</td>
            <td class="goods-desc">{{ order.goodsDescription || order.specialInstructions }}</td>
            <td class="amount-cell">¥{{ formatPrice(order.totalAmount) }}</td>
            <td>
              <span :class="['priority-tag', `priority-${order.priority}`]">
                {{ getPriorityText(order.priority) }}
              </span>
            </td>
            <td>
              <span :class="['status-tag', `status-${order.orderStatus || order.status}`]">
                {{ getStatusText(order.orderStatus || order.status) }}
              </span>
            </td>
            <td>{{ formatDateTime(order.createdAt) }}</td>
            <td class="action-cell">
              <button @click="handleEdit(order)" class="action-btn edit-btn">编辑</button>
              <button @click="handleView(order)" class="action-btn view-btn">详情</button>
              <button @click="handleDelete(order)" class="action-btn delete-btn">删除</button>
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
              <label>订单号 *</label>
              <input v-model="formData.order_number" :disabled="!isAdd" required>
            </div>
            <div class="form-group">
              <label>客户名称 *</label>
              <select v-model="formData.customer_id" required>
                <option value="">请选择客户</option>
                <option v-for="customer in customerOptions" :key="customer.id" :value="customer.id">
                  {{ customer.name }}
                </option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>发货城市 *</label>
              <select v-model="formData.origin_city" required>
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
              <label>收货城市 *</label>
              <select v-model="formData.destination_city" required>
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
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>发货地址 *</label>
              <input v-model="formData.origin_address" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>收货地址 *</label>
              <input v-model="formData.destination_address" required>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group full-width">
              <label>货物描述 *</label>
              <textarea v-model="formData.goods_description" rows="3" required></textarea>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>总重量(kg)</label>
              <input type="number" step="0.01" v-model="formData.total_weight">
            </div>
            <div class="form-group">
              <label>总体积(m³)</label>
              <input type="number" step="0.01" v-model="formData.total_volume">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>总金额 *</label>
              <input type="number" step="0.01" v-model="formData.total_amount" required>
            </div>
            <div class="form-group">
              <label>优先级</label>
              <select v-model="formData.priority">
                <option value="normal">普通</option>
                <option value="high">高</option>
                <option value="urgent">紧急</option>
                <option value="low">低</option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>状态</label>
              <select v-model="formData.status">
                <option value="pending">待确认</option>
                <option value="confirmed">已确认</option>
                <option value="picked_up">已取货</option>
                <option value="in_transit">运输中</option>
                <option value="delivered">已送达</option>
                <option value="cancelled">已取消</option>
              </select>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { orderApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  status: '',
  priority: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  order_number: '',
  customer_id: '',
  customer_name: '',
  origin_city: '',
  origin_address: '',
  destination_city: '',
  destination_address: '',
  goods_description: '',
  total_weight: '',
  total_volume: '',
  total_amount: '',
  priority: 'normal',
  status: 'pending',
  remarks: ''
})

const tableData = ref([])
const customerOptions = ref([])
const dialogTitle = ref('新增订单')
const isAdd = ref(true)

// 模拟客户数据
const loadCustomers = () => {
  customerOptions.value = [
    { id: 1, name: '阿里巴巴集团' },
    { id: 2, name: '腾讯科技' },
    { id: 3, name: '京东集团' },
    { id: 4, name: '字节跳动' },
    { id: 5, name: '美团' }
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
    if (searchForm.status) {
      queryParams.status = searchForm.status
    }
    if (searchForm.priority) {
      queryParams.priority = searchForm.priority
    }
    
    // 调用API获取数据
    const response = await orderApi.getOrders(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          order_number: 'ORD202501001',
          customer_id: 1,
          customer_name: '阿里巴巴集团',
          origin_city: '杭州',
          origin_address: '西湖区文三路969号',
          destination_city: '北京',
          destination_address: '海淀区中关村大街1号',
          goods_description: '服务器设备及配件',
          total_weight: 500.00,
          total_volume: 2.5,
          total_amount: 15000.00,
          priority: 'urgent',
          status: 'in_transit',
          remarks: '加急处理',
          created_at: '2024-01-15T09:00:00'
        },
        {
          id: 2,
          order_number: 'ORD202501002',
          customer_id: 2,
          customer_name: '腾讯科技',
          origin_city: '深圳',
          origin_address: '南山区深南大道10000号',
          destination_city: '上海',
          destination_address: '浦东新区陆家嘴环路1000号',
          goods_description: '办公用品及电子设备',
          total_weight: 200.00,
          total_volume: 1.2,
          total_amount: 8500.00,
          priority: 'high',
          status: 'delivered',
          remarks: '',
          created_at: '2024-01-14T14:30:00'
        },
        {
          id: 3,
          order_number: 'ORD202501003',
          customer_id: 3,
          customer_name: '京东集团',
          origin_city: '北京',
          origin_address: '朝阳区北辰西路8号',
          destination_city: '广州',
          destination_address: '天河区珠江新城华夏路16号',
          goods_description: '图书及文化用品',
          total_weight: 150.00,
          total_volume: 0.8,
          total_amount: 3200.00,
          priority: 'normal',
          status: 'pending',
          remarks: '',
          created_at: '2024-01-13T11:20:00'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(order => 
          order.order_number.toLowerCase().includes(keyword) ||
          order.customer_name.toLowerCase().includes(keyword) ||
          order.goods_description.toLowerCase().includes(keyword)
        )
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(order => order.status === searchForm.status)
      }
      
      if (searchForm.priority) {
        filteredData = filteredData.filter(order => order.priority === searchForm.priority)
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

const getPriorityText = (priority) => {
  const priorityMap = {
    urgent: '紧急',
    high: '高',
    normal: '普通',
    low: '低'
  }
  return priorityMap[priority] || priority
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待确认',
    confirmed: '已确认',
    picked_up: '已取货',
    in_transit: '运输中',
    delivered: '已送达',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const formatPrice = (price) => {
  return Number(price || 0).toFixed(2)
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
    priority: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增订单'
  resetForm()
  // 自动生成订单号
  formData.order_number = 'ORD' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + String(Date.now()).slice(-3)
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑订单'
  // 正确映射API字段到表单字段
  Object.assign(formData, {
    id: row.id,
    order_number: row.orderNumber,
    customer_id: row.customerId,
    customer_name: row.customerName,
    origin_city: row.originCity,
    origin_address: row.originAddress,
    destination_city: row.destinationCity,
    destination_address: row.destinationAddress,
    goods_description: row.goodsDescription || row.specialInstructions,
    total_weight: row.totalWeight,
    total_volume: row.totalVolume,
    total_amount: row.totalAmount,
    priority: row.priority,
    status: row.orderStatus || row.status,
    remarks: row.remarks || ''
  })
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessage.info(`查看订单详情: ${row.orderNumber}`)
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除订单 ${row.orderNumber} 吗？`)) {
    try {
      await orderApi.deleteOrder(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除订单失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    // 从客户列表中获取客户名称
    const customer = customerOptions.value.find(c => c.id == formData.customer_id)
    if (customer) {
      formData.customer_name = customer.name
    }
    
    // 将前端的下划线字段映射到后端的驼峰字段
    const orderData = {
      id: formData.id,
      orderNumber: formData.order_number,
      customerId: parseInt(formData.customer_id),
      originCity: formData.origin_city,
      originAddress: formData.origin_address,
      destinationCity: formData.destination_city,
      destinationAddress: formData.destination_address,
      specialInstructions: formData.goods_description,
      totalWeight: parseFloat(formData.total_weight) || 0,
      totalVolume: parseFloat(formData.total_volume) || 0,
      totalAmount: parseFloat(formData.total_amount) || 0,
      priority: formData.priority,
      orderStatus: formData.status,
      paymentStatus: 'pending'
    }
    
    await orderApi.saveOrder(orderData)
    ElMessage.success(isAdd.value ? '订单创建成功' : '订单更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存订单失败:', error)
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
    order_number: '',
    customer_id: '',
    customer_name: '',
    origin_city: '',
    origin_address: '',
    destination_city: '',
    destination_address: '',
    goods_description: '',
    total_weight: '',
    total_volume: '',
    total_amount: '',
    priority: 'normal',
    status: 'pending',
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
  loadCustomers()
})
</script>

<style scoped>
.orders-container {
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

.order-number {
  font-family: monospace;
  font-weight: 600;
  color: #1a73e8;
}

.customer-name {
  font-weight: 500;
  color: #2c3e50;
}

.goods-desc {
  max-width: 150px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.amount-cell {
  font-weight: 500;
  color: #137333;
}

.priority-tag, .status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  min-width: 60px;
}

.priority-urgent { background: #ffebee; color: #c62828; }
.priority-high { background: #fff3e0; color: #ef6c00; }
.priority-normal { background: #e8f5e8; color: #2e7d32; }
.priority-low { background: #f3e5f5; color: #7b1fa2; }

.status-pending { background: #fff3e0; color: #ef6c00; }
.status-confirmed { background: #e3f2fd; color: #1565c0; }
.status-picked_up { background: #f3e5f5; color: #7b1fa2; }
.status-in_transit { background: #e0f2fe; color: #0277bd; }
.status-delivered { background: #e8f5e8; color: #2e7d32; }
.status-cancelled { background: #ffebee; color: #c62828; }

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
  max-width: 800px;
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