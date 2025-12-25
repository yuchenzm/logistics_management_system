<template>
  <div class="order-detail-page">
    <div class="header">
      <h1>订单详情</h1>
      <div class="user-info">
        <span>欢迎，{{ userInfo.username }}！</span>
        <button @click="goBack" class="back-btn">返回订单</button>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </div>
    
    <div class="content">
      <div v-if="loading" class="loading-container">
        <div class="loading-text">加载中...</div>
      </div>
      
      <div v-else-if="orderDetail" class="order-container">
        <!-- 订单基本信息 -->
        <div class="info-card">
          <div class="card-header">
            <h3>订单信息</h3>
            <div class="order-status">
              <span 
                :class="'status-tag ' + getStatusClass(orderDetail.orderStatus)"
              >
                {{ getStatusText(orderDetail.orderStatus) }}
              </span>
            </div>
          </div>
          
          <div class="info-grid">
            <div class="info-item">
              <label>订单号：</label>
              <span class="order-number">{{ orderDetail.orderNumber }}</span>
            </div>
            <div class="info-item">
              <label>客户ID：</label>
              <span>{{ orderDetail.customerId }}</span>
            </div>
            <div class="info-item">
              <label>优先级：</label>
              <span 
                :class="'priority-tag ' + getPriorityClass(orderDetail.priority)"
              >
                {{ getPriorityText(orderDetail.priority) }}
              </span>
            </div>
            <div class="info-item">
              <label>支付状态：</label>
              <span 
                :class="'payment-tag ' + getPaymentStatusClass(orderDetail.paymentStatus)"
              >
                {{ getPaymentStatusText(orderDetail.paymentStatus) }}
              </span>
            </div>
            <div class="info-item">
              <label>创建时间：</label>
              <span>{{ formatDateTime(orderDetail.createdAt) }}</span>
            </div>
            <div class="info-item">
              <label>更新时间：</label>
              <span>{{ formatDateTime(orderDetail.updatedAt) }}</span>
            </div>
          </div>
        </div>
        
        <!-- 地址信息 -->
        <div class="info-card">
          <div class="card-header">
            <h3>地址信息</h3>
          </div>
          
          <div class="address-section">
            <div class="address-card origin">
              <div class="address-header">
                <div class="address-icon">📍</div>
                <h4>取货地址</h4>
              </div>
              <div class="address-content">
                <div class="address-text">{{ orderDetail.originAddress }}</div>
                <div class="address-city" v-if="orderDetail.originCity">
                  {{ orderDetail.originCity }}{{ orderDetail.originProvince ? ', ' + orderDetail.originProvince : '' }}
                </div>
              </div>
            </div>
            
            <div class="address-arrow">→</div>
            
            <div class="address-card destination">
              <div class="address-header">
                <div class="address-icon">🎯</div>
                <h4>送达地址</h4>
              </div>
              <div class="address-content">
                <div class="address-text">{{ orderDetail.destinationAddress }}</div>
                <div class="address-city" v-if="orderDetail.destinationCity">
                  {{ orderDetail.destinationCity }}{{ orderDetail.destinationProvince ? ', ' + orderDetail.destinationProvince : '' }}
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 时间信息 -->
        <div class="info-card">
          <div class="card-header">
            <h3>时间安排</h3>
          </div>
          
          <div class="time-grid">
            <div class="time-item">
              <label>取货日期：</label>
              <span>{{ formatDate(orderDetail.pickupDate) }}</span>
            </div>
            <div class="time-item">
              <label>送达日期：</label>
              <span>{{ formatDate(orderDetail.deliveryDate) }}</span>
            </div>
            <div class="time-item">
              <label>预期送达：</label>
              <span>{{ formatDate(orderDetail.expectedDeliveryDate) }}</span>
            </div>
          </div>
        </div>
        
        <!-- 货物信息 -->
        <div class="info-card">
          <div class="card-header">
            <h3>货物信息</h3>
          </div>
          
          <div class="goods-grid">
            <div class="goods-item">
              <div class="goods-icon">📦</div>
              <div class="goods-info">
                <div class="goods-label">总重量</div>
                <div class="goods-value">{{ orderDetail.totalWeight }} kg</div>
              </div>
            </div>
            <div class="goods-item">
              <div class="goods-icon">📐</div>
              <div class="goods-info">
                <div class="goods-label">总体积</div>
                <div class="goods-value">{{ orderDetail.totalVolume }} m³</div>
              </div>
            </div>
            <div class="goods-item">
              <div class="goods-icon">💰</div>
              <div class="goods-info">
                <div class="goods-label">总费用</div>
                <div class="goods-value amount">¥{{ orderDetail.totalAmount || 0 }}</div>
              </div>
            </div>
          </div>
          
          <div v-if="orderDetail.specialInstructions" class="special-instructions">
            <label>特殊说明：</label>
            <div class="instructions-content">{{ orderDetail.specialInstructions }}</div>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button 
            v-if="canTrack(orderDetail.orderStatus)"
            class="btn btn-primary"
            @click="trackOrder"
          >
            物流跟踪
          </button>
          <button 
            v-if="orderDetail.orderStatus === 'pending'"
            class="btn btn-danger"
            @click="cancelOrder"
            :disabled="cancelling"
          >
            {{ cancelling ? '取消中...' : '取消订单' }}
          </button>
          <button 
            class="btn btn-info"
            @click="printOrder"
          >
            打印订单
          </button>
        </div>
      </div>
      
      <div v-else class="error-container">
        <div class="error-icon">❌</div>
        <div class="error-text">
          <h3>订单不存在</h3>
          <p>未找到相关订单信息，请检查订单号是否正确</p>
        </div>
        <button class="btn btn-primary" @click="goBack">返回订单列表</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { orderApi } from '../utils/api'

