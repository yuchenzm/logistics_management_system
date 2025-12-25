<template>
  <div class="shipping-rates-container">
    <div class="page-header">
      <h1 class="page-title">
        <i class="icon">💲</i>
        运费管理
      </h1>
      <p class="page-description">管理系统中的运费标准，包括基础费率、重量费率和距离费率设置</p>
    </div>

    <div class="action-bar">
      <div class="search-section">
        <input v-model="searchForm.origin" placeholder="始发城市" class="search-input" />
        <input v-model="searchForm.destination" placeholder="目的城市" class="search-input" />
        <select v-model="searchForm.type" class="search-select">
          <option value="">全部类型</option>
          <option value="express">特快</option>
          <option value="standard">标准</option>
          <option value="economy">经济</option>
        </select>
        <button @click="handleSearch" class="search-btn">搜索</button>
      </div>
      
      <div class="button-section">
        <button @click="handleAdd" class="primary-btn">➕ 新增费率</button>
        <button @click="handleBatchImport" class="secondary-btn">📥 批量导入</button>
      </div>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>始发城市</th>
            <th>目的城市</th>
            <th>运输类型</th>
            <th>重量范围(kg)</th>
            <th>基础费率</th>
            <th>重量费率(/kg)</th>
            <th>距离费率(/km)</th>
            <th>燃油附加费率</th>
            <th>有效期</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="rate in tableData" :key="rate.id">
            <td class="city-name">{{ rate.origin_city }}</td>
            <td class="city-name">{{ rate.destination_city }}</td>
            <td>
              <span :class="['type-tag', `type-${rate.transport_type}`]">
                {{ getTypeText(rate.transport_type) }}
              </span>
            </td>
            <td class="weight-range">{{ rate.weight_range_min }}-{{ rate.weight_range_max }}</td>
            <td class="rate-cell">¥{{ formatAmount(rate.base_rate) }}</td>
            <td class="rate-cell">¥{{ formatAmount(rate.rate_per_kg) }}</td>
            <td class="rate-cell">¥{{ formatAmount(rate.rate_per_km) }}</td>
            <td class="surcharge-cell">{{ (rate.fuel_surcharge_rate * 100).toFixed(2) }}%</td>
            <td class="date-range">
              <div>{{ formatDate(rate.effective_date) }}</div>
              <div class="expiry-date">{{ formatDate(rate.expiry_date) }}</div>
            </td>
            <td>
              <span :class="['status-tag', `status-${rate.status}`]">
                {{ getStatusText(rate.status) }}
              </span>
            </td>
            <td class="action-cell">
              <button @click="handleEdit(rate)" class="action-btn">编辑</button>
              <button @click="handleCopy(rate)" class="action-btn">复制</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { shippingRateApi } from '@/utils/api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)

const searchForm = reactive({
  origin: '',
  destination: '',
  type: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  origin_city: '',
  destination_city: '',
  transport_type: 'standard',
  weight_range_min: 0,
  weight_range_max: 10,
  base_rate: '',
  rate_per_kg: '',
  rate_per_km: '',
  fuel_surcharge_rate: 0.12,
  effective_date: '',
  expiry_date: '',
  status: 'active'
})

