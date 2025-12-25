import { request } from './http'
import { ElMessage } from 'element-plus'

export interface ApiStatus {
  name: string
  endpoint: string
  status: 'available' | 'error' | 'unknown'
  message: string
}

export class ApiChecker {
  private static instance: ApiChecker
  private apiStatuses: Map<string, ApiStatus> = new Map()

  static getInstance(): ApiChecker {
    if (!ApiChecker.instance) {
      ApiChecker.instance = new ApiChecker()
    }
    return ApiChecker.instance
  }

  // 检查基础API状态
  async checkApiHealth(): Promise<boolean> {
    try {
      const response = await request.get('/health', {}, { showLoading: false })
      return response.code === 200 || response.status === 'UP'
    } catch (error) {
      // 这里的 health 接口可能不存在，所以只打印警告
      console.warn('API健康检查接口(/health)可能不存在，请确认后端是否提供。')
      return false
    }
  }

  // 检查用户认证API
  async checkAuthApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '用户认证',
      endpoint: '/users/login',
      status: 'unknown',
      message: ''
    }

    try {
      // 尝试访问API文档接口
      await request.get('/api/docs', {}, { showLoading: false })
      status.status = 'available'
      status.message = 'API可用'
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('auth', status)
    return status
  }

  // 检查订单API
  async checkOrderApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '订单管理',
      endpoint: '/orders',
      status: 'unknown',
      message: ''
    }

    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.id) {
        await request.get(`/orders/customer/${userInfo.id}`, {}, { showLoading: false })
      } else {
        await request.get('/orders', {}, { showLoading: false })
      }
      status.status = 'available'
      status.message = 'API可用'
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('order', status)
    return status
  }

  // 检查地址API
  async checkAddressApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '地址管理',
      endpoint: '/addresses',
      status: 'unknown',
      message: ''
    }

    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.id) {
        await request.get(`/addresses/user/${userInfo.id}`, {}, { showLoading: false })
        status.status = 'available'
        status.message = 'API可用'
      } else {
        status.status = 'error'
        status.message = '需要用户登录'
      }
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('address', status)
    return status
  }

  // 检查通知API
  async checkNotificationApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '消息通知',
      endpoint: '/notifications',
      status: 'unknown',
      message: ''
    }

    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.id) {
        await request.get(`/notifications/user/${userInfo.id}`, {}, { showLoading: false })
        status.status = 'available'
        status.message = 'API可用'
      } else {
        status.status = 'error'
        status.message = '需要用户登录'
      }
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('notification', status)
    return status
  }

  // 检查反馈API
  async checkFeedbackApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '意见反馈',
      endpoint: '/feedback',
      status: 'unknown',
      message: ''
    }

    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.id) {
        await request.get(`/feedback/user/${userInfo.id}`, {}, { showLoading: false })
        status.status = 'available'
        status.message = 'API可用'
      } else {
        status.status = 'error'
        status.message = '需要用户登录'
      }
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('feedback', status)
    return status
  }

  // 检查运输API
  async checkTransportApi(): Promise<ApiStatus> {
    const status: ApiStatus = {
      name: '物流跟踪',
      endpoint: '/transports',
      status: 'unknown',
      message: ''
    }

    try {
      await request.post('/transports/page', {
        pageNum: 1,
        pageSize: 1
      }, { showLoading: false })
      status.status = 'available'
      status.message = 'API可用'
    } catch (error: any) {
      status.status = 'error'
      status.message = error.message || 'API不可用'
    }

    this.apiStatuses.set('transport', status)
    return status
  }

  // 检查所有API状态
  async checkAllApis(): Promise<ApiStatus[]> {
    const checks = [
      this.checkAuthApi(),
      this.checkOrderApi(),
      this.checkAddressApi(),
      this.checkNotificationApi(),
      this.checkFeedbackApi(),
      this.checkTransportApi()
    ]

    const results = await Promise.all(checks)
    
    // 显示检查结果摘要
    const availableCount = results.filter(r => r.status === 'available').length
    const totalCount = results.length
    
    if (availableCount === totalCount) {
      ElMessage.success(`所有API接口正常 (${availableCount}/${totalCount})`)
    } else if (availableCount > 0) {
      ElMessage.warning(`部分API接口可用 (${availableCount}/${totalCount})`)
    } else {
      ElMessage.error(`所有API接口不可用，将使用模拟数据`)
    }

    return results
  }

  // 获取API状态
  getApiStatus(apiName: string): ApiStatus | undefined {
    return this.apiStatuses.get(apiName)
  }

  // 检查API是否可用
  isApiAvailable(apiName: string): boolean {
    const status = this.apiStatuses.get(apiName)
    return status?.status === 'available'
  }

  // 显示API状态报告
  showApiReport() {
    const statuses = Array.from(this.apiStatuses.values())
    console.group('🔍 API状态检查报告')
    
    statuses.forEach(status => {
      const emoji = status.status === 'available' ? '✅' : '❌'
      console.log(`${emoji} ${status.name}: ${status.message}`)
    })
    
    console.groupEnd()
  }
}

// 导出单例实例
export const apiChecker = ApiChecker.getInstance()

// 在应用启动时检查API状态
export const initApiChecker = async () => {
  try {
    await apiChecker.checkAllApis()
    apiChecker.showApiReport()
  } catch (error) {
    console.error('API状态检查失败:', error)
  }
} 