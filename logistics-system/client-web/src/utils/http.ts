import axios, { type AxiosResponse, type AxiosError } from 'axios'
import { ElMessage, ElLoading } from 'element-plus'

// 创建axios实例
const http = axios.create({
  baseURL: '/api', // 使用相对路径，以便Vite代理能够生效
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 加载状态管理
class LoadingManager {
  private loadingCount = 0
  private loadingInstance: any = null

  show(text = '加载中...') {
    if (this.loadingCount === 0) {
      this.loadingInstance = ElLoading.service({
        lock: true,
        text,
        background: 'rgba(0, 0, 0, 0.7)',
      })
    }
    this.loadingCount++
  }

  hide() {
    this.loadingCount--
    if (this.loadingCount <= 0) {
      this.loadingCount = 0
      if (this.loadingInstance) {
        this.loadingInstance.close()
        this.loadingInstance = null
      }
    }
  }

  forceHide() {
    this.loadingCount = 0
    if (this.loadingInstance) {
      this.loadingInstance.close()
      this.loadingInstance = null
    }
  }
}

const loadingManager = new LoadingManager()

// 请求拦截器
http.interceptors.request.use(
  (config) => {
    // 显示加载状态
    if (config.showLoading !== false) {
      loadingManager.show(config.loadingText || '加载中...')
    }

    // 添加认证token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => {
    loadingManager.hide()
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  (response: AxiosResponse) => {
    loadingManager.hide()
    
    const { data } = response
    
    // 处理业务逻辑错误
    if (data.code && data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return data
  },
  (error: AxiosError) => {
    loadingManager.hide()
    
    // 处理HTTP错误
    if (error.response) {
      const { status, data } = error.response as any
      
      switch (status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || error.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error(error.message || '请求失败')
    }
    
    return Promise.reject(error)
  }
)

// 请求方法封装
export const request = {
  get<T = any>(url: string, params?: any, options?: any): Promise<T> {
    return http.get(url, { params, ...options })
  },

  post<T = any>(url: string, data?: any, options?: any): Promise<T> {
    return http.post(url, data, options)
  },

  put<T = any>(url: string, data?: any, options?: any): Promise<T> {
    return http.put(url, data, options)
  },

  delete<T = any>(url: string, params?: any, options?: any): Promise<T> {
    return http.delete(url, { params, ...options })
  },

  upload<T = any>(url: string, formData: FormData, options?: any): Promise<T> {
    return http.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      ...options,
    })
  },
}

// 加载状态管理器导出
export { loadingManager }

// 错误处理工具
export const errorHandler = {
  // 统一错误处理
  handle(error: any, customMessage?: string) {
    const message = customMessage || error.message || '操作失败'
    ElMessage.error(message)
    console.error('Error:', error)
  },

  // 验证错误处理
  handleValidation(errors: Record<string, string[]>) {
    const errorMessages = Object.values(errors).flat()
    if (errorMessages.length > 0) {
      ElMessage.error(errorMessages[0])
    }
  },

  // 异步错误处理
  async handleAsync<T>(
    fn: () => Promise<T>,
    errorMessage?: string
  ): Promise<T | null> {
    try {
      return await fn()
    } catch (error) {
      this.handle(error, errorMessage)
      return null
    }
  },
}

// 成功提示
export const successHandler = {
  show(message: string) {
    ElMessage.success(message)
  },
}

export default http

// 类型定义
declare module 'axios' {
  interface AxiosRequestConfig {
    showLoading?: boolean
    loadingText?: string
  }
} 