<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
        <div class="logo">
          <h3 v-if="!isCollapse">物流管理</h3>
          <h3 v-else>物</h3>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          :collapse="isCollapse"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse-transition="false"
        >
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>仪表板</span>
          </el-menu-item>
          
          <el-menu-item index="/orders">
            <el-icon><Document /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
          
          <el-menu-item index="/customers">
            <el-icon><User /></el-icon>
            <span>客户管理</span>
          </el-menu-item>
          
          <el-menu-item index="/drivers">
            <el-icon><Avatar /></el-icon>
            <span>司机管理</span>
          </el-menu-item>
          
          <el-menu-item index="/vehicles">
            <el-icon><Van /></el-icon>
            <span>车辆管理</span>
          </el-menu-item>
          
          <el-menu-item index="/transports">
            <el-icon><Box /></el-icon>
            <span>运输管理</span>
          </el-menu-item>
          
          <el-menu-item index="/deliveries">
            <el-icon><Van /></el-icon>
            <span>配送管理</span>
          </el-menu-item>
          
          <el-sub-menu index="warehouse" :popper-append-to-body="false">
            <template #title>
              <el-icon><OfficeBuilding /></el-icon>
              <span>仓储管理</span>
            </template>
            <el-menu-item index="/warehouses">
              <el-icon><House /></el-icon>
              <span>仓库管理</span>
            </el-menu-item>
            <el-menu-item index="/goods">
              <el-icon><Goods /></el-icon>
              <span>货物管理</span>
            </el-menu-item>
            <el-menu-item index="/inventory">
              <el-icon><Grid /></el-icon>
              <span>库存管理</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="finance" :popper-append-to-body="false">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>财务管理</span>
            </template>
            <el-menu-item index="/expenses">
              <el-icon><CreditCard /></el-icon>
              <span>费用管理</span>
            </el-menu-item>
            <el-menu-item index="/shipping-rates">
              <el-icon><Coin /></el-icon>
              <span>运费管理</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/suppliers">
            <el-icon><Shop /></el-icon>
            <span>供应商管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区域 -->
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header class="navbar" height="60px">
          <div class="navbar-left">
            <el-icon
              class="collapse-btn"
              @click="toggleCollapse"
            >
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
            
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
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
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 主要内容 -->
        <el-main>
          <router-view />
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
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Avatar, Box, Document, User, OfficeBuilding, House, Goods, 
  Grid, Money, CreditCard, Shop, Van, Coin, Odometer,
  Fold, Expand, ArrowDown
} from "@element-plus/icons-vue";

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const userInfo = ref({
  username: '雨辰'
})

const activeMenu = computed(() => route.path)

// 页面标题映射
const pageTitleMap = {
  '/dashboard': '仪表板',
  '/orders': '订单管理',
  '/customers': '客户管理',
  '/drivers': '司机管理',
  '/vehicles': '车辆管理',
  '/transports': '运输管理',
  '/deliveries': '配送管理',
  '/warehouses': '仓库管理',
  '/goods': '货物管理',
  '/inventory': '库存管理',
  '/expenses': '费用管理',
  '/shipping-rates': '运费管理',
  '/suppliers': '供应商管理'
}

const currentPageTitle = computed(() => {
  return pageTitleMap[route.path] || '仪表板'
})

const userAvatar = ref('https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png')

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command) => {
  if (command === 'logout') {
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
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
}

.sidebar {
  background-color: #304156;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  position: relative !important;
  z-index: 1000;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 2px;
  height: 100%;
  background: linear-gradient(to bottom, rgba(64, 158, 255, 0.3), rgba(64, 158, 255, 0.1));
  transition: opacity 0.3s ease;
}

.sidebar:hover::before {
  opacity: 0.8;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 18px;
  border-bottom: 1px solid #434c5e;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform-origin: center;
  flex-shrink: 0;
}

.logo h3 {
  transition: all 0.2s ease-in-out;
  white-space: nowrap;
  overflow: hidden;
  margin: 0;
}

.sidebar-menu {
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding-bottom: 20px;
}

/* 自定义滚动条样式 */
.sidebar-menu::-webkit-scrollbar {
  width: 6px;
}

.sidebar-menu::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.sidebar-menu::-webkit-scrollbar-thumb {
  background: rgba(64, 158, 255, 0.5);
  border-radius: 3px;
  transition: background 0.3s ease;
}

.sidebar-menu::-webkit-scrollbar-thumb:hover {
  background: rgba(64, 158, 255, 0.8);
}

/* Firefox 滚动条样式 */
.sidebar-menu {
  scrollbar-width: thin;
  scrollbar-color: rgba(64, 158, 255, 0.5) rgba(255, 255, 255, 0.1);
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 200px;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.navbar {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  font-size: 18px;
  cursor: pointer;
  color: #5a6169;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 8px;
  border-radius: 6px;
  position: relative;
  overflow: hidden;
}

.collapse-btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
  transition: all 0.3s ease;
  transform: translate(-50%, -50%);
}

.collapse-btn:hover::before {
  width: 100%;
  height: 100%;
  border-radius: 6px;
}

.collapse-btn:hover {
  color: #409eff;
  transform: scale(1.1);
}

.collapse-btn:active {
  transform: scale(0.95);
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
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.username {
  font-weight: 500;
  transition: all 0.3s ease;
}

.footer {
  background: white;
  border-top: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px;
  box-shadow: 0 -1px 4px rgba(0, 21, 41, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  font-size: 14px;
  color: #5a6169;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-info .el-icon {
  color: #409eff;
  font-size: 16px;
}

.author-name {
  font-weight: 600;
  color: #409eff;
  transition: all 0.3s ease;
}

.signature {
  font-style: italic;
  color: #8c939d;
  position: relative;
  overflow: hidden;
}

.signature::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(64, 158, 255, 0.1), transparent);
  transition: left 2s ease;
}

.footer:hover .signature::before {
  left: 100%;
}

.footer:hover .author-name {
  color: #337ecc;
  transform: scale(1.05);
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
}

.separator {
  color: #dcdfe6;
  font-weight: 300;
}

.copyright {
  color: #8c939d;
}

.version {
  color: #409eff;
  font-weight: 500;
  padding: 2px 8px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 12px;
  font-size: 12px;
  transition: all 0.3s ease;
}

.footer:hover .version {
  background: rgba(64, 158, 255, 0.2);
  transform: scale(1.05);
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }
  
  .author-info, .footer-links {
    flex-wrap: wrap;
    justify-content: center;
  }
}

/* Element Plus 容器组件样式 */
:deep(.el-container) {
  height: 100vh;
}

:deep(.el-main) {
  background: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}

:deep(.el-header) {
  padding: 0;
}

:deep(.el-footer) {
  padding: 0;
}
</style> 