<template>
  <div class="tracking-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>物流跟踪</h2>
        <p>实时跟踪您的包裹运输状态</p>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="refreshTracking" :loading="searching">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>
    
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-section">
        <h3>跟踪查询</h3>
        <div class="search-form">
          <el-input
            v-model="trackingNumber"
            placeholder="请输入订单号或运输单号"
            size="large"
            class="tracking-input"
            @keyup.enter="searchTracking"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button 
            type="primary" 
            size="large"
            @click="searchTracking"
            :loading="searching"
          >
            查询跟踪
          </el-button>
        </div>
        
        <div class="quick-search">
          <span class="quick-label">快速查询：</span>
          <el-button 
            v-for="order in recentOrders" 
            :key="order.id"
            type="info"
            size="small"
            plain
            @click="quickSearch(order.orderNumber)"
          >
            {{ order.orderNumber }}
          </el-button>
        </div>
      </div>
    </el-card>
    
    <!-- 跟踪结果 -->
    <div v-if="trackingResult" class="tracking-result">
      <!-- 基本信息 -->
      <el-card class="result-card">
        <template #header>
          <div class="result-header">
            <h3>跟踪结果</h3>
            <div class="tracking-info">
              <div class="info-item">
                <label>订单号：</label>
                <span class="order-number">{{ trackingResult.orderNumber }}</span>
              </div>
              <div class="info-item">
                <label>当前状态：</label>
                <el-tag 
                  :type="getStatusType(trackingResult.orderStatus)"
                  size="large"
                >
                  {{ getStatusText(trackingResult.orderStatus) }}
                </el-tag>
              </div>
            </div>
          </div>
        </template>
        
        <!-- 配送路线 -->
        <div class="route-section">
          <h4>配送路线</h4>
          <div class="route-card">
            <div class="route-item start">
              <div class="route-dot start-dot"></div>
              <div class="route-content">
                <div class="route-title">取货地址</div>
                <div class="route-address">{{ trackingResult.originAddress }}</div>
              </div>
            </div>
            <div class="route-line"></div>
            <div class="route-item end">
              <div class="route-dot end-dot"></div>
              <div class="route-content">
                <div class="route-title">送达地址</div>
                <div class="route-address">{{ trackingResult.destinationAddress }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 配送信息 -->
        <div class="delivery-section">
          <h4>配送信息</h4>
          <div class="info-grid">
            <div class="info-card">
              <div class="info-icon">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="info-content">
                <div class="info-label">创建时间</div>
                <div class="info-value">{{ formatDateTime(trackingResult.createdAt) }}</div>
              </div>
            </div>
            <div class="info-card">
              <div class="info-icon">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="info-content">
                <div class="info-label">更新时间</div>
                <div class="info-value">{{ formatDateTime(trackingResult.updatedAt) }}</div>
              </div>
            </div>
            <div class="info-card">
              <div class="info-icon">
                <el-icon><ScaleToOriginal /></el-icon>
              </div>
              <div class="info-content">
                <div class="info-label">货物重量</div>
                <div class="info-value">{{ trackingResult.totalWeight || 0 }} kg</div>
              </div>
            </div>
            <div class="info-card">
              <div class="info-icon">
                <el-icon><Box /></el-icon>
              </div>
              <div class="info-content">
                <div class="info-label">货物体积</div>
                <div class="info-value">{{ trackingResult.totalVolume || 0 }} m³</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 物流轨迹 -->
      <el-card v-if="trackingHistory.length > 0" class="timeline-card">
        <template #header>
          <h4>物流轨迹</h4>
        </template>
        
        <div class="timeline">
          <div 
            v-for="(item, index) in trackingHistory" 
            :key="index"
            class="timeline-item"
            :class="{ active: index === 0 }"
          >
            <div class="timeline-dot" :class="{ active: index === 0 }"></div>
            <div class="timeline-content">
              <div class="timeline-time">{{ formatDateTime(item.trackingTime) }}</div>
              <div class="timeline-location">
                <el-icon><Location /></el-icon>
                {{ item.location }}
              </div>
              <div class="timeline-status">{{ item.statusUpdate }}</div>
              <div v-if="item.notes" class="timeline-notes">{{ item.notes }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 无结果提示 -->
    <div v-else-if="!searching && searched" class="no-result">
      <el-empty description="未找到相关信息" :image-size="100">
        <div class="no-result-text">
          <h3>未找到相关信息</h3>
          <p>请检查您输入的订单号或运输单号是否正确</p>
        </div>
        <el-button type="primary" @click="resetSearch">重新查询</el-button>
      </el-empty>
    </div>
    
    <!-- 搜索提示 -->
    <div v-if="!trackingResult && !searching && !searched" class="search-prompt">
      <el-empty description="请输入单号进行查询" :image-size="100">
        <div class="prompt-text">
          <h3>请输入订单号或运输单号进行查询</h3>
          <p>您可以通过订单号或运输单号来跟踪您的包裹</p>
        </div>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Refresh, Search, Calendar, Clock, ScaleToOriginal, Box, Location } from '@element-plus/icons-vue'
import { orderApi } from '../utils/api'
import { formatDateTime } from '../utils/formatters'
import type { Order } from '../types/api'

// 接口定义
type OrderStatus = 'pending' | 'confirmed' | 'picked_up' | 'in_transit' | 'delivered' | 'cancelled';

const route = useRoute()
const router = useRouter()
const searching = ref(false)
const searched = ref(false)
const trackingNumber = ref('')

const trackingResult = ref<Order | null>(null)
const trackingHistory = ref<any[]>([])
const recentOrders = ref<any[]>([])

// 初始化时获取最近订单和处理路由参数
onMounted(() => {
  // 获取最近的几个订单号用于快速查询
  orderApi.list({ pageNum: 1, pageSize: 5 }).then(res => {
    if (res.code === 200) {
      recentOrders.value = res.data.records || []
    }
  })

  // 如果URL中有订单号，则自动查询
  const orderNumberFromRoute = route.params.orderNumber
  if (orderNumberFromRoute && typeof orderNumberFromRoute === 'string') {
    trackingNumber.value = orderNumberFromRoute
    searchTracking()
  }
})

const searchTracking = async () => {
  if (!trackingNumber.value) {
    ElMessage.warning('请输入订单号或运输单号')
    return
  }

  searching.value = true
  try {
    const response = await orderApi.getByOrderNumber(trackingNumber.value)
    
    if (response.code !== 200 || !response.data) {
      ElMessage.warning('未找到该订单，请检查订单号是否正确。')
      trackingResult.value = null
      return
    }
    
    trackingResult.value = response.data

    // 如果订单有关联的运输信息，则获取运输跟踪记录
    if (response.data.transportId) {
      const trackingResponse: any = await orderApi.getTrackingInfo(trackingNumber.value)
      if (trackingResponse.code === 200 && trackingResponse.data) {
        trackingHistory.value = trackingResponse.data.sort((a: any, b: any) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
      }
    }
    
    searched.value = true
    ElMessage.success('查询成功！')
  } catch (error) {
    console.error('查询订单失败:', error)
    ElMessage.error('查询失败，请稍后重试')
  } finally {
    searching.value = false
  }
}

const quickSearch = (orderNumber: string) => {
  trackingNumber.value = orderNumber
  searchTracking()
}

const refreshTracking = () => {
  if (trackingResult.value) {
    searchTracking()
  } else {
    ElMessage.info('请先进行一次查询')
  }
}

const resetSearch = () => {
  trackingNumber.value = ''
  searched.value = false
  resetResults()
}

const resetResults = () => {
  trackingResult.value = null
  trackingHistory.value = []
}

const getStatusText = (status: OrderStatus | undefined) => {
  if (!status) return '未知状态';
  const map: Record<OrderStatus, string> = {
    pending: '待处理',
    confirmed: '已确认',
    picked_up: '已取货',
    in_transit: '运输中',
    delivered: '已送达',
    cancelled: '已取消'
  };
  return map[status] || '未知状态';
};

const getStatusType = (status: OrderStatus | undefined) => {
  if (!status) return 'info';
  const map: Record<OrderStatus, string> = {
    pending: 'warning',
    confirmed: 'primary',
    picked_up: 'info',
    in_transit: 'primary',
    delivered: 'success',
    cancelled: 'danger'
  };
  return map[status] || 'info';
};
</script>

<style scoped>
.tracking-page {
  max-width: 1000px;
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

.search-card {
  margin-bottom: 24px;
}

.search-section h3 {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.search-form {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.tracking-input {
  flex: 1;
  max-width: 400px;
}

.quick-search {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.quick-label {
  font-size: 14px;
  color: #606266;
  margin-right: 8px;
}

.tracking-result {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.result-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.tracking-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-item label {
  font-size: 14px;
  color: #606266;
}

.order-number {
  font-weight: 600;
  color: #409eff;
}

.route-section {
  margin-bottom: 24px;
}

.route-section h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.route-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 24px;
  position: relative;
}

.route-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  position: relative;
}

.route-item.end {
  margin-top: 32px;
}

.route-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-top: 4px;
  flex-shrink: 0;
}

.route-dot.start-dot {
  background: #67c23a;
}

.route-dot.end-dot {
  background: #f56c6c;
}

.route-line {
  position: absolute;
  left: 37px;
  top: 40px;
  width: 2px;
  height: 24px;
  background: #dcdfe6;
}

.route-content {
  flex: 1;
}

.route-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.route-address {
  font-size: 16px;
  color: #606266;
  margin-bottom: 8px;
}

.route-extra {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

.delivery-section {
  margin-bottom: 24px;
}

.delivery-section h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.info-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.info-icon {
  width: 32px;
  height: 32px;
  background: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
}

.info-content {
  flex: 1;
}

.info-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.transport-content {
  padding: 16px 0;
}

.transport-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.transport-number {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-item label {
  font-size: 14px;
  color: #606266;
  min-width: 80px;
}

.detail-item span {
  font-size: 14px;
  color: #303133;
}

.driver-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-top: 16px;
}

.driver-info h5 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.driver-details {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.driver-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.driver-item label {
  font-size: 14px;
  color: #606266;
}

.driver-item span {
  font-size: 14px;
  color: #303133;
}

.timeline {
  position: relative;
  padding-left: 32px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 11px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #dcdfe6;
}

.timeline-item {
  position: relative;
  margin-bottom: 24px;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -21px;
  top: 4px;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #dcdfe6;
}

.timeline-dot.active {
  background: #409eff;
  width: 12px;
  height: 12px;
  left: -22px;
}

.timeline-content {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.timeline-item.active .timeline-content {
  background: #e1f3d8;
  border-left: 4px solid #67c23a;
}

.timeline-time {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.timeline-location {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.timeline-status {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.timeline-notes {
  font-size: 14px;
  color: #909399;
}

.no-result, .search-prompt {
  text-align: center;
  padding: 40px;
}

.no-result-text h3, .prompt-text h3 {
  margin: 16px 0 8px 0;
  font-size: 18px;
  color: #303133;
}

.no-result-text p, .prompt-text p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .result-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .driver-details {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
