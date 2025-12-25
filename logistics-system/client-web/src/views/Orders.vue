<template>
  <div class="orders-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>我的订单</h2>
        <p>管理您的所有订单信息</p>
      </div>
      <div class="header-right">
        <el-button type="success" @click="createOrder">
          <el-icon><Plus /></el-icon>
          创建订单
        </el-button>
      </div>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-row">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon pending">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ orderStats.pending }}</div>
            <div class="stat-label">待处理</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon in-transit">
            <el-icon><Van /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ orderStats.inTransit }}</div>
            <div class="stat-label">运输中</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon delivered">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ orderStats.delivered }}</div>
            <div class="stat-label">已送达</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon total">
            <el-icon><DocumentCopy /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ orderStats.total }}</div>
            <div class="stat-label">总订单</div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 搜索和过滤 -->
    <el-card class="search-card">
      <div class="search-bar">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索订单号、起始地址或目的地址" 
          class="search-input"
          clearable
          @keyup.enter="searchOrders"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select 
          v-model="statusFilter" 
          placeholder="订单状态" 
          clearable
          class="filter-select"
        >
          <el-option label="全部状态" value="" />
          <el-option label="待处理" value="pending" />
          <el-option label="已确认" value="confirmed" />
          <el-option label="已取货" value="picked_up" />
          <el-option label="运输中" value="in_transit" />
          <el-option label="已送达" value="delivered" />
          <el-option label="已取消" value="cancelled" />
        </el-select>
        
        <el-select 
          v-model="priorityFilter" 
          placeholder="优先级" 
          clearable
          class="filter-select"
        >
          <el-option label="全部优先级" value="" />
          <el-option label="低" value="low" />
          <el-option label="中" value="medium" />
          <el-option label="高" value="high" />
        </el-select>
        
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="searchOrders"
        />
        
        <el-button 
          type="primary" 
          @click="searchOrders"
          :loading="loading"
        >
          搜索
        </el-button>
        
        <el-button 
          @click="resetFilters"
        >
          重置
        </el-button>
        
        <el-button 
          type="info" 
          @click="exportOrders"
        >
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </div>
    </el-card>
    
    <!-- 订单列表 -->
    <el-card class="table-card">
      <el-table 
        :data="orders" 
        v-loading="loading"
        stripe
        style="width: 100%"
        @row-click="viewDetail"
        row-class-name="clickable-row"
        height="500"
      >
        <el-table-column prop="orderNumber" label="订单号" width="160" fixed="left">
          <template #default="{ row }">
            <span class="order-number">{{ row.orderNumber }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="起始地址" min-width="200">
          <template #default="{ row }">
            <el-tooltip :content="row.originAddress" placement="top">
              <span class="address-text">{{ row.originAddress }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        
        <el-table-column label="目的地址" min-width="200">
          <template #default="{ row }">
            <el-tooltip :content="row.destinationAddress" placement="top">
              <span class="address-text">{{ row.destinationAddress }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        
        <el-table-column label="重量(kg)" width="100">
          <template #default="{ row }">
            {{ row.totalWeight || '-' }}
          </template>
        </el-table-column>
        
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="getStatusType(row.orderStatus)"
              size="small"
            >
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="优先级" width="80">
          <template #default="{ row }">
            <el-tag 
              :type="getPriorityType(row.priority)"
              size="small"
            >
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        
        <el-table-column label="费用" width="100">
          <template #default="{ row }">
            <span class="amount">¥{{ row.totalAmount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button 
              size="small" 
              type="primary" 
              @click.stop="viewDetail(row)"
              link
            >
              详情
            </el-button>
            <el-button 
              v-if="canTrack(row.orderStatus)"
              size="small" 
              type="success" 
              @click.stop="trackOrder(row)"
              link
            >
              跟踪
            </el-button>
            <el-button 
              v-if="row.orderStatus === 'pending'"
              size="small" 
              type="warning" 
              @click.stop="editOrder(row)"
              link
            >
              编辑
            </el-button>
            <el-button 
              v-if="row.orderStatus === 'pending'"
              size="small" 
              type="danger" 
              @click.stop="cancelOrder(row.id)"
              link
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
    
    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="80%"
      max-width="800px"
    >
      <div v-if="selectedOrder" class="order-detail">
        <div class="detail-grid">
          <div class="detail-item">
            <label>订单号：</label>
            <span>{{ selectedOrder.orderNumber }}</span>
          </div>
          <div class="detail-item">
            <label>客户ID：</label>
            <span>{{ selectedOrder.customerId }}</span>
          </div>
          <div class="detail-item">
            <label>订单状态：</label>
            <el-tag 
              :type="getStatusType(selectedOrder.orderStatus)"
              size="small"
            >
              {{ getStatusText(selectedOrder.orderStatus) }}
            </el-tag>
          </div>
          <div class="detail-item">
            <label>优先级：</label>
            <el-tag 
              :type="getPriorityType(selectedOrder.priority)"
              size="small"
            >
              {{ getPriorityText(selectedOrder.priority) }}
            </el-tag>
          </div>
          <div class="detail-item">
            <label>起始地址：</label>
            <span>{{ selectedOrder.originAddress }}</span>
          </div>
          <div class="detail-item">
            <label>目的地址：</label>
            <span>{{ selectedOrder.destinationAddress }}</span>
          </div>
          <div class="detail-item">
            <label>总重量：</label>
            <span>{{ selectedOrder.totalWeight }} kg</span>
          </div>
          <div class="detail-item">
            <label>总费用：</label>
            <span class="amount">¥{{ selectedOrder.totalAmount }}</span>
          </div>
          <div class="detail-item">
            <label>创建时间：</label>
            <span>{{ formatDateTime(selectedOrder.createdAt) }}</span>
          </div>
          <div class="detail-item">
            <label>更新时间：</label>
            <span>{{ formatDateTime(selectedOrder.updatedAt) }}</span>
          </div>
        </div>
        
        <div class="detail-section">
          <h4>备注信息</h4>
          <p>{{ selectedOrder.remarks || '暂无备注' }}</p>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button 
            v-if="canTrack(selectedOrder?.orderStatus)"
            type="success" 
            @click="trackOrder(selectedOrder)"
          >
            物流跟踪
          </el-button>
          <el-button 
            v-if="selectedOrder?.orderStatus === 'pending'"
            type="warning" 
            @click="editOrder(selectedOrder)"
          >
            编辑订单
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi } from '../utils/api';
import { useRouter } from 'vue-router';
import type { Order, OrderStatus, PriorityLevel } from '../types/api';

const router = useRouter();

// 状态类型映射
const statusMap: Record<OrderStatus, string> = {
  pending: '待处理',
  confirmed: '已确认',
  picked_up: '已取件',
  in_transit: '运输中',
  delivered: '已送达',
  cancelled: '已取消'
}

// 状态类型样式映射
const statusTypeMap: Record<OrderStatus, string> = {
  pending: 'warning',
  confirmed: '',
  picked_up: 'info',
  in_transit: '',
  delivered: 'success',
  cancelled: 'danger'
}

// 优先级映射
const priorityMap: Record<PriorityLevel, string> = {
  low: '低',
  medium: '中',
  high: '高'
}

// 优先级类型样式映射
const priorityTypeMap: Record<PriorityLevel, string> = {
  low: 'info',
  medium: 'warning',
  high: 'danger'
}

// 使用Order类型定义订单列表
const orders = ref<Order[]>([])

// 响应式数据
const loading = ref(false)
const selectedOrder = ref<Order | null>(null)
const detailDialogVisible = ref(false)

// 搜索和过滤
const searchQuery = ref('')
const statusFilter = ref('')
const priorityFilter = ref('')
const dateRange = ref([])

// 分页
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 统计数据
const orderStats = reactive({
  pending: 0,
  inTransit: 0,
  delivered: 0,
  total: 0
})

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchQuery.value,
      status: statusFilter.value,
      priority: priorityFilter.value,
      startDate: dateRange.value?.[0],
      endDate: dateRange.value?.[1]
    }
    
    const response: any = await orderApi.list(params);

    if (response && response.code === 200 && response.data) {
      orders.value = response.data.records || [];
      total.value = response.data.total || 0;
        if (orders.value.length === 0) {
          ElMessage.info('当前没有订单数据');
        }
      } else {
      ElMessage.error(response?.message || '获取订单列表失败');
        orders.value = [];
        total.value = 0;
      }
  } catch (error: any) {
      console.error('获取订单列表失败:', error);
    ElMessage.error('获取订单列表失败: ' + (error?.message || '未知错误'));
    } finally {
      loading.value = false;
    }
}

// 获取订单统计
const fetchOrderStats = async () => {
  try {
    const response: any = await orderApi.getSummary();
    
    if (response && response.code === 200 && response.data) {
      const statsData = response.data;
        orderStats.pending = statsData.pending || 0;
        orderStats.inTransit = statsData.inTransit || 0;
        orderStats.delivered = statsData.delivered || 0;
        orderStats.total = statsData.total || 0;
      } else {
      ElMessage.error(response?.message || '获取订单统计失败')
      }
  } catch (error: any) {
    console.error('获取订单统计失败:', error)
    ElMessage.error('获取订单统计失败: ' + (error?.message || '未知错误'));
  }
}


// 组件挂载时加载数据
onMounted(() => {
  fetchOrders()
  fetchOrderStats()
})

// 搜索订单
const searchOrders = () => {
  currentPage.value = 1
  fetchOrders()
}

// 重置过滤条件
const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  priorityFilter.value = ''
  dateRange.value = []
  currentPage.value = 1
  fetchOrders()
}

