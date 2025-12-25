<template>
  <div class="dashboard">
    <div class="header">
      <h1>物流管理系统 - 仪表板</h1>
      <div class="user-info">
        <div class="user-profile">
          <div class="user-name">{{ userInfo.realName || userInfo.username }}</div>
          <div class="user-signature" v-if="userInfo.signature">{{ userInfo.signature }}</div>
        </div>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </div>
    
    <div class="content">
      <div class="welcome-card">
        <h2>🎉 登录成功！</h2>
        <p>欢迎使用物流管理系统管理员后台</p>
        <p>当前时间：{{ currentTime }}</p>
      </div>
      
      <div class="stats-grid">
        <div class="stat-card" @click="navigateTo('/orders')">
          <div class="stat-icon">📦</div>
          <h3>今日订单</h3>
          <div class="stat-number">{{ stats.todayOrders }}</div>
          <div class="stat-trend">↗ +15%</div>
        </div>
        <div class="stat-card" @click="navigateTo('/transports')">
          <div class="stat-icon">🚛</div>
          <h3>运输中</h3>
          <div class="stat-number">{{ stats.inTransit }}</div>
          <div class="stat-trend">→ 持平</div>
        </div>
        <div class="stat-card" @click="navigateTo('/customers')">
          <div class="stat-icon">👥</div>
          <h3>总客户数</h3>
          <div class="stat-number">{{ stats.totalCustomers }}</div>
          <div class="stat-trend">↗ +8%</div>
        </div>
        <div class="stat-card" @click="navigateTo('/drivers')">
          <div class="stat-icon">👨‍💼</div>
          <h3>活跃司机</h3>
          <div class="stat-number">{{ stats.activeDrivers }}</div>
          <div class="stat-trend">↗ +2</div>
        </div>
      </div>
      
      <div class="quick-actions">
        <h3>快捷操作</h3>
        <div class="action-buttons">
          <button class="action-btn" @click="navigateToAdd('/orders', 'add')">
            <span class="btn-icon">📝</span>
            新建订单
          </button>
          <button class="action-btn" @click="navigateTo('/customers')">
            <span class="btn-icon">👥</span>
            客户管理
          </button>
          <button class="action-btn" @click="navigateTo('/drivers')">
            <span class="btn-icon">👨‍💼</span>
            司机管理
          </button>
          <button class="action-btn" @click="navigateTo('/vehicles')">
            <span class="btn-icon">🚛</span>
            车辆管理
          </button>
          <button class="action-btn" @click="navigateTo('/warehouses')">
            <span class="btn-icon">🏢</span>
            仓库管理
          </button>
          <button class="action-btn" @click="navigateTo('/goods')">
            <span class="btn-icon">📦</span>
            货物管理
          </button>
          <button class="action-btn" @click="navigateTo('/transports')">
            <span class="btn-icon">🚚</span>
            运输管理
          </button>
          <button class="action-btn" @click="navigateTo('/expenses')">
            <span class="btn-icon">💰</span>
            费用管理
          </button>
        </div>
      </div>

      <div class="recent-section">
        <div class="recent-orders">
          <h3>最近订单</h3>
          <div class="order-list">
            <div v-for="order in recentOrders" :key="order.id" class="order-item" @click="navigateTo('/orders')">
              <div class="order-info">
                <div class="order-number">{{ order.orderNumber }}</div>
                <div class="order-customer">{{ order.customer }}</div>
              </div>
              <div class="order-status" :class="`status-${order.status}`">
                {{ getOrderStatusText(order.status) }}
              </div>
            </div>
          </div>
        </div>

        <div class="system-status">
          <h3>系统状态</h3>
          <div class="status-list">
            <div class="status-item">
              <span class="status-label">数据库</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">Redis缓存</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">消息队列</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">存储空间</span>
              <span class="status-value">75%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentTime = ref('')
const userInfo = reactive({
  username: '管理员',
  realName: '',
  signature: ''
})

const stats = reactive({
  todayOrders: 23,
  inTransit: 12,
  totalCustomers: 156,
  activeDrivers: 8
})

const recentOrders = ref([
  { id: 1, orderNumber: 'ORD202501001', customer: '阿里巴巴集团', status: 'in_transit' },
  { id: 2, orderNumber: 'ORD202501002', customer: '腾讯科技', status: 'delivered' },
  { id: 3, orderNumber: 'ORD202501003', customer: '京东集团', status: 'pending' },
  { id: 4, orderNumber: 'ORD202501004', customer: '字节跳动', status: 'confirmed' },
  { id: 5, orderNumber: 'ORD202501005', customer: '美团', status: 'picked_up' }
])

onMounted(() => {
  // 获取用户信息
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    const user = JSON.parse(storedUserInfo)
    Object.assign(userInfo, {
      username: user.username,
      realName: user.realName || '雨辰',
      signature: user.signature || '代码如诗，逻辑如画，用技术编织美好未来'
    })
  }
  
  // 更新时间
  updateTime()
  setInterval(updateTime, 1000)
})

const updateTime = () => {
  currentTime.value = new Date().toLocaleString('zh-CN')
}

const logout = () => {
  if (confirm('确认退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }
}

// 导航到指定页面
const navigateTo = (path) => {
  router.push(path)
}

// 导航到添加页面（将来可以扩展为直接打开新增对话框）
const navigateToAdd = (path, action) => {
  router.push(path)
  // 这里可以后续添加直接打开新增对话框的逻辑
}

// 获取订单状态文本
const getOrderStatusText = (status) => {
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
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  padding: 20px 30px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h1 {
  color: #2c3e50;
  font-size: 24px;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-profile {
  text-align: right;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.user-signature {
  font-size: 12px;
  color: #7f8c8d;
  font-style: italic;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.logout-btn {
  padding: 8px 16px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: #c0392b;
}

.content {
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
  margin-bottom: 30px;
}

.welcome-card h2 {
  color: #27ae60;
  margin-bottom: 15px;
}

.welcome-card p {
  color: #7f8c8d;
  margin: 8px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.15);
}

.stat-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.stat-card h3 {
  color: #7f8c8d;
  font-size: 14px;
  margin: 10px 0;
  font-weight: 500;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #2c3e50;
  margin: 10px 0;
}

.stat-trend {
  font-size: 12px;
  color: #27ae60;
  font-weight: 500;
}

.quick-actions {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.quick-actions h3 {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 18px;
}

.action-buttons {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 15px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-icon {
  font-size: 16px;
}

.recent-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.recent-orders, .system-status {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.recent-orders h3, .system-status h3 {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 18px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #e1e8ed;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.order-item:hover {
  background: #f8f9fa;
  border-color: #409eff;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-number {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.order-customer {
  font-size: 12px;
  color: #7f8c8d;
}

.order-status {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending { background: #fff3e0; color: #ef6c00; }
.status-confirmed { background: #e3f2fd; color: #1565c0; }
.status-picked_up { background: #f3e5f5; color: #7b1fa2; }
.status-in_transit { background: #e0f2fe; color: #0277bd; }
.status-delivered { background: #e8f5e8; color: #2e7d32; }
.status-cancelled { background: #ffebee; color: #c62828; }

.status-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f1f3f4;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  color: #5f6368;
  font-size: 14px;
}

.status-value {
  font-weight: 500;
  color: #2c3e50;
}

.status-value.online {
  color: #27ae60;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .action-buttons {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .recent-section {
    grid-template-columns: 1fr;
  }
  
  .header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
}
</style> 