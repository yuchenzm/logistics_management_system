import axios from 'axios'

// API基础URL
const API_BASE_URL = 'http://localhost:8080/api'

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // token过期，清除本地存储并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('customerId')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 用户认证相关API
export const authApi = {
  // 用户登录
  login: (data: { username: string; password: string }) =>
    apiClient.post('/users/login', data),
    
  // 用户注册
  register: (data: {
    username: string
    password: string
    email: string
    phone: string
  }) => apiClient.post('/users', data),
  
  // 获取用户信息
  getUserInfo: (id: number) => apiClient.get(`/users/${id}`),
  
  // 更新用户信息
  updateProfile: (id: number, data: any) => apiClient.put(`/users/${id}`, data),
  
  // 修改密码
  updatePassword: (id: number, data: {
    oldPassword: string
    newPassword: string
  }) => apiClient.put(`/users/${id}/password`, data)
}

// 订单相关API
export const orderApi = {
  // 获取所有订单
  getAll: () => apiClient.get('/orders'),
  
  // 根据客户ID获取订单
  getByCustomerId: (customerId: number) => 
    apiClient.get(`/orders/customer/${customerId}`),
  
  // 根据状态获取订单
  getByStatus: (status: string) => 
    apiClient.get(`/orders/status/${status}`),
  
  // 分页查询订单
  getByPage: (pageRequest: {
    pageNum: number
    pageSize: number
    orderNumber?: string
    orderStatus?: string
    priority?: string
    startDate?: string
    endDate?: string
  }) => apiClient.post('/orders/page', pageRequest),
  
  // 根据订单号查询
  getByOrderNumber: (orderNumber: string) => 
    apiClient.get(`/orders/number/${orderNumber}`),
  
  // 获取订单详情
  getOrderDetail: (id: number) => apiClient.get(`/orders/${id}`),
  
  // 创建订单
  create: (data: {
    customerName: string
    phone: string
    customerId?: number
    originAddress: string
    destinationAddress: string
    pickupAddress: string
    pickupContact: string
    pickupPhone: string
    pickupTime?: string
    deliveryAddress: string
    deliveryContact: string
    deliveryPhone: string
    deliveryTime?: string
    expectedDeliveryTime?: string
    goodsDescription: string
    goodsType: string
    weight: number
    volume: number
    goodsValue: number
    totalWeight: number
    totalVolume: number
    totalAmount: number
    priority: string
    serviceType: string
    specialInstructions?: string
    remarks?: string
  }) => apiClient.post('/orders', data),
  
  // 更新订单
  update: (id: number, data: any) => apiClient.put(`/orders/${id}`, data),
  
  // 更新订单状态
  updateStatus: (id: number, status: string) => 
    apiClient.put(`/orders/${id}/status`, { orderStatus: status }),
  
  // 取消订单
  cancelOrder: (id: number) => apiClient.put(`/orders/${id}/cancel`),
  
  // 删除订单
  delete: (id: number) => apiClient.delete(`/orders/${id}`),
  
  // 获取订单统计
  getOrderStats: () => apiClient.get('/orders/statistics'),
  
  // 获取物流跟踪记录
  getTrackingHistory: (id: number) => apiClient.get(`/orders/${id}/tracking`)
}

// 客户相关API
export const customerApi = {
  // 获取所有活跃客户
  getAllActive: () => apiClient.get('/customers/active'),
  
  // 分页查询客户
  getByPage: (pageRequest: {
    pageNum: number
    pageSize: number
    keyword?: string
  }) => apiClient.post('/customers/page', pageRequest),
  
  // 搜索客户
  search: (keyword: string) => 
    apiClient.get(`/customers/search?keyword=${keyword}`),
  
  // 根据ID查询客户
  getById: (id: number) => apiClient.get(`/customers/${id}`),
  
  // 创建客户
  create: (data: {
    name: string
    contactPerson: string
    phone: string
    email: string
    address: string
    customerType: string
  }) => apiClient.post('/customers', data),
  
  // 更新客户
  update: (id: number, data: any) => apiClient.put(`/customers/${id}`, data),
  
  // 删除客户
  delete: (id: number) => apiClient.delete(`/customers/${id}`)
}

