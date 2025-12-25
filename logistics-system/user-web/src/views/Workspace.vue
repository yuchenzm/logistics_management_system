<template>
  <div class="workspace">
    <header class="header">
      <h1>物流管理系统 - 员工工作台</h1>
      <div class="user-info">
        <div class="user-profile">
          <div class="user-name">{{ userInfo.realName || userInfo.username }}</div>
          <div class="user-signature" v-if="userInfo.signature">{{ userInfo.signature }}</div>
        </div>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </header>

    <main class="main-content">
      <div class="content-inner">
        <div class="welcome-card">
          <div class="welcome-text">
            <h2>🎉 登录成功！</h2>
            <p>欢迎回来，开始高效的一天。</p>
          </div>
          <div class="current-time">
            <p>当前时间</p>
            <span>{{ currentTime }}</span>
          </div>
        </div>
        
        <div class="stats-grid">
          <div class="stat-card">
            <div class="label">今日任务</div>
            <div class="value">{{ stats.todayTasks }}</div>
          </div>
          <div class="stat-card">
            <div class="label">处理中</div>
            <div class="value">{{ stats.processing }}</div>
          </div>
          <div class="stat-card">
            <div class="label">已完成</div>
            <div class="value">{{ stats.completed }}</div>
          </div>
          <div class="stat-card">
            <div class="label">本月总计</div>
            <div class="value">{{ stats.monthlyTotal }}</div>
          </div>
        </div>
        
        <div class="panels-grid">
          <div class="panel quick-actions">
            <h3>快捷操作</h3>
            <div class="action-buttons">
              <button class="action-btn">处理订单</button>
              <button class="action-btn">更新状态</button>
              <button class="action-btn">查看报表</button>
              <button class="action-btn">任务管理</button>
            </div>
          </div>
          
          <div class="panel recent-activity">
            <h3>最近活动</h3>
            <div class="activity-list">
              <div class="activity-item">
                <span class="activity-time">10:30</span>
                <span class="activity-desc">处理订单 ORD2025010001</span>
              </div>
              <div class="activity-item">
                <span class="activity-time">09:15</span>
                <span class="activity-desc">更新运输状态</span>
              </div>
              <div class="activity-item">
                <span class="activity-time">08:00</span>
                <span class="activity-desc">开始工作</span>
              </div>
              <div class="activity-item">
                <span class="activity-time">昨日 17:45</span>
                <span class="activity-desc">完成订单 ORD2025010003</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <div class="author-info">
          <span class="author-icon">👤</span>
          <span class="author-name">雨辰</span>
          <span class="separator">·</span>
          <span class="signature">代码改变世界，创新驱动未来</span>
        </div>
        <div class="footer-links">
          <span class="copyright">© 2024 物流管理系统</span>
          <span class="separator">|</span>
          <span class="version">v1.0.0</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentTime = ref<string>('')
const userInfo = reactive({
  username: '员工',
  realName: '',
  signature: ''
})

const stats = reactive({
  todayTasks: 16,
  processing: 5,
  completed: 8,
  monthlyTotal: 45
})

let timer: number;

onMounted(() => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    try {
      const user = JSON.parse(storedUserInfo)
      Object.assign(userInfo, {
        username: user.username,
        realName: user.realName || '雨辰',
        signature: user.signature || '代码如诗，逻辑如画'
      })
    } catch (e) {
      console.error("Failed to parse user info from localStorage", e)
    }
  }
  
  updateTime()
  timer = window.setInterval(updateTime, 1000)
})

onBeforeUnmount(() => {
  clearInterval(timer)
})

const updateTime = () => {
  currentTime.value = new Date().toLocaleString('zh-CN', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false })
}