const tableData = ref([])
const dialogTitle = ref('新增费率')
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
    if (searchForm.origin) {
      queryParams.origin = searchForm.origin
    }
    if (searchForm.destination) {
      queryParams.destination = searchForm.destination
    }
    if (searchForm.type) {
      queryParams.type = searchForm.type
    }
    
    // 调用API获取数据
    const response = await shippingRateApi.getShippingRates(queryParams)
    
    if (response && response.data) {
      tableData.value = response.data.records || response.data
      pagination.total = response.data.total || response.data.length || 0
    } else {
      // 如果API调用失败，使用模拟数据作为备用
      const mockData = [
        {
          id: 1,
          origin_city: '上海',
          destination_city: '北京',
          transport_type: 'express',
          weight_range_min: 0.0,
          weight_range_max: 10.0,
          base_rate: 30.00,
          rate_per_kg: 8.50,
          rate_per_km: 1.20,
          fuel_surcharge_rate: 0.15,
          effective_date: '2025-01-01',
          expiry_date: '2025-12-31',
          status: 'active'
        },
        {
          id: 2,
          origin_city: '上海',
          destination_city: '北京',
          transport_type: 'standard',
          weight_range_min: 0.0,
          weight_range_max: 10.0,
          base_rate: 20.00,
          rate_per_kg: 5.50,
          rate_per_km: 0.80,
          fuel_surcharge_rate: 0.12,
          effective_date: '2025-01-01',
          expiry_date: '2025-12-31',
          status: 'active'
        },
        {
          id: 3,
          origin_city: '北京',
          destination_city: '广州',
          transport_type: 'economy',
          weight_range_min: 10.0,
          weight_range_max: 100.0,
          base_rate: 50.00,
          rate_per_kg: 3.20,
          rate_per_km: 0.60,
          fuel_surcharge_rate: 0.10,
          effective_date: '2025-01-01',
          expiry_date: '2025-12-31',
          status: 'active'
        }
      ]
      
      // 根据搜索条件过滤模拟数据
      let filteredData = mockData
      
      if (searchForm.origin) {
        filteredData = filteredData.filter(rate => 
          rate.origin_city.includes(searchForm.origin)
        )
      }
      
      if (searchForm.destination) {
        filteredData = filteredData.filter(rate => 
          rate.destination_city.includes(searchForm.destination)
        )
      }
      
      if (searchForm.type) {
        filteredData = filteredData.filter(rate => rate.transport_type === searchForm.type)
      }
      
      tableData.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载运费数据失败:', error)
    ElMessage.error('加载数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

const getTypeText = (type) => {
  const typeMap = {
    express: '特快',
    standard: '标准',
    economy: '经济'
  }
  return typeMap[type] || type
}

const getStatusText = (status) => {
  return status === 'active' ? '有效' : '无效'
}

const formatAmount = (amount) => {
  return amount ? parseFloat(amount).toFixed(2) : '0.00'
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN')
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    origin: '',
    destination: '',
    type: ''
  })
  handleSearch()
}

const handleAdd = () => {
  isAdd.value = true
  dialogTitle.value = '新增费率'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isAdd.value = false
  dialogTitle.value = '编辑费率'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleCopy = (row) => {
  isAdd.value = true
  dialogTitle.value = '复制费率'
  Object.assign(formData, { ...row, id: null })
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  if (confirm(`确定要删除从${row.origin_city}到${row.destination_city}的${getTypeText(row.transport_type)}费率吗？`)) {
    try {
      await shippingRateApi.deleteShippingRate(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除运费失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '网络错误'))
    }
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await shippingRateApi.saveShippingRate(formData)
    ElMessage.success(isAdd.value ? '费率创建成功' : '费率更新成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存运费失败:', error)
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
    origin_city: '',
    destination_city: '',
    transport_type: 'standard',
    weight_range_min: 0,
    weight_range_max: 10,
    base_rate: '',
    rate_per_kg: '',
    rate_per_km: '',
    fuel_surcharge_rate: 0.12,
    effective_date: '',
    expiry_date: '',
    status: 'active'
  })
}

const handleBatchImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.shipping-rates-container {
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

.city-name {
  font-weight: 500;
  color: #2c3e50;
}

.weight-range {
  font-size: 12px;
  color: #7f8c8d;
}

.rate-cell {
  font-weight: 600;
  color: #137333;
}

.surcharge-cell {
  font-weight: 500;
  color: #ef6c00;
}

.date-range {
  font-size: 12px;
}

.expiry-date {
  color: #7f8c8d;
  margin-top: 2px;
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

.type-express { background: #ffebee; color: #c62828; }
.type-standard { background: #e0f2fe; color: #0277bd; }
.type-economy { background: #e8f5e8; color: #2e7d32; }

.status-active { background: #e8f5e8; color: #2e7d32; }
.status-inactive { background: #ffebee; color: #c62828; }

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