const router = useRouter()
const route = useRoute()
const loading = ref(true)
const cancelling = ref(false)
const orderDetail = ref(null)

const userInfo = reactive({
  username: '客户'
})

onMounted(async () => {
  // 获取用户信息
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    const user = JSON.parse(storedUserInfo)
    userInfo.username = user.username || user.realName || '客户'
  }
  
  // 加载订单详情
  await loadOrderDetail()
})

const loadOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = route.params.id as string
    
    if (!orderId) {
      alert('订单ID不能为空')
      return
    }
    
    const response = await orderApi.getOrderDetail(parseInt(orderId))
    if (response.data.code === 200) {
      orderDetail.value = response.data.data
    } else {
      alert(response.data.message || '获取订单详情失败')
    }
  } catch (error) {
    console.log('获取订单详情失败：', error)
    alert('获取订单详情失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const getStatusText = (status: string) => {
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

const getStatusClass = (status: string) => {
  const classMap = {
    'pending': 'warning',
    'confirmed': 'info',
    'picked_up': 'primary',
    'in_transit': 'primary',
    'delivered': 'success',
    'cancelled': 'danger'
  }
  return classMap[status] || 'info'
}

const getPriorityText = (priority: string) => {
  const priorityMap = {
    'low': '低',
    'normal': '普通',
    'high': '高',
    'urgent': '紧急'
  }
  return priorityMap[priority] || '普通'
}

const getPriorityClass = (priority: string) => {
  const classMap = {
    'low': 'info',
    'normal': 'default',
    'high': 'warning',
    'urgent': 'danger'
  }
  return classMap[priority] || 'default'
}

const getPaymentStatusText = (status: string) => {
  const statusMap = {
    'pending': '待支付',
    'paid': '已支付',
    'partial': '部分支付',
    'refunded': '已退款'
  }
  return statusMap[status] || status
}

const getPaymentStatusClass = (status: string) => {
  const classMap = {
    'pending': 'warning',
    'paid': 'success',
    'partial': 'warning',
    'refunded': 'info'
  }
  return classMap[status] || 'info'
}

const formatDateTime = (datetime: string) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString('zh-CN')
}

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const canTrack = (status: string) => {
  return ['confirmed', 'picked_up', 'in_transit', 'delivered'].includes(status)
}

const trackOrder = () => {
  if (orderDetail.value) {
    router.push({ 
      name: 'tracking', 
      query: { orderNumber: orderDetail.value.orderNumber }
    })
  }
}

const cancelOrder = async () => {
  if (!orderDetail.value) return
  
  if (confirm(`确定要取消订单 ${orderDetail.value.orderNumber} 吗？`)) {
    try {
      cancelling.value = true
      const response = await orderApi.updateOrderStatus(orderDetail.value.id, 'cancelled')
      if (response.data.code === 200) {
        alert('订单已取消')
        orderDetail.value.orderStatus = 'cancelled'
      } else {
        alert(response.data.message || '取消订单失败')
      }
    } catch (error) {
      console.log('取消订单失败：', error)
      alert('取消订单失败，请稍后重试')
    } finally {
      cancelling.value = false
    }
  }
}

const printOrder = () => {
  window.print()
}

const goBack = () => {
  router.push('/orders')
}

const logout = () => {
  if (confirm('确认退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('customerId')
    router.push('/login')
  }
}
</script>

<style scoped>
.order-detail-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  overflow-y: auto;
  margin: 0;
  box-sizing: border-box;
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 20px 30px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header h1 {
  color: #2c3e50;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 16px;
  color: #495057;
  font-weight: 500;
}

.back-btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 152, 219, 0.3);
}