const logout = () => {
  if (confirm('确认退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');

html {
  scroll-behavior: smooth;
}

.workspace {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: #0d1a33;
  color: #cdd5e0;
  font-family: 'Roboto', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.header {
  flex-shrink: 0;
  background: rgba(19, 36, 66, 0.5);
  border-bottom: 1px solid rgba(0, 191, 255, 0.2);
  padding: 0 40px;
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(5px);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header h1 {
  font-size: 24px;
  font-weight: 500;
  color: #e0e6f0;
  margin: 0;
  letter-spacing: 1px;
  flex-grow: 1; /* 让标题占据多余空间，将右侧内容推开 */
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-profile {
  text-align: right;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #ffffff;
}

.user-signature {
  font-size: 12px;
  color: #00bfff;
  opacity: 0.8;
}

.logout-btn {
  padding: 8px 16px;
  background: transparent;
  color: #00bfff;
  border: 1px solid #00bfff;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: rgba(0, 191, 255, 0.1);
  box-shadow: 0 0 10px rgba(0, 191, 255, 0.3);
}

.main-content {
  flex-grow: 1;
  overflow: auto;
  padding: 30px 40px;
  padding-bottom: 80px; /* 为固定页脚留出空间 */
}

/* Custom Scrollbar */
.main-content::-webkit-scrollbar {
  width: 8px;
}

.main-content::-webkit-scrollbar-track {
  background: #0d1a33;
}

.main-content::-webkit-scrollbar-thumb {
  background-color: rgba(0, 191, 255, 0.3);
  border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 191, 255, 0.5);
}

.content-inner {
  max-width: 1400px;
  margin: 0 auto;
}

.welcome-card {
  background: rgba(13, 37, 70, 0.6);
  border-radius: 12px;
  padding: 25px 35px;
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid rgba(0, 191, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 24, 61, 0.3);
}

.welcome-card h2 {
  font-size: 28px;
  font-weight: 500;
  margin: 0 0 10px 0;
}

.welcome-card p {
  font-size: 16px;
  opacity: 0.8;
  margin: 0;
}

.current-time {
  text-align: right;
}

.current-time p {
  font-size: 14px;
  opacity: 0.7;
}

.current-time span {
  font-size: 28px;
  font-weight: 700;
  color: #00bfff;
  letter-spacing: 1.5px;
  text-shadow: 0 0 8px rgba(0, 191, 255, 0.4);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
  margin-bottom: 30px;
}

.stat-card {
  background: rgba(19, 36, 66, 0.5);
  padding: 25px;
  border-radius: 12px;
  border: 1px solid rgba(0, 191, 255, 0.2);
  text-align: center;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  background: rgba(19, 36, 66, 0.8);
  border-color: rgba(0, 191, 255, 0.5);
  box-shadow: 0 8px 25px rgba(0, 191, 255, 0.2);
}

.stat-card h3 {
  font-size: 16px;
  color: #cdd5e0;
  font-weight: 400;
  margin: 0 0 10px 0;
}

.stat-card .label {
  font-size: 14px;
  color: #8aacc8;
  margin-bottom: 10px;
}

.stat-card .value {
  font-size: 44px;
  font-weight: 700;
  color: #e0e6f0;
}

.panels-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
}

.panel {
  background: rgba(19, 36, 66, 0.5);
  padding: 30px;
  border-radius: 12px;
  border: 1px solid rgba(0, 191, 255, 0.2);
}

.panel h3 {
  font-size: 20px;
  color: #ffffff;
  margin: 0 0 20px 0;
  padding-bottom: 15px;
  border-bottom: 1px solid rgba(0, 191, 255, 0.2);
}

.action-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.action-btn {
  padding: 12px;
  background: rgba(0, 191, 255, 0.1);
  border: 1px solid rgba(0, 191, 255, 0.3);
  color: #cdd5e0;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.action-btn:hover {
  background: rgba(0, 191, 255, 0.2);
  color: #ffffff;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 14px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(0, 191, 255, 0.1);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  font-weight: 500;
  color: #00bfff;
  min-width: 80px;
  text-align: right;
}

.activity-desc {
  color: #cdd5e0;
}

.footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  z-index: 10;
  box-sizing: border-box; /* 确保 padding 不会影响总宽度 */
  
  /* flex-shrink: 0; */ /* 在 fixed 布局下不再需要 */
  background-color: rgba(19, 36, 66, 0.5); /* 和 header 统一 */
  border-top: 1px solid rgba(0, 191, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px 40px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1400px;
  /* padding: 0 40px; */ /* 移除，因为父元素已有 padding */
  color: rgba(205, 213, 224, 0.6);
  font-size: 13px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-icon {
  font-size: 16px;
}

.author-name {
  font-weight: 500;
  color: rgba(205, 213, 224, 0.8);
}

.separator {
  margin: 0 4px;
  color: rgba(205, 213, 224, 0.4);
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style> 