// 导出订单
const exportOrders = () => {
  ElMessage.info('导出功能暂未实现')
}

// 查看详情
const viewDetail = (row: Order) => {
  selectedOrder.value = row
  detailDialogVisible.value = true
}

// 跟踪订单
const trackOrder = (row: any) => {
  router.push(`/tracking?orderNumber=${row.orderNumber}`)
}

// 编辑订单
const editOrder = (order: Order) => {
  if (order.id) {
    router.push(`/orders/edit/${order.id}`)
  } else {
    ElMessage.error('无法编辑，订单ID缺失')
  }
}

// 取消订单方法
const cancelOrder = async (id?: string) => {
  if (!id) return;
  
  try {
    await ElMessageBox.confirm('确定要取消此订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await orderApi.cancelOrder(id)
    
    ElMessage.success('订单已取消')
    fetchOrders()
  } catch (error) {
    console.error('取消订单失败:', error)
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
    }
  }
}

// 创建订单
const createOrder = () => {
  router.push('/create-order')
}

// 分页处理
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchOrders()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchOrders()
}

// 工具函数
const getStatusText = (status: OrderStatus) => {
  const statusMap = {
    'pending': '待处理',
    'confirmed': '已确认',
    'picked_up': '已取货',
    'in_transit': '运输中',
    'delivered': '已送达',
    'cancelled': '已取消'
  }
  return statusMap[status] || status
}