.logout-btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(231, 76, 60, 0.3);
}

.content {
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.loading-container, .error-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-align: center;
}

.loading-text {
  font-size: 18px;
  color: #666;
}

.order-container {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.info-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header h3 {
  color: #2c3e50;
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.status-tag, .priority-tag, .payment-tag {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-tag.warning, .priority-tag.warning, .payment-tag.warning {
  background: #fff3cd;
  color: #856404;
}

.status-tag.info, .priority-tag.info, .payment-tag.info {
  background: #cce7ff;
  color: #0066cc;
}

.status-tag.primary, .priority-tag.primary, .payment-tag.primary {
  background: #cce7ff;
  color: #0066cc;
}

.status-tag.success, .priority-tag.success, .payment-tag.success {
  background: #d4edda;
  color: #155724;
}

.status-tag.danger, .priority-tag.danger, .payment-tag.danger {
  background: #f8d7da;
  color: #721c24;
}

.priority-tag.default {
  background: #f8f9fa;
  color: #495057;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-item label {
  font-weight: 600;
  color: #555;
  min-width: 100px;
  flex-shrink: 0;
}

.info-item span {
  color: #333;
}

.order-number {
  font-weight: 600;
  color: #3498db;
  font-size: 16px;
}

.address-section {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 30px;
  align-items: center;
}

.address-card {
  background: #f8f9fa;
  border-radius: 15px;
  padding: 25px;
  border: 1px solid #e9ecef;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.address-icon {
  font-size: 24px;
}

.address-header h4 {
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.address-text {
  color: #333;
  font-size: 15px;
  line-height: 1.6;
  margin-bottom: 8px;
}

.address-city {
  color: #666;
  font-size: 14px;
}

.address-arrow {
  font-size: 24px;
  color: #3498db;
  font-weight: bold;
  text-align: center;
}

.time-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.time-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 10px;
  border: 1px solid #e9ecef;
}

.time-item label {
  font-weight: 600;
  color: #555;
  min-width: 80px;
}

.goods-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 25px;
}

.goods-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
}

.goods-icon {
  font-size: 28px;
}

.goods-info {
  flex: 1;
}

.goods-label {
  color: #666;
  font-size: 14px;
  margin-bottom: 5px;
}

.goods-value {
  color: #2c3e50;
  font-size: 18px;
  font-weight: 600;
}

.goods-value.amount {
  color: #27ae60;
}

.special-instructions {
  padding: 20px;
  background: #fff3cd;
  border-radius: 10px;
  border: 1px solid #ffeeba;
}

.special-instructions label {
  font-weight: 600;
  color: #856404;
  display: block;
  margin-bottom: 10px;
}

.instructions-content {
  color: #856404;
  line-height: 1.6;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 152, 219, 0.3);
}

.btn-danger {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(231, 76, 60, 0.3);
}

.btn-info {
  background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
  color: white;
}

.btn-info:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(23, 162, 184, 0.3);
}

.error-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.error-text h3 {
  color: #2c3e50;
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 10px 0;
}

.error-text p {
  color: #666;
  font-size: 16px;
  margin: 0 0 30px 0;
}

@media print {
  .header, .action-buttons {
    display: none !important;
  }
  
  .order-detail-page {
    position: static;
    background: white;
    height: auto;
  }
  
  .content {
    padding: 20px;
  }
  
  .info-card {
    background: white;
    box-shadow: none;
    border: 1px solid #ddd;
    page-break-inside: avoid;
  }
}

@media (max-width: 768px) {
  .header {
    padding: 15px 20px;
    flex-direction: column;
    gap: 15px;
  }
  
  .header h1 {
    font-size: 24px;
  }
  
  .user-info {
    gap: 15px;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .content {
    padding: 20px;
  }
  
  .info-card {
    padding: 20px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .address-section {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .address-arrow {
    transform: rotate(90deg);
  }
  
  .time-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .goods-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }
  
  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .info-item label {
    min-width: auto;
  }
}
</style> 