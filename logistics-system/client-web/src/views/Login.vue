<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-card">
        <div class="logo-section">
          <div class="logo">🚚</div>
          <h1>物流管理系统</h1>
          <p>客户端登录</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="formRules"
          class="login-form"
          size="large"
          status-icon
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <div class="demo-accounts">
            <h4>演示账号</h4>
            <div class="account-list">
              <div class="account-item" @click="fillAccount('alibaba', '123456')">
                <span class="role">企业客户</span>
                <span class="username">alibaba / 123456</span>
              </div>
              <div class="account-item" @click="fillAccount('tencent', '123456')">
                <span class="role">企业客户</span>
                <span class="username">tencent / 123456</span>
              </div>
            </div>
          </div>

          <div class="register-link">
            还没有账号？
            <router-link to="/register" class="link">立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<!--
<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    alert('请输入用户名和密码')
    return
  }

  loading.value = true

  try {
    // 模拟登录请求
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 保存用户信息到localStorage
    localStorage.setItem('token', 'user-token-' + Date.now())
    localStorage.setItem('userInfo', JSON.stringify({
      username: loginForm.username,
      role: 'user',
      realName: '雨辰',
      signature: '代码如诗，逻辑如画，用技术编织美好未来'
    }))

    alert('登录成功')
    router.push('/')
  } catch (error) {
    alert('登录失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>
-->

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { authApi } from '../utils/api'
import type { AxiosError } from 'axios'
import { md5 } from 'js-md5'

interface ApiErrorResponse {
  message: string;
  code?: number;
}

const router = useRouter()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loading.value = true
    
    const encryptedPassword = md5(loginForm.password)

    const response = await authApi.login({
        username: loginForm.username,
        password: encryptedPassword
      })

    if (response.success && response.data) {
      const { token, user } = response.data;
      
      if (token && user) {
        // 核心修复：确保只将 user 对象存入 localStorage
        localStorage.setItem('userInfo', JSON.stringify(user))
        localStorage.setItem('token', token)
        
        ElMessage.success('登录成功！')
        
        // 跳转到首页
        await router.push('/')
      } else {
        ElMessage.error('登录失败：无效的响应数据')
      }
    } else {
        ElMessage.error(`登录失败: ${response.message || '未知错误'}`)
    }
  } catch (error: unknown) {
    const axiosError = error as AxiosError<ApiErrorResponse>;
    const errorMsg = axiosError.response?.data?.message || axiosError.message || '登录失败，请检查网络连接或稍后重试';
    ElMessage.error(`登录请求失败: ${errorMsg}`);
  } finally {
    loading.value = false
  }
}

const fillAccount = (username: string, password: string) => {
  loginForm.username = username
  loginForm.password = password
}
</script>

<style scoped>
.login-page {
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
}

.login-container {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 400px;
  max-width: 90%;
  transition: all 0.3s ease;
}

.login-container:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
  transform: translateY(-5px);
}

.login-card {
  padding: 40px;
  display: flex;
  flex-direction: column;
}

.logo-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  font-size: 48px;
  line-height: 1;
  margin-bottom: 10px;
}

.logo-section h1 {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #333;
}

.logo-section p {
  font-size: 14px;
  color: #888;
  margin: 0;
}

.login-form .el-form-item {
  margin-bottom: 25px;
}

.login-form .el-input__wrapper {
  border-radius: 8px;
  padding: 4px 15px;
  transition: all 0.3s ease;
}

.login-form .el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.5);
  border-color: #667eea;
}

.login-btn {
  width: 100%;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  padding: 18px 0;
  height: auto;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  opacity: 0.9;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.login-footer {
  margin-top: 20px;
}

.demo-accounts {
  background-color: #f9fafb;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
}

.demo-accounts h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #555;
  font-weight: 600;
}

.account-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.account-item {
  font-size: 13px;
  color: #555;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 8px 12px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  display: flex;
  justify-content: space-between;
}

.account-item:hover {
  border-color: #764ba2;
  color: #764ba2;
  transform: translateX(3px);
}

.account-item .role {
  font-weight: 500;
}

.register-link {
  text-align: center;
  font-size: 14px;
  color: #666;
}

.register-link .link {
  color: #764ba2;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.3s ease;
}

.register-link .link:hover {
  text-decoration: underline;
}

/* 使用 :deep() 来修改Element Plus组件的内部样式 */
:deep(.el-input__inner) {
  height: 48px;
}
</style>
