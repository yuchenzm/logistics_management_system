<template>
  <div class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
      <div class="logo-container">
        <img :src="logo" alt="Logo" class="logo-img" />
        <h3 v-if="!isCollapse" class="logo-text">物流客户端</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :unique-opened="true"
      >
        <el-menu-item index="/">
          <el-icon><Odometer /></el-icon>
          <span>仪表板</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><Document /></el-icon>
          <span>我的订单</span>
        </el-menu-item>
        <el-menu-item index="/create-order">
          <el-icon><Plus /></el-icon>
          <span>创建订单</span>
        </el-menu-item>
        <el-menu-item index="/tracking">
          <el-icon><Location /></el-icon>
          <span>物流跟踪</span>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><User /></el-icon>
          <span>个人资料</span>
        </el-menu-item>
        <el-menu-item index="/addresses">
          <el-icon><MapLocation /></el-icon>
          <span>收货地址</span>
        </el-menu-item>
        <el-menu-item index="/feedback">
          <el-icon><ChatDotRound /></el-icon>
          <span>意见反馈</span>
        </el-menu-item>
        <el-menu-item index="/notifications">
          <el-icon><Bell /></el-icon>
          <span>消息通知</span>
        </el-menu-item>
      </el-menu>
      <div class="collapse-trigger" @click="toggleCollapse">
        <el-icon>
          <Fold v-if="!isCollapse" />
          <Expand v-else />
        </el-icon>
      </div>
    </el-aside>
    <div class="content-wrapper">
      <el-header class="navbar" height="60px">
        <div class="navbar-left">
          <div class="collapse-trigger-header" @click="toggleCollapse">
            <el-icon>
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
          </div>
        </div>
        <div class="navbar-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="userAvatar" />
              <span class="username">{{ userInfo.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>

      <!-- 底部个人信息区域 -->
      <el-footer class="footer" height="60px">
        <div class="footer-content">
          <div class="author-info">
            <el-icon><User /></el-icon>
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
      </el-footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  ArrowDown, User, Document, Plus, Location, MapLocation, 
  ChatDotRound, Bell, Odometer, Fold, Expand
} from '@element-plus/icons-vue'
import logo from '@/assets/logo.svg'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const userInfo = ref({
  username: '客户'
})

const activeMenu = computed(() => route.path)

const userAvatar = ref('https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png')

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command: 'profile' | 'logout') => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确认退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch {
      // 用户取消退出
    }
  }
}

onMounted(() => {
  try {
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      const parsedUserInfo = JSON.parse(storedUserInfo)
      userInfo.value = {
        username: parsedUserInfo.username || parsedUserInfo.realName || '客户'
      }
    }
  } catch (error) {
    console.error('获取用户信息失败：', error)
    userInfo.value = {
      username: '客户'
    }
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f5f7fa;
}

.navbar {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  flex-shrink: 0;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 10px;
  height: 60px;
  background-color: #2b3a4a;
  overflow: hidden;
}

.logo-img {
  height: 32px;
  width: 32px;
  margin-right: 12px;
}

.logo-text {
  color: white;
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: #5a6169;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.username {
  font-weight: 500;
}

.sidebar {
  background-color: #304156;
  position: relative;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar-menu {
  flex: 1;
  border: none;
  overflow-y: auto;
  overflow-x: hidden;
}

.el-menu:not(.el-menu--collapse) {
  width: 200px;
}

.collapse-trigger {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  cursor: pointer;
  font-size: 20px;
  color: #bfcbd9;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.3s;
}

.collapse-trigger:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.collapse-trigger-header {
  cursor: pointer;
  font-size: 20px;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.3s;
}

.collapse-trigger-header:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: opacity 0.3s ease;
}

.fade-transform-enter-from,
.fade-transform-leave-to {
  opacity: 0;
}

/* Element Plus 组件样式覆盖 */
:deep(.el-container) {
  height: 100vh !important;
  width: 100% !important;
  margin: 0;
  padding: 0;
}

:deep(.el-header) {
  padding: 0 !important;
  margin: 0;
  width: 100% !important;
}

:deep(.el-aside) {
  overflow: hidden;
  margin: 0;
  padding: 0;
}

:deep(.el-main) {
  padding: 0 !important;
  margin: 0;
  width: 100% !important;
  flex: 1;
}

.footer {
  flex-shrink: 0;
  background-color: #f5f7fa;
  border-top: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  padding: 0 20px;
  color: #909399;
  font-size: 14px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-name {
  font-weight: 500;
  color: #606266;
}

.separator {
  margin: 0 4px;
  color: #c0c4cc;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar {
    padding: 0 16px;
  }
  
  .logo h3 {
    font-size: 18px;
  }
  
  .user-info {
    padding: 4px 8px;
  }
  
  .username {
    display: none;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .sidebar {
    width: 64px !important;
  }
  
  .collapse-trigger {
    display: none;
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 12px;
  }
  
  .navbar {
    padding: 0 12px;
  }
}
</style>