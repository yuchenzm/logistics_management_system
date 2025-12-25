<template>
  <div class="dashboard">
    <!-- 欢迎卡片 -->
    <div class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2 class="greeting">🎉 欢迎回来, {{ userInfo.username }}!</h2>
          <p class="date-text">今天是 {{ currentDate }}, 祝您有美好的一天!</p>
        </div>
        <div class="welcome-illustration">
          <img :src="illustrationUrl" alt="Welcome Illustration" />
        </div>
      </div>
    </div>

    <!-- 数据统计 -->
    <div v-if="!apiError" class="stats-grid">
      <el-card class="stat-card orders-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon><DocumentCopy /></el-icon>
          </div>
          <div class="stat-info">
            <h3>总订单数</h3>
            <div class="stat-number">{{ stats.totalOrders }}</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card transit-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon><Van /></el-icon>
          </div>
          <div class="stat-info">
            <h3>运输中</h3>
            <div class="stat-number">{{ stats.inTransit }}</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card completed-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <h3>已完成</h3>
            <div class="stat-number">{{ stats.completed }}</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card amount-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-info">
            <h3>总费用</h3>
            <div class="stat-number">¥{{ stats.totalAmount?.toFixed(2) }}</div>
          </div>
        </div>
      </el-card>
    </div>
    <el-card v-else class="error-card">
      <el-empty description="后端服务连接失败或正在开发中...">
        <el-button type="primary" @click="loadDashboardData" :loading="loading">重试</el-button>
      </el-empty>
    </el-card>

    <!-- 快捷操作 -->
    <el-card class="quick-actions" shadow="hover">
      <template #header>
        <h3>快捷操作</h3>
      </template>
      <div class="action-buttons">
        <el-button type="primary" size="large" @click="router.push('/create-order')" :icon="Plus">创建订单</el-button>
        <el-button type="info" size="large" @click="router.push('/orders')" :icon="List">查看订单</el-button>
        <el-button type="warning" size="large" @click="router.push('/tracking')" :icon="Search">物流跟踪</el-button>
        <el-button type="success" size="large" @click="router.push('/profile')" :icon="User">个人设置</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElCard, ElButton, ElIcon, ElEmpty } from 'element-plus'
import { orderApi } from '../utils/api'
import { DocumentCopy, Van, CircleCheck, Money, Plus, List, Search, User } from '@element-plus/icons-vue'
import illustrationUrl from '@/assets/welcome-illustration.svg'

const router = useRouter()

// 用户信息
const userInfo = ref({ username: '用户' })
const currentDate = computed(() => new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' }))

// 数据统计
const stats = reactive({
  totalOrders: 0,
  inTransit: 0,
  completed: 0,
  totalAmount: 0
})

const loading = ref(true)
const apiError = ref(false)

const loadDashboardData = async () => {
  loading.value = true
  apiError.value = false
  try {
    const response: any = await orderApi.getSummary()
    if (response.code === 200) {
      Object.assign(stats, response.data)
    } else {
      ElMessage.error(response.message || '加载统计数据失败')
      apiError.value = true
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    apiError.value = true
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 加载用户信息
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
  
  loadDashboardData()
})
</script>

<style scoped>
.dashboard {
  padding: 24px;
  background-color: #f0f2f5;
}

.welcome-card {
  background: linear-gradient(135deg, #6a82fb 0%, #fc5c7d 100%);
  color: white;
  padding: 32px;
  border-radius: 16px;
  margin-bottom: 24px;
  box-shadow: 0 10px 30px -10px rgba(106, 130, 251, 0.5);
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.welcome-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px -10px rgba(106, 130, 251, 0.6);
}

.welcome-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background-image: radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
  background-size: 25px 25px;
  animation: bg-pan 20s linear infinite;
}

@keyframes bg-pan {
  0% { transform: translate(0, 0); }
  100% { transform: translate(25px, 25px); }
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  gap: 20px;
  flex-wrap: wrap;
}

.welcome-text .greeting {
  margin: 0 0 12px 0;
  font-size: 32px;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.welcome-text .date-text {
  margin: 0;
  font-size: 16px;
  opacity: 0.85;
}

.welcome-illustration {
  width: 180px;
  height: 180px;
}
.welcome-illustration img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    justify-content: center;
    text-align: center;
  }

  .welcome-illustration {
    order: -1;
    width: 140px;
    height: 140px;
    margin-bottom: 16px;
  }

  .welcome-text .greeting {
    font-size: 24px;
  }

  .welcome-text .date-text {
    font-size: 14px;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 12px;
  border: none;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease;
}
.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 32px;
  color: white;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}
.orders-card .stat-icon { background-color: #409EFF; }
.transit-card .stat-icon { background-color: #E6A23C; }
.completed-card .stat-icon { background-color: #67C23A; }
.amount-card .stat-icon { background-color: #F56C6C; }

.stat-info h3 {
  margin: 0 0 8px 0;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.stat-info .stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.quick-actions {
  border-radius: 12px;
}
.action-buttons {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}
.error-card {
  margin-bottom: 24px;
  text-align: center;
}
</style> 