const getStatusType = (status: OrderStatus) => {
  return statusTypeMap[status] || ''
}

const getPriorityText = (priority: PriorityLevel) => {
  const priorityMap = {
    'low': '低',
    'medium': '中',
    'high': '高'
  }
  return priorityMap[priority] || priority
}

const getPriorityType = (priority: PriorityLevel) => {
  const typeMap = {
    'low': 'info',
    'medium': 'warning',
    'high': 'danger'
  }
  return typeMap[priority] || 'info'
}

const canTrack = (status: string) => {
  return ['picked_up', 'in_transit', 'delivered'].includes(status)
}

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}
</script>

<style scoped>
.orders-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left h2 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.header-left p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  border: none;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.stat-icon.pending {
  background: linear-gradient(135deg, #e6a23c 0%, #f39c12 100%);
}

.stat-icon.in-transit {
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 100%);
}

.stat-icon.delivered {
  background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
}

.stat-icon.total {
  background: linear-gradient(135deg, #909399 0%, #73767a 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.search-card {
  margin-bottom: 16px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.search-input {
  min-width: 300px;
  flex: 1;
}

.filter-select {
  min-width: 120px;
}

.table-card {
  border: none;
}

.clickable-row {
  cursor: pointer;
}

.clickable-row:hover {
  background-color: #f5f7fa;
}

.order-number {
  font-weight: 600;
  color: #409eff;
}

.address-text {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.amount {
  font-weight: 600;
  color: #f56c6c;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.order-detail {
  padding: 16px 0;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-item label {
  font-weight: 500;
  color: #606266;
  min-width: 80px;
}

.detail-item span {
  color: #303133;
}

.detail-section {
  margin-top: 24px;
}

.detail-section h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #303133;
}

.detail-section p {
  margin: 0;
  color: #606266;
  line-height: 1.5;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .search-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input {
    min-width: auto;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>
