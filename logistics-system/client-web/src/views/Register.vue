<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-card">
        <div class="logo-section">
          <div class="logo">🚚</div>
          <h1>物流管理系统</h1>
          <p>客户端注册</p>
        </div>
        
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="formRules"
          class="register-form"
          size="large"
          status-icon
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              clearable
            />
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="请输入邮箱"
              prefix-icon="Message"
              clearable
            />
          </el-form-item>
          
          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              prefix-icon="Phone"
              clearable
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              clearable
            />
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              prefix-icon="Lock"
              show-password
              clearable
            />
          </el-form-item>
          
          <el-form-item>
            <el-button
              type="primary"
              class="register-btn"
              :loading="loading"
              @click="handleRegister"
            >
              {{ loading ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="register-footer">
          <div class="login-link">
            已有账号？
            <router-link to="/login" class="link">立即登录</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { authApi } from '../utils/api'

const router = useRouter()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/, message: '用户名只能包含字母、数字、下划线和中文', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    { 
      pattern: /^(?=.*[a-zA-Z])(?=.*\d).{6,}$/, 
      message: '密码必须包含字母和数字', 
      trigger: 'blur' 
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: any, callback: any) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    const valid = await registerFormRef.value.validate()
    if (!valid) return
    
    loading.value = true
    
    const response = await authApi.register({
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email,
      phone: registerForm.phone
    })
    
    if (response.data.code === 200) {
      ElMessage.success('注册成功！请使用新账号登录')
      
      // 跳转到登录页面
      router.push('/login')
    } else {
      ElMessage.error(response.data.message || '注册失败')
    }
  } catch (error) {
    console.log('注册失败：', error)
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  margin: 0;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

.register-container {
  width: 100%;
  max-width: 420px;
  margin: 0 auto;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}

.logo-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  font-size: 50px;
  margin-bottom: 15px;
  display: block;
}

.logo-section h1 {
  color: #2c3e50;
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo-section p {
  color: #7f8c8d;
  font-size: 14px;
  margin: 0;
}

.register-form {
  margin-bottom: 25px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

.register-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.register-footer {
  text-align: center;
}

.login-link {
  color: #7f8c8d;
  font-size: 14px;
}

.link {
  color: #3498db;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s ease;
}

.link:hover {
  color: #2980b9;
  text-decoration: underline;
}

:deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.3s ease;
  border: 1px solid #e1e6ef;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0,0,0,0.12);
  border-color: #3498db;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
  border-color: #667eea;
}

:deep(.el-form-item__error) {
  font-size: 12px;
}

@media (max-width: 480px) {
  .register-page {
    padding: 15px;
  }
  
  .register-card {
    padding: 30px 25px;
    margin: 10px 0;
  }
  
  .logo {
    font-size: 40px;
  }
  
  .logo-section h1 {
    font-size: 20px;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 18px;
  }
}
</style> 