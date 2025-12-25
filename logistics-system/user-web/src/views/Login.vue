<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>物流管理系统</h2>
        <p>员工/司机登录</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-item">
          <input
            v-model="loginForm.username"
            type="text"
            placeholder="请输入员工账号"
            required
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            required
          />
        </div>
        
        <div class="form-item">
          <button type="submit" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
      </form>
      
      <div class="demo-account">
        <p>演示账号：</p>
        <p>员工：employee1 / 123456</p>
        <p>司机：driver1 / 123456</p>
      </div>
      
      <div class="register-link">
        <p>还没有账号？ <a href="/register" @click.prevent="goToRegister">立即注册</a></p>
      </div>
    </div>
  </div>
</template>

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

<style scoped>
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  margin: 0;
  box-sizing: border-box;
}

.login-box {
  background: white;
  border-radius: 16px;
  padding: 50px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 420px;
  text-align: center;
}

.login-header {
  margin-bottom: 40px;
}

.login-header h2 {
  color: #2c3e50;
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.login-header p {
  color: #7f8c8d;
  font-size: 16px;
  margin: 0;
  font-weight: 400;
}

.login-form {
  width: 100%;
  margin-bottom: 30px;
}

.form-item {
  margin-bottom: 24px;
}

.form-item input {
  width: 100%;
  height: 52px;
  padding: 0 20px;
  border: 1px solid #e1e8ed;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
  box-sizing: border-box;
  background: #f8fafe;
  color: #2c3e50;
}

.form-item input:focus {
  outline: none;
  border-color: #4285f4;
  background: white;
  box-shadow: 0 0 0 3px rgba(66, 133, 244, 0.1);
}

.form-item input::placeholder {
  color: #95a5a6;
  font-weight: 400;
}

.form-item button {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #4285f4 0%, #34a853 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
}

.form-item button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(66, 133, 244, 0.3);
}

.form-item button:active {
  transform: translateY(0);
}

.form-item button:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.demo-account {
  margin-top: 25px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 10px;
  font-size: 14px;
  color: #6c757d;
  line-height: 1.6;
}

.demo-account p {
  margin: 6px 0;
}

.demo-account p:first-child {
  font-weight: 600;
  color: #495057;
  margin-bottom: 10px;
}

.register-link {
  margin-top: 20px;
  text-align: center;
}

.register-link p {
  margin: 0;
  color: #6c757d;
  font-size: 14px;
}

.register-link a {
  color: #4285f4;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.register-link a:hover {
  color: #34a853;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-container {
    padding: 15px;
  }
  
  .login-box {
    padding: 40px 30px;
    max-width: 100%;
  }
  
  .login-header h2 {
    font-size: 28px;
  }
  
  .form-item input,
  .form-item button {
    height: 48px;
    font-size: 16px;
  }
}
</style> 