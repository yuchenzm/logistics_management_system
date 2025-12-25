<template>
  <div class="feedback-page-wrapper">
    <el-card class="box-card feedback-view">
      <template #header>
        <div class="card-header">
          <el-icon :size="20" style="margin-right: 8px;"><ChatDotRound /></el-icon>
          <span>我的反馈</span>
          <el-button class="button" type="primary" :icon="Plus" @click="dialogVisible = true">提交新反馈</el-button>
        </div>
      </template>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="apiError" class="error-container">
        <el-empty description="无法加载反馈列表，后端服务可能正在开发中。">
          <el-button type="primary" @click="fetchFeedback">点击重试</el-button>
        </el-empty>
      </div>

      <el-timeline v-else-if="feedbackList.length > 0" class="feedback-timeline">
        <el-timeline-item
          v-for="item in feedbackList"
          :key="item.id"
          :timestamp="formatDateTime(item.createdAt)"
          :type="getStatusType(item.status)"
          hollow
        >
          <h4>{{ item.title }}</h4>
          <p class="feedback-content">{{ item.content }}</p>
          <el-tag size="small">{{ getTypeText(item.type) }}</el-tag>
          <div v-if="item.reply" class="reply-section">
            <strong>官方回复：</strong>
            <p>{{ item.reply }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无反馈历史记录"></el-empty>
    </el-card>

    <el-dialog v-model="dialogVisible" title="提交新反馈" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="反馈类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="suggestion"></el-option>
            <el-option label="界面问题" value="ui_issue"></el-option>
            <el-option label="性能问题" value="performance"></el-option>
            <el-option label="其他问题" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请一句话描述您的问题"></el-input>
        </el-form-item>
        <el-form-item label="详细内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请详细描述您的问题或建议..."></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting" size="large">提交反馈</el-button>
          <el-button @click="dialogVisible = false" size="large">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElCard, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElButton, ElTimeline, ElTimelineItem, ElTag, ElIcon, ElEmpty, ElSkeleton } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Plus, ChatDotRound } from '@element-plus/icons-vue'
import { feedbackApi } from '../utils/api'

interface FeedbackItem {
  id: number;
  title: string;
  content: string;
  type: string;
  status: string;
  reply?: string;
  createdAt: string;
}

const formRef = ref<FormInstance>()
const submitting = ref(false)
const loading = ref(true)
const apiError = ref(false)
const feedbackList = ref<FeedbackItem[]>([])

const form = reactive({
  type: '',
  title: '',
  content: '',
})

const rules = {
  type: [{ required: true, message: '请选择反馈类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入详细内容', trigger: 'blur' }]
}

const dialogVisible = ref(false)

const fetchFeedback = async () => {
  loading.value = true;
  apiError.value = false;
  try {
    const userInfoStr = localStorage.getItem('userInfo');
    if (!userInfoStr) throw new Error('用户未登录');
    const userInfo = JSON.parse(userInfoStr);
    const userId = userInfo?.id;
    if (!userId) throw new Error('无法获取用户ID');

    const response: any = await feedbackApi.list(userId);
    if (response.code === 200) {
      feedbackList.value = response.data;
    } else {
      ElMessage.error(response.message || '获取反馈列表失败');
      apiError.value = true;
    }
  } catch (error: any) {
    console.error('获取反馈列表失败:', error);
    ElMessage.error(error.message || '获取反馈列表失败，请稍后重试');
    apiError.value = true;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchFeedback);

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const response: any = await feedbackApi.submit(form)
        if (response.code === 200) {
          ElMessage.success('反馈提交成功！');
          dialogVisible.value = false;
          formRef.value?.resetFields();
          await fetchFeedback();
        } else {
          ElMessage.error(response.message || '提交失败');
        }
      } catch (error) {
        ElMessage.error('提交失败，请重试');
      } finally {
        submitting.value = false
      }
    }
  })
}

const formatDateTime = (dateTimeStr: string) => {
    if (!dateTimeStr) return 'N/A';
    const date = new Date(dateTimeStr);
    return date.toLocaleString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}

const getStatusType = (status: string): 'primary' | 'success' | 'warning' | 'info' | 'danger' => {
    const map = {
        'pending': 'warning',
        'processing': 'primary',
        'resolved': 'success',
        'closed': 'info'
    };
    return (map[status as keyof typeof map] || 'info') as 'primary' | 'success' | 'warning' | 'info' | 'danger';
}

const getTypeText = (type: string) => {
    const map = {
        'suggestion': '功能建议',
        'ui_issue': '界面问题',
        'performance': '性能问题',
        'other': '其他问题'
    };
    return map[type as keyof typeof map] || '未知类型';
}
</script>

<style scoped>
.feedback-page-wrapper {
  padding: 24px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  font-size: 24px;
  color: #303133;
}
.page-header p {
  color: #606266;
}
.content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.form-card, .history-card {
  border-radius: 12px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
h3 .el-icon {
  vertical-align: middle;
  margin-right: 8px;
}
.timeline-container {
  min-height: 200px;
}
.feedback-content {
  color: #606266;
  margin: 8px 0;
}
.reply-section {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 8px 12px;
  margin-top: 8px;
  font-size: 14px;
}
.reply-section p {
  margin: 4px 0 0 0;
  color: #303133;
}
.loading-container, .error-container {
  padding: 20px;
  text-align: center;
}
</style>