// 运输相关API
export const transportApi = {
  // 分页查询运输
  getByPage: (pageRequest: {
    pageNum: number
    pageSize: number
    keyword?: string
    status?: string
  }) => apiClient.post('/transports/page', pageRequest),
  
  // 根据状态查询运输
  getByStatus: (status: string) => 
    apiClient.get(`/transports/status/${status}`),
  
  // 根据运输号查询
  getByTransportNumber: (transportNumber: string) => 
    apiClient.get(`/transports/number/${transportNumber}`),
  
  // 根据订单ID获取运输信息
  getByOrderId: (orderId: number) => 
    apiClient.get(`/transports/order/${orderId}`),
  
  // 获取运输详情
  getById: (id: number) => apiClient.get(`/transports/${id}`),
  
  // 创建运输
  create: (data: any) => apiClient.post('/transports', data),
  
  // 更新运输状态
  updateStatus: (id: number, status: string) => 
    apiClient.put(`/transports/${id}/status`, { transportStatus: status }),
  
  // 删除运输
  delete: (id: number) => apiClient.delete(`/transports/${id}`)
}

// 运费相关API
export const shippingApi = {
  // 获取所有运费
  getAll: () => apiClient.get('/shipping-rates'),
  
  // 分页查询运费
  getByPage: (pageRequest: {
    pageNum: number
    pageSize: number
    keyword?: string
  }) => apiClient.post('/shipping-rates/page', pageRequest),
  
  // 根据路线查询运费
  getByRoute: (originCity: string, destinationCity: string) =>
jcdgsdc'    apiClient.get(`/shipping-rates/route?originCity=${originCity}&destinationCity=${destinationCity}`),
  
  // 根据运输类型查询运费
  getByTransportType: (transportType: string) =>
    apiClient.get(`/shipping-rates/type/${transportType}`),
  
  // 计算运费
  calculateShipping: (data: {
    originCity: string
    destinationCity: string
    transportType: string
    weight: number
    volume: number
  }) => apiClient.post('/shipping-rates/calculate', data),
  
  // 搜索运费
  search: (keyword: string) => 
    apiClient.get(`/shipping-rates/search?keyword=${keyword}`),
  
  // 根据ID查询运费
  getById: (id: number) => apiClient.get(`/shipping-rates/${id}`),
  
  // 保存运费
  save: (data: any) => apiClient.post('/shipping-rates', data),
  
  // 批量更新运费
  batchUpdate: (data: any[]) => apiClient.post('/shipping-rates/batch-update', data),
  
  // 删除运费
  delete: (id: number) => apiClient.delete(`/shipping-rates/${id}`)
}

// 地址管理API
export const addressApi = {
  // 获取用户地址列表
  getByUserId: (userId: number) => apiClient.get(`/addresses/user/${userId}`),
  
  // 根据ID获取地址
  getById: (id: number) => apiClient.get(`/addresses/${id}`),
  
  // 创建地址
  create: (data: {
    userId: number
    contactName: string
    contactPhone: string
    province: string
    city: string
    district: string
    address: string
    isDefault: boolean
  }) => apiClient.post('/addresses', data),
  
  // 更新地址
  update: (id: number, data: any) => apiClient.put(`/addresses/${id}`, data),
  
  // 设置默认地址
  setDefault: (id: number) => apiClient.put(`/addresses/${id}/default`),
  
  // 删除地址
  delete: (id: number) => apiClient.delete(`/addresses/${id}`)
}

// 消息通知API
export const notificationApi = {
  // 获取用户通知列表
  getByUserId: (userId: number, pageRequest: {
    pageNum: number
    pageSize: number
    isRead?: boolean
  }) => apiClient.post(`/notifications/user/${userId}/page`, pageRequest),
  
  // 标记为已读
  markAsRead: (id: number) => apiClient.put(`/notifications/${id}/read`),
  
  // 批量标记为已读
  batchMarkAsRead: (ids: number[]) => apiClient.put('/notifications/batch-read', { ids }),
  
  // 删除通知
  delete: (id: number) => apiClient.delete(`/notifications/${id}`),
  
  // 获取未读数量
  getUnreadCount: (userId: number) => apiClient.get(`/notifications/user/${userId}/unread-count`)
}

// 反馈API
export const feedbackApi = {
  // 获取用户反馈列表
  getByUserId: (userId: number, pageRequest: {
    pageNum: number
    pageSize: number
  }) => apiClient.post(`/feedback/user/${userId}/page`, pageRequest),
  
  // 创建反馈
  create: (data: {
    userId: number
    title: string
    content: string
    category: string
  }) => apiClient.post('/feedback', data),
  
  // 根据ID获取反馈
  getById: (id: number) => apiClient.get(`/feedback/${id}`)
}

export default apiClient
