<template>
  <div class="deliveries-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">🚚</i>
        配送管理
      </h1>
      <p class="page-description">管理系统中的配送任务，包括配送状态、签收信息和配送记录</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input v-model="searchForm.keyword" placeholder="搜索收件人、电话" class="search-input" />
        <select v-model="searchForm.status" class="search-select">
          <option value="">全部状态</option>
          <option value="pending">待配送</option>
          <option value="out_for_delivery">配送中</option>
          <option value="delivered">已签收</option>
          <option value="failed">配送失败</option>
          <option value="returned">已退回</option>
        </select>
        <input type="date" v-model="searchForm.date" class="search-input" />
        <button @click="handleSearch" class="search-btn">搜索</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAssign" class="primary-btn">📋 分配配送</button>
        <button @click="handleBatchUpdate" class="secondary-btn">🔄 批量更新</button>
      </div>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="selectAll"></th>
            <th>运输单号</th>
            <th>收件人</th>
            <th>配送地址</th>
            <th>联系电话</th>
            <th>计划配送时间</th>
            <th>实际配送时间</th>
            <th>配送状态</th>
            <th>尝试次数</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="delivery in tableData" :key="delivery.id">
            <td><input type="checkbox" :value="delivery.id" v-model="selectedRows"></td>
            <td class="transport-number">{{ delivery.transportNumber }}</td>
            <td class="recipient-name">{{ delivery.recipientName }}</td>
            <td class="address-cell">{{ delivery.deliveryAddress }}</td>
            <td>{{ delivery.recipientPhone }}</td>
            <td>{{ formatDateTime(delivery.scheduledTime) }}</td>
            <td>{{ delivery.actualDeliveryTime ? formatDateTime(delivery.actualDeliveryTime) : '-' }}</td>
            <td>
              <span :class="['status-tag', `status-${delivery.deliveryStatus}`]">
                {{ getStatusText(delivery.deliveryStatus) }}
              </span>
            </td>
            <td class="attempts-cell">{{ delivery.attempts || 0 }}</td>
            <td class="action-cell">
              <button @click="handleUpdateStatus(delivery)" class="action-btn">更新状态</button>
              <button @click="handleView(delivery)" class="action-btn">详情</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { deliveryApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const selectedRows = ref([])

const searchForm = reactive({
  keyword: '',
  status: '',
  date: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

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
    if (searchForm.date) {
      queryParams.date = searchForm.date
    }
    
    // 调用API获取数据
    const response = await deliveryApi.getDeliveries(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          transportNumber: 'TR20250101001',
          recipientName: '李明',
          recipientPhone: '13812345678',
          deliveryAddress: '北京市朝阳区建国路88号',
          deliveryStatus: 'delivered',
          scheduledTime: '2025-01-03T09:00:00',
          actualDeliveryTime: '2025-01-03T10:30:00',
          attempts: 1,
          signatureInfo: '本人签收'
        },
        {
          id: 2,
          transportNumber: 'TR20250101002',
          recipientName: '王小红',
          recipientPhone: '13987654321',
          deliveryAddress: '上海市浦东新区陆家嘴环路1000号',
          deliveryStatus: 'out_for_delivery',
          scheduledTime: '2025-01-03T14:00:00',
          actualDeliveryTime: null,
          attempts: 1,
          signatureInfo: ''
        },
        {
          id: 3,
          transportNumber: 'TR20250101003',
          recipientName: '张三丰',
          recipientPhone: '13656789012',
          deliveryAddress: '广州市天河区珠江新城华夏路16号',
          deliveryStatus: 'pending',
          scheduledTime: '2025-01-04T10:00:00',
          actualDeliveryTime: null,
          attempts: 0,
          signatureInfo: ''
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.keyword) {
        const keyword = searchForm.keyword.toLowerCase()
        filteredData = filteredData.filter(delivery => 
          delivery.recipientName.toLowerCase().includes(keyword) ||
          delivery.recipientPhone.includes(keyword) ||
          delivery.transportNumber.toLowerCase().includes(keyword)
        )
      }
      
      if (searchForm.status) {
        filteredData = filteredData.filter(delivery => delivery.deliveryStatus === searchForm.status)
      }
      
      if (searchForm.date) {
        filteredData = filteredData.filter(delivery => {
          if (!delivery.scheduledTime) return false
          const deliveryDate = delivery.scheduledTime.split('T')[0]
          return deliveryDate === searchForm.date
        })
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载配送数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待配送',
    out_for_delivery: '配送中',
    delivered: '已签收',
    failed: '配送失败',
    returned: '已退回'
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

const handleAssign = () => {
  ElMessage.info('分配配送功能开发中...')
}

const handleBatchUpdate = () => {
  ElMessage.info('批量更新功能开发中...')
}

const handleUpdateStatus = (row) => {
  alert(`更新配送状态: ${row.transportNumber}`)
}

const handleView = (row) => {
  ElMessage.info(`查看配送详情: ${row.transportNumber}`)
}

const selectAll = (event) => {
  if (event.target.checked) {
    selectedRows.value = tableData.value.map(item => item.id)
  } else {
    selectedRows.value = []
  }
}

const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: '',
    date: ''
  })
  handleSearch()
}

const handleSign = async (row) => {
  try {
    await deliveryApi.signForDelivery(row.id, { signature_info: '已签收' })
    ElMessage.success('签收成功')
    loadData()
  } catch (error) {
    console.error('签收失败:', error)
    ElMessage.error('签收失败: ' + (error.message || '网络错误'))
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.deliveries-container {
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

.transport-number {
  font-weight: 600;
  color: #1a73e8;
}

.recipient-name {
  font-weight: 500;
  color: #2c3e50;
}

.address-cell {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.attempts-cell {
  font-weight: 500;
  color: #ef6c00;
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

.status-pending { background: #fef7e0; color: #b06000; }
.status-out_for_delivery { background: #e0f2fe; color: #0277bd; }
.status-delivered { background: #e8f5e8; color: #2e7d32; }
.status-failed { background: #ffebee; color: #c62828; }
.status-returned { background: #fff3e0; color: #ef6c00; }

.action-btn {
  padding: 6px 12px;
  margin: 0 3px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  background: #e3f2fd;
  color: #1565c0;
}

.action-btn:hover {
  background: #bbdefb;
}
</style>
