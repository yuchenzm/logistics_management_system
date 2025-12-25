<template>
  <div class="demo-page">
    <div class="header">
      <h1>功能演示 & API状态</h1>
      <button @click="goBack" class="back-btn">返回</button>
    </div>

    <div class="content">
      <!-- API Status Section -->
      <div class="widget">
        <div class="widget-header">
          <h2><i class="fas fa-network-wired"></i> API接口状态</h2>
          <button @click="runApiChecks" :disabled="isTesting" class="btn btn-primary">
            {{ isTesting ? '检测中...' : '重新检测' }}
          </button>
        </div>
        <div class="api-status-list">
          <div v-if="apiStatuses.length === 0 && !isTesting" class="empty-state">
            点击 "重新检测" 开始检查API状态
          </div>
          <div v-for="api in apiStatuses" :key="api.name" class="api-status-item">
            <span :class="['status-icon', getStatusClass(api.status)]">
              {{ getStatusIcon(api.status) }}
            </span>
            <div class="api-info">
              <span class="api-name">{{ api.name }}</span>
              <span class="api-endpoint">{{ api.endpoint }}</span>
            </div>
            <span class="api-message">{{ api.message }}</span>
          </div>
        </div>
      </div>
      
      <!-- Feature Demo Section -->
      <div class="widget">
        <div class="widget-header">
          <h2><i class="fas fa-vial"></i> 功能演示</h2>
          <p>基于API状态，以下功能将使用真实API或模拟数据。</p>
        </div>
        <!-- Add more feature demo buttons here -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { apiChecker, type ApiStatus } from '../utils/api-checker'
import { ElLoading } from 'element-plus'

const router = useRouter()
const isTesting = ref(false)
const apiStatuses = ref<ApiStatus[]>([])

const runApiChecks = async () => {
  isTesting.value = true
  const loading = ElLoading.service({ text: '正在全面检测API...', background: 'rgba(0, 0, 0, 0.8)' })
  try {
    apiStatuses.value = await apiChecker.checkAllApis()
    apiChecker.showApiReport()
  } finally {
    isTesting.value = false
    loading.close()
  }
}

onMounted(() => {
  runApiChecks()
})

const getStatusClass = (status: ApiStatus['status']) => {
  return {
    available: 'status-success',
    error: 'status-error',
    unknown: 'status-unknown'
  }[status]
}

const getStatusIcon = (status: ApiStatus['status']) => {
  return {
    available: '✅',
    error: '❌',
    unknown: '❓'
  }[status]
}

const goBack = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
/* Add FontAwesome if you can, otherwise use text icons */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css');

.demo-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  overflow-y: auto;
  padding: 20px;
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 8px 32px rgba(0,0,0,0.1);
}

.header h1 {
  color: #2c3e50;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
}

.content {
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.demo-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
  backdrop-filter: blur(10px);
}

.demo-section {
  margin-bottom: 40px;
}

.demo-section h2 {
  color: #2c3e50;
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
}

.api-test-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.api-test-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  border: 1px solid #e1e8ed;
}

.api-test-card h3 {
  color: #2c3e50;
  margin: 0 0 15px 0;
  font-size: 18px;
}

.api-test-card button {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 10px;
}

.api-test-card button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
}

.api-test-card button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.test-result {
  padding: 10px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
}

.test-result.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.test-result.warning {
  background: #fff3cd;
  color: #856404;
  border: 1px solid #ffeaa7;
}

.test-result.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.test-result:not(.success):not(.warning):not(.error) {
  background: #f8f9fa;
  color: #6c757d;
  border: 1px solid #dee2e6;
}

.feature-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background: #f8f9fa;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-btn.active {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
}

.demo-content {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.demo-content h3 {
  color: #2c3e50;
  margin: 0 0 20px 0;
  font-size: 20px;
}

.demo-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.demo-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.demo-btn.primary {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
}

.demo-btn.info {
  background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
  color: white;
}

.demo-btn.warning {
  background: linear-gradient(135deg, #ffc107 0%, #e0a800 100%);
  color: #212529;
}

.demo-btn.success {
  background: linear-gradient(135deg, #28a745 0%, #218838 100%);
  color: white;
}

.demo-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.demo-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.demo-item {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #e1e8ed;
}

.item-info h4 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 16px;
}

.item-info p {
  margin: 0 0 8px 0;
  color: #6c757d;
  font-size: 14px;
}

.item-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #6c757d;
  color: white;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.action-btn.danger {
  background: #dc3545;
}

.status-pending { color: #ffc107; }
.status-confirmed { color: #17a2b8; }
.status-picked_up { color: #28a745; }
.status-in_transit { color: #007bff; }
.status-delivered { color: #28a745; }
.status-cancelled { color: #dc3545; }

.default-badge {
  background: #28a745;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.read { color: #6c757d; }
.unread { color: #dc3545; font-weight: bold; }

.customer-type {
  background: #e9ecef;
  color: #495057;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.status-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  text-align: center;
}

.status-card h3 {
  color: #2c3e50;
  margin: 0 0 15px 0;
  font-size: 18px;
}

.status-indicator {
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  margin-bottom: 10px;
  display: inline-block;
}

.status-indicator.online {
  background: #d4edda;
  color: #155724;
}

.status-indicator.offline {
  background: #f8d7da;
  color: #721c24;
}

.status-card p {
  color: #6c757d;
  margin: 0;
  font-size: 14px;
}

.widget {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.widget-header h2 {
  font-size: 20px;
  color: #333;
}
.api-status-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.api-status-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f7f7f7;
  border-radius: 8px;
}
.status-icon {
  font-size: 20px;
  margin-right: 12px;
}
.api-info {
  flex-grow: 1;
}
.api-name {
  font-weight: bold;
  display: block;
}
.api-endpoint {
  font-size: 12px;
  color: #666;
  font-family: monospace;
}
.api-message {
  font-size: 14px;
  color: #555;
}
.status-success { color: #2ecc71; }
.status-error { color: #e74c3c; }
.status-unknown { color: #f39c12; }

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .content {
    padding: 20px;
  }
  
  .demo-container {
    padding: 20px;
  }
  
  .api-test-grid {
    grid-template-columns: 1fr;
  }
  
  .demo-actions {
    flex-direction: column;
  }
  
  .demo-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .item-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .status-grid {
    grid-template-columns: 1fr;
  }
}
</style> 