<template>
  <el-card class="box-card notifications-view">
    <template #header>
      <div class="card-header">
        <el-icon :size="20" style="margin-right: 8px;"><Bell /></el-icon>
        <span>消息通知</span>
        <div>
          <el-button @click="markAllAsRead" :disabled="unreadCount === 0">全部已读</el-button>
          <el-button type="danger" @click="clearAll" :disabled="notifications.length === 0">清空全部</el-button>
        </div>
      </div>
    </template>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    
    <div v-else-if="apiError" class="error-container">
      <el-empty description="无法加载通知列表，后端服务可能正在开发中。">
        <el-button type="primary" @click="fetchNotifications">点击重试</el-button>
      </el-empty>
    </div>

    <div v-else-if="notifications.length > 0" class="notification-list">
      <div
        v-for="notification in notifications"
        :key="notification.id"
        :class="['notification-item', { 'is-read': notification.status === 'read' }]"
        @click="markAsRead(notification)"
      >
        <div class="notification-content">
          <el-icon class="notification-icon" :size="22"><component :is="getIcon(notification.type)" /></el-icon>
          <div class="notification-details">
            <p class="notification-title">{{ notification.title }}</p>
            <p class="notification-message">{{ notification.content }}</p>
          </div>
        </div>
        <div class="notification-meta">
          <span class="notification-time">{{ formatTime(notification.createTime) }}</span>
          <el-tag :type="notification.status === 'read' ? 'info' : 'warning'" size="small">
            {{ notification.status === 'read' ? '已读' : '未读' }}
          </el-tag>
        </div>
      </div>
    </div>

    <el-empty v-else description="暂无新的通知"></el-empty>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { ElCard, ElButton, ElTag, ElMessageBox, ElMessage, ElIcon, ElEmpty, ElSkeleton } from 'element-plus';
import { Bell, SuccessFilled, WarningFilled, InfoFilled, QuestionFilled } from '@element-plus/icons-vue';
import { notificationApi } from '../utils/api';
import { getUserInfo } from '@/utils/auth'; // 导入获取用户信息的方法

interface Notification {
  id: number;
  userId: number;
  title: string;
  content: string; // Changed from message
  type: 'order' | 'system' | 'promotion' | 'default' | 'transport';
  status: 'read' | 'unread'; // Changed from isRead
  createTime: string; // Changed from createdAt
  readTime?: string;
}

const notifications = ref<Notification[]>([]);
const loading = ref(true);
const apiError = ref(false);

const fetchNotifications = async () => {
  loading.value = true;
  apiError.value = false;
  const userInfo = getUserInfo();
  if (!userInfo || !userInfo.id) {
    ElMessage.error('无法获取用户信息，请重新登录。');
    loading.value = false;
    apiError.value = true;
    return;
  }

  try {
    const response: any = await notificationApi.list(userInfo.id);
    if (response.code === 200) {
      notifications.value = response.data || []; // 确保在data为null时不会出错
    } else {
      ElMessage.error(response.message || '获取通知列表失败');
      apiError.value = true;
    }
  } catch (error) {
    console.error('获取通知列表失败:', error);
    ElMessage.error('获取通知列表失败，请稍后重试');
    apiError.value = true;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchNotifications);

const unreadCount = computed(() => notifications.value.filter(n => n.status === 'unread').length);

const getIcon = (type: Notification['type']) => {
  switch (type) {
    case 'order': return SuccessFilled;
    case 'system': return WarningFilled;
    case 'promotion': return InfoFilled;
    case 'transport': return InfoFilled; // Added for transport type
    default: return QuestionFilled;
  }
};

const markAsRead = async (notification: Notification) => {
  if (notification.status === 'read') return; // 如果已读，则不重复发送请求
  try {
    const response: any = await notificationApi.markAsRead(notification.id);
    if (response.code === 200) {
      const index = notifications.value.findIndex(n => n.id === notification.id);
      if (index !== -1) {
        notifications.value[index].status = 'read';
      }
      ElMessage.success('已标记为已读');
    } else {
      ElMessage.error(response.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const markAllAsRead = async () => {
  const userInfo = getUserInfo();
  if (!userInfo || !userInfo.id) {
    ElMessage.error('无法获取用户信息，请重新登录。');
    return;
  }
  try {
    const response: any = await notificationApi.markAllAsRead(userInfo.id);
    if (response.code === 200) {
      notifications.value.forEach(n => n.status = 'read');
      ElMessage.success('所有消息已标记为已读');
    } else {
      ElMessage.error(response.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const deleteOne = async (id: number) => {
  try {
    const response: any = await notificationApi.delete(id);
    if (response.code === 200) {
      const index = notifications.value.findIndex(n => n.id === id);
      if (index > -1) {
        notifications.value.splice(index, 1);
      }
      ElMessage.success('删除成功');
    } else {
      ElMessage.error(response.message || '删除失败');
    }
  } catch (error) {
    ElMessage.error('删除失败');
  }
};

const clearAll = async () => {
  const userInfo = getUserInfo();
  if (!userInfo || !userInfo.id) {
    ElMessage.error('无法获取用户信息，请重新登录。');
    return;
  }

  try {
    await ElMessageBox.confirm('确定要清空所有通知吗？此操作不可撤销。', '警告', {
      type: 'warning', confirmButtonText: '确定清空', cancelButtonText: '取消'
    })
    const response: any = await notificationApi.deleteAll(userInfo.id);
    if (response.code === 200) {
      notifications.value = [];
      ElMessage.success('所有通知已清空');
    } else {
      ElMessage.error(response.message || '操作失败');
    }
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('操作失败');
  }
};

const formatTime = (dateStr: string) => new Date(dateStr).toLocaleString();

</script>

<style scoped>
.notifications-page {
  padding: 24px;
  background-color: #f5f7fa;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.header-actions {
  display: flex;
  gap: 12px;
}
.notification-container {
  border-radius: 12px;
}
.notification-tabs .el-tabs__header {
  margin-bottom: 0;
}
.notification-list {
  min-height: 400px;
  padding: 8px;
}
.notification-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  transition: background-color 0.3s;
}
.notification-item:last-child {
  border-bottom: none;
}
.notification-item:hover {
  background-color: #ecf5ff;
}
.notification-item.unread {
  background-color: #fff;
  font-weight: bold;
}
.item-icon {
  font-size: 24px;
  color: #409eff;
}
.item-content {
  flex-grow: 1;
}
.item-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.item-title {
  font-size: 16px;
  margin: 0;
}
.item-time {
  font-size: 12px;
  color: #909399;
}
.item-message {
  margin: 0;
  font-size: 14px;
  color: #606266;
  font-weight: normal;
}
.item-actions {
  display: flex;
  gap: 8px;
}
.list-enter-active, .list-leave-active {
  transition: all 0.5s ease;
}
.list-enter-from, .list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.loading-container, .error-container {
  padding: 20px;
  text-align: center;
}
</style>
