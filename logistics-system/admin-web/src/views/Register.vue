<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <h2>物流管理系统</h2>
        <p>管理员注册</p>
      </div>
      
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-item">
          <input
            v-model="registerForm.username"
            type="text"
            placeholder="请输入用户名"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="registerForm.realName"
            type="text"
            placeholder="请输入真实姓名"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="registerForm.email"
            type="email"
            placeholder="请输入邮箱"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="registerForm.phone"
            type="tel"
            placeholder="请输入手机号"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-item">
          <button type="submit" :disabled="loading" class="register-button">
            {{ loading ? '注册中...' : '立即注册' }}
          </button>
        </div>
      </form>
      
      <div class="login-link">
        <p>已有账号？ <a href="/login" @click.prevent="goToLogin">立即登录</a></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  realName: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const handleRegister = async () => {
  if (!registerForm.username || !registerForm.realName || !registerForm.email || 
      !registerForm.phone || !registerForm.password || !registerForm.confirmPassword) {
    alert('请填写完整的注册信息')
    return
  }
  
  if (registerForm.password !== registerForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  
  if (registerForm.password.length < 6) {
    alert('密码长度不能少于6位')
    return
  }
  
  loading.value = true
  
  try {
    // 模拟注册请求
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    alert('注册成功！请使用新账号登录')
    router.push('/login')
  } catch (error) {
    alert('注册失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
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
  overflow-y: auto;
}

.register-box {
  background: white;
  border-radius: 16px;
  padding: 50px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 480px;
  text-align: center;
}

.register-header {
  margin-bottom: 40px;
}

.register-header h2 {
  color: #2c3e50;
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.register-header p {
  color: #7f8c8d;
  font-size: 16px;
  margin: 0;
  font-weight: 400;
}

.register-form {
  width: 100%;
  margin-bottom: 30px;
}

.form-item {
  margin-bottom: 24px;
}

.form-input {
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

.form-input:focus {
  outline: none;
  border-color: #4285f4;
  background: white;
  box-shadow: 0 0 0 3px rgba(66, 133, 244, 0.1);
}

.form-input::placeholder {
  color: #95a5a6;
  font-weight: 400;
}

.register-button {
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

.register-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(66, 133, 244, 0.3);
}

.register-button:active {
  transform: translateY(0);
}

.register-button:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.login-link {
  margin-top: 25px;
  text-align: center;
}

.login-link p {
  margin: 0;
  color: #6c757d;
  font-size: 14px;
}

.login-link a {
  color: #4285f4;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.login-link a:hover {
  color: #34a853;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-container {
    padding: 15px;
  }
  
  .register-box {
    padding: 40px 30px;
    max-width: 100%;
  }
  
  .register-header h2 {
    font-size: 28px;
  }
  
  .form-input,
  .register-button {
    height: 48px;
    font-size: 16px;
  }
}
</style> 