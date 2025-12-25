<template>
  <div class="profile-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>个人资料</h2>
        <p>管理您的个人信息和账户设置</p>
      </div>
    </div>
    
    <div class="profile-container">
      <!-- 左侧导航 -->
      <div class="profile-sidebar">
        <div class="user-card">
          <div class="user-avatar">
            <div class="avatar-circle">
              {{ userInfo.username.charAt(0).toUpperCase() }}
            </div>
          </div>
          <div class="user-info">
            <h3>{{ userInfo.username }}</h3>
            <p>{{ userInfo.email || '暂未设置邮箱' }}</p>
            <el-tag :type="getRoleType(userInfo.role)" size="small">
              {{ getRoleText(userInfo.role) }}
            </el-tag>
          </div>
        </div>
        
        <div class="menu-list">
          <div 
            v-for="item in menuItems" 
            :key="item.key"
            class="menu-item"
            :class="{ active: activeTab === item.key }"
            @click="activeTab = item.key"
          >
            <el-icon class="menu-icon">
              <component :is="item.icon" />
            </el-icon>
            <span class="menu-text">{{ item.label }}</span>
          </div>
        </div>
      </div>
      
      <!-- 右侧内容 -->
      <div class="profile-main">
        <!-- 基本信息 -->
        <el-card v-show="activeTab === 'basic'" class="tab-card">
          <template #header>
            <div class="card-header">
              <h3>基本信息</h3>
              <el-button 
                v-if="!editing.basic" 
                type="primary" 
                @click="startEdit('basic')"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <div v-else class="edit-actions">
                <el-button @click="cancelEdit('basic')">取消</el-button>
                <el-button 
                  type="primary" 
                  :loading="saving.basic"
                  @click="saveBasicInfo"
                >
                  保存
                </el-button>
              </div>
            </div>
          </template>
          
          <el-form :model="editForm.basic" label-width="100px" size="large">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名">
                  <el-input 
                    v-if="editing.basic"
                    v-model="editForm.basic.username"
                    placeholder="请输入用户名"
                  />
                  <span v-else class="info-text">{{ userInfo.username }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input 
                    v-if="editing.basic"
                    v-model="editForm.basic.email"
                    placeholder="请输入邮箱"
                  />
                  <span v-else class="info-text">{{ userInfo.email || '-' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="手机号">
                  <el-input 
                    v-if="editing.basic"
                    v-model="editForm.basic.phone"
                    placeholder="请输入手机号"
                  />
                  <span v-else class="info-text">{{ userInfo.phone || '-' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="角色">
                  <span class="info-text">{{ getRoleText(userInfo.role) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="注册时间">
                  <span class="info-text">{{ formatDateTime(userInfo.createdAt) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最后登录">
                  <span class="info-text">{{ formatDateTime(userInfo.lastLoginAt) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
        
        <!-- 客户信息 -->
        <!--
        <el-card v-show="activeTab === 'customer'" class="tab-card">
          <template #header>
            <div class="card-header">
              <h3>客户信息</h3>
              <el-button 
                v-if="!currentCustomer && !editing.customer" 
                type="primary" 
                @click="startEdit('customer')"
              >
                <el-icon><Plus /></el-icon>
                关联客户
              </el-button>
              <el-button 
                v-else-if="currentCustomer && !editing.customer" 
                type="primary" 
                @click="startEdit('customer')"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <div v-else class="edit-actions">
                <el-button @click="cancelEdit('customer')">取消</el-button>
                <el-button 
                  type="primary" 
                  :loading="saving.customer"
                  @click="saveCustomerInfo"
                >
                  保存
                </el-button>
              </div>
            </div>
          </template>
          
          <div v-if="currentCustomer || editing.customer">
            <el-form :model="editForm.customer" label-width="100px" size="large">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="客户名称">
                    <el-input 
                      v-if="editing.customer"
                      v-model="editForm.customer.name"
                      placeholder="请输入客户名称"
                    />
                    <span v-else class="info-text">{{ currentCustomer?.name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系人">
                    <el-input 
                      v-if="editing.customer"
                      v-model="editForm.customer.contactPerson"
                      placeholder="请输入联系人"
                    />
                    <span v-else class="info-text">{{ currentCustomer?.contactPerson }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="联系电话">
                    <el-input 
                      v-if="editing.customer"
                      v-model="editForm.customer.phone"
                      placeholder="请输入联系电话"
                    />
                    <span v-else class="info-text">{{ currentCustomer?.phone }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="邮箱">
                    <el-input 
                      v-if="editing.customer"
                      v-model="editForm.customer.email"
                      placeholder="请输入邮箱"
                    />
                    <span v-else class="info-text">{{ currentCustomer?.email }}</span>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="地址">
                <el-input 
                  v-if="editing.customer"
                  v-model="editForm.customer.address"
                  placeholder="请输入地址"
                />
                <span v-else class="info-text">{{ currentCustomer?.address }}</span>
              </el-form-item>
              
              <el-form-item label="客户类型">
                <el-radio-group v-if="editing.customer" v-model="editForm.customer.customerType">
                  <el-radio label="INDIVIDUAL">个人客户</el-radio>
                  <el-radio label="CORPORATE">企业客户</el-radio>
                </el-radio-group>
                <span v-else class="info-text">{{ getCustomerTypeText(currentCustomer?.customerType) }}</span>
              </el-form-item>
            </el-form>
          </div>
          <el-empty v-else description="您还未关联客户信息"></el-empty>
        </el-card>
        -->
        
        <!-- 安全设置 -->
        <el-card v-show="activeTab === 'security'" class="tab-card">
          <template #header>
            <div class="card-header">
              <h3>安全设置</h3>
            </div>
          </template>
          
          <div class="security-section">
            <div class="security-item">
              <div class="security-info">
                <h4>登录密码</h4>
                <p>用于登录系统的密码，建议定期更换</p>
              </div>
              <el-button type="primary" @click="showPasswordDialog = true">
                修改密码
              </el-button>
            </div>
            
            <el-divider />
            
            <div class="security-item">
              <div class="security-info">
                <h4>登录日志</h4>
                <p>查看最近的登录记录</p>
              </div>
              <el-button @click="showLoginLog = !showLoginLog">
                {{ showLoginLog ? '隐藏' : '查看' }}日志
              </el-button>
            </div>
            
            <div v-show="showLoginLog" class="login-log">
              <el-table :data="loginHistory" style="width: 100%">
                <el-table-column prop="loginTime" label="登录时间" width="200">
                  <template #default="{ row }">
                    {{ formatDateTime(row.loginTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="ip" label="IP地址" width="150" />
                <el-table-column prop="device" label="设备信息" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
                      {{ row.status === 'success' ? '成功' : '失败' }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-card>
        
        <!-- 偏好设置 -->
        <el-card v-show="activeTab === 'preferences'" class="tab-card">
          <template #header>
            <div class="card-header">
              <h3>偏好设置</h3>
              <el-button 
                type="primary" 
                :loading="saving.preferences"
                @click="savePreferences"
              >
                保存设置
              </el-button>
            </div>
          </template>
          
          <el-form :model="preferences" label-width="120px" size="large">
            <el-form-item label="邮件通知">
              <el-switch
                v-model="preferences.emailNotification"
                active-text="开启"
                inactive-text="关闭"
              />
              <div class="form-note">接收订单状态更新等重要通知</div>
            </el-form-item>
            
            <el-form-item label="短信通知">
              <el-switch
                v-model="preferences.smsNotification"
                active-text="开启"
                inactive-text="关闭"
              />
              <div class="form-note">接收配送状态变更的短信提醒</div>
            </el-form-item>
            
            <el-form-item label="语言设置">
              <el-select v-model="preferences.language" placeholder="选择语言">
                <el-option label="简体中文" value="zh-CN" />
                <el-option label="English" value="en-US" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="时区设置">
              <el-select v-model="preferences.timezone" placeholder="选择时区">
                <el-option label="中国标准时间 (UTC+8)" value="Asia/Shanghai" />
                <el-option label="美国东部时间 (UTC-5)" value="America/New_York" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
    
    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showPasswordDialog"
      title="修改密码"
      width="400px"
      :before-close="handlePasswordDialogClose"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            show-password
            placeholder="请输入当前密码"
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPasswordDialog = false">取消</el-button>
          <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, shallowRef } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  User,
  UserFilled,
  Lock,
  Setting,
  Edit,
  Plus
} from '@element-plus/icons-vue'
import { userApi } from '../utils/api'
import { formatDateTime } from '../utils/formatters'
import { useRouter } from 'vue-router'

const router = useRouter()

// 类型定义
interface Customer {
  id: number;
  name: string;
  contactPerson: string;
  phone: string;
  email: string;
  customerType: 'individual' | 'company';
  address: string;
  creditRating: 'A' | 'B' | 'C' | 'D';
}
type Role = 'customer' | 'admin' | 'employee';

// 响应式数据
const activeTab = ref('basic')
const showPasswordDialog = ref(false)
const showLoginLog = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref<FormInstance>()

const userInfo = reactive({
  id: 0,
  username: '客户',
  email: '',
  phone: '',
  role: 'customer' as Role,
  createdAt: '',
  lastLoginAt: ''
})

// const currentCustomer = ref<Customer | null>(null)

const editing = reactive<{ [key: string]: boolean }>({
  basic: false,
  customer: false
})

const saving = reactive<{ [key: string]: boolean }>({
  basic: false,
  customer: false,
  preferences: false
})

const editForm = reactive({
  basic: {
    username: '',
    email: '',
    phone: ''
  },
  /*
  customer: {
    name: '',
    contactPerson: '',
    phone: '',
    email: '',
    customerType: 'company',
    address: ''
  },
  */
  password: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const preferences = reactive({
  emailNotification: true,
  smsNotification: true,
  language: 'zh-CN',
  timezone: 'Asia/Shanghai'
})

const loginHistory = ref([
  {
    loginTime: new Date().toISOString(),
    ip: '192.168.1.100',
    device: 'Chrome 120.0.0.0 / Windows 10',
    status: 'success'
  },
  {
    loginTime: new Date(Date.now() - 86400000).toISOString(),
    ip: '192.168.1.100',
    device: 'Chrome 120.0.0.0 / Windows 10',
    status: 'success'
  }
])

// 菜单配置
const menuItems = [
  { key: 'basic', label: '基本信息', icon: User },
  { key: 'customer', label: '客户信息', icon: UserFilled },
  { key: 'security', label: '安全设置', icon: Lock },
  { key: 'preferences', label: '偏好设置', icon: Setting }
]

// 密码验证规则
const passwordRules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 页面加载时执行
onMounted(() => {
  loadUserInfo()
})

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const storedUser = localStorage.getItem('userInfo');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser && parsedUser.id) {
        const response: any = await userApi.getProfile(parsedUser.id);
        if (response.code === 200) {
          Object.assign(userInfo, response.data);
          ElMessage.success('用户信息已更新');
        } else {
          // 如果API调用失败，仍使用本地数据
          Object.assign(userInfo, parsedUser);
          ElMessage.error(response.message || '无法从服务器更新用户信息');
        }
      } else {
        throw new Error('本地存储的用户信息格式不正确');
      }
    } else {
      throw new Error('未找到本地用户信息，请重新登录');
    }
  } catch (error: any) {
    console.error('加载用户信息失败：', error);
    ElMessage.error(error.message || '加载用户信息失败，请重新登录');
    router.push('/login');
  }
}

// 提交个人信息更新
const submitProfile = async () => {
  // ... existing code ...
}

/*
const loadCustomerInfo = async (customerId: number) => {
  try {
    const response = await customerApi.get(customerId)
    if (response.data.code === 200) {
      currentCustomer.value = response.data.data
    } else {
      ElMessage.error('加载关联客户信息失败')
    }
  } catch (error) {
    console.error('加载客户信息失败：', error)
  }
}
*/

// 加载偏好设置
const loadPreferences = () => {
  const savedPreferences = localStorage.getItem('userPreferences')
  if (savedPreferences) {
    Object.assign(preferences, JSON.parse(savedPreferences))
  }
}

// 开始编辑
const startEdit = (type: string) => {
  editing[type] = true
  
  if (type === 'basic') {
    Object.assign(editForm.basic, {
      username: userInfo.username,
      email: userInfo.email,
      phone: userInfo.phone
    })
  } else if (type === 'customer') {
    /*
    if (currentCustomer.value) {
      Object.assign(editForm.customer, currentCustomer.value)
    } else {
      Object.assign(editForm.customer, {
        name: '',
        contactPerson: '',
        phone: '',
        email: '',
        customerType: 'company',
        address: ''
      })
    }
    */
  }
}

// 取消编辑
const cancelEdit = (type: string) => {
  editing[type] = false
}

// 实现保存函数
const saveBasicInfo = async () => {
  saving.basic = true
  try {
    const response = await userApi.updateProfile(editForm.basic)
    if (response.data.code === 200) {
      Object.assign(userInfo, response.data.data)
      ElMessage.success('基本信息更新成功')
      editing.basic = false
    } else {
      ElMessage.error(response.data.message || '保存失败')
    }
  } catch (error) {
    console.error('保存基本信息失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    saving.basic = false
  }
}

/*
const saveCustomerInfo = async () => {
  saving.customer = true
  try {
    let response;
    if (currentCustomer.value && currentCustomer.value.id) {
      response = await customerApi.update(currentCustomer.value.id, editForm.customer)
    } else {
      response = await customerApi.create(editForm.customer)
    }
    
    if (response.data.code === 200) {
      currentCustomer.value = response.data.data
      ElMessage.success('客户信息保存成功')
      editing.customer = false
    } else {
      ElMessage.error(response.data.message || '保存失败')
    }
  } catch (error) {
    console.error('保存客户信息失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    saving.customer = false
  }
}
*/

// 保存偏好设置
const savePreferences = () => {
  saving.preferences = true
  
  setTimeout(() => {
    localStorage.setItem('userPreferences', JSON.stringify(preferences))
    saving.preferences = false
    ElMessage.success('设置保存成功')
  }, 500)
}

const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        const response = await userApi.updatePassword(passwordForm)
        if (response.data.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          showPasswordDialog.value = false
          // 可选：清除登录状态并跳转到登录页
        } else {
          ElMessage.error(response.data.message || '密码修改失败')
        }
      } catch (error) {
        console.error('密码修改失败:', error)
        ElMessage.error('密码修改失败，请重试')
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

// 关闭密码对话框
const handlePasswordDialogClose = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.clearValidate()
}

// 工具函数
const getRoleText = (role: Role) => {
  const roleMap: Record<Role, string> = {
    customer: '普通客户',
    admin: '管理员',
    employee: '员工'
  }
  return roleMap[role] || '未知角色'
}

const getRoleType = (role: Role) => {
  const typeMap: Record<Role, string> = {
    customer: 'primary',
    admin: 'success',
    employee: 'warning'
  }
  return typeMap[role] || 'info'
}

const getCustomerTypeText = (type: Customer['customerType']) => {
  if (!type) return ''
  const typeMap = {
    'individual': '个人客户',
    'company': '企业客户'
  }
  return typeMap[type] || type
}

const getCreditType = (rating: Customer['creditRating']) => {
  if (!rating) return 'info'
  const typeMap = {
    'A': 'success',
    'B': 'primary',
    'C': 'warning',
    'D': 'danger'
  }
  return typeMap[rating] || 'info'
}
</script>

<style scoped>
.profile-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.profile-container {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
}

.profile-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.user-card {
  background: white;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.user-avatar {
  margin-bottom: 16px;
}

.avatar-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  font-size: 32px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.user-info h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.user-info p {
  margin: 0 0 12px 0;
  color: #909399;
  font-size: 14px;
}

.menu-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item.active {
  background: #e1f3d8;
  color: #67c23a;
  border-right: 3px solid #67c23a;
}

.menu-icon {
  font-size: 16px;
}

.menu-text {
  font-size: 14px;
  font-weight: 500;
}

.profile-main {
  display: flex;
  flex-direction: column;
}

.tab-card {
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.edit-actions {
  display: flex;
  gap: 12px;
}

.info-text {
  color: #303133;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px;
}

.security-section {
  padding: 16px 0;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.security-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.security-info p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.login-log {
  margin-top: 16px;
}

.form-note {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

@media (max-width: 768px) {
  .profile-container {
    grid-template-columns: 1fr;
  }
  
  .menu-list {
    display: flex;
    overflow-x: auto;
  }
  
  .menu-item {
    flex-shrink: 0;
    min-width: 120px;
    justify-content: center;
    border-bottom: none;
    border-right: 1px solid #f0f0f0;
  }
  
  .menu-item:last-child {
    border-right: none;
  }
  
  .menu-item.active {
    border-right: 1px solid #f0f0f0;
    border-bottom: 3px solid #67c23a;
  }
}
</style>
