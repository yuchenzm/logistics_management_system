/**
 * API配置文件
 * - 所有API请求都集中在此处
 * - 使用http.ts中封装的axios实例
 */

import http from './http'
import type { ApiResponse, PageResponse, Order, OrderStats, LoginResponse } from '@/types/api.d.ts';

// 认证相关
export const authApi = {
  login: (data: any) => http.post('/users/login', data) as Promise<ApiResponse<LoginResponse>>,
  register: (data: any) => http.post('/users/register', data),
  getInfo: () => http.get('/info')
};

// 用户相关
export const userApi = {
  getProfile: (id: number) => http.get(`/users/${id}`),
  updateProfile: (data: any) => http.put('/user/profile', data),
  updatePassword: (data: any) => http.put('/user/password', data),
  getAddresses: (userId: number) => http.get(`/addresses/user/${userId}`),
  addAddress: (data: any) => http.post('/addresses', data),
  updateAddress: (id: number, data: any) => http.put(`/addresses/${id}`, data),
  deleteAddress: (id: number) => http.delete(`/addresses/${id}`),
  setDefaultAddress: (id: number) => http.put(`/addresses/${id}/default`)
}

// 订单相关
export const orderApi = {
  getSummary: () => http.get('/orders/statistics') as Promise<ApiResponse<OrderStats>>,
  create: (data: any) => http.post('/orders', data) as Promise<ApiResponse<Order>>,
  list: (params: any) => http.post('/orders/page', params) as Promise<ApiResponse<PageResponse<Order>>>,
  detail: (id: string) => http.get(`/orders/${id}`) as Promise<ApiResponse<Order>>,
  update: (id: string, data: any) => http.put(`/orders/${id}`, data) as Promise<ApiResponse<Order>>,
  getByOrderNumber: (orderNumber: string) => http.get(`/orders/number/${orderNumber}`) as Promise<ApiResponse<Order>>,
  updateStatus: (id: string, status: string) => http.put(`/orders/${id}/status`, { status }) as Promise<ApiResponse<any>>,
  cancelOrder: (id: string) => http.delete(`/orders/${id}`) as Promise<ApiResponse<any>>,
  getTrackingInfo: (orderNumber: string) => http.get(`/tracking/${orderNumber}`) as Promise<ApiResponse<any>>
};

// 反馈相关
export const feedbackApi = {
  submit: (data: any) => http.post('/feedback', data),
  list: (userId: number) => http.get(`/feedback/user/${userId}`)
};

// 通知相关
export const notificationApi = {
  list: (userId: number) => http.get(`/notifications/user/${userId}`),
  markAsRead: (id: number) => http.put(`/notifications/${id}/read`),
  markAllAsRead: (userId: number) => http.put(`/notifications/user/${userId}/read-all`),
  delete: (id: number) => http.delete(`/notifications/${id}`),
  deleteAll: (userId: number) => http.delete(`/notifications/user/${userId}/all`)
};

// 地址相关 (作为客户地址)
export const addressApi = {
  getAddresses: (customerId: number) => http.get(`/customers/${customerId}/addresses`),
  addAddress: (customerId: number, data: any) => http.post(`/customers/${customerId}/addresses`, data),
  updateAddress: (addressId: number, data: any) => http.put(`/addresses/${addressId}`, data),
  deleteAddress: (addressId: number) => http.delete(`/addresses/${addressId}`),
  setDefault: (addressId: number) => http.post(`/addresses/${addressId}/default`)
};

// 客户相关
export const customerApi = {
  getAddresses: (id: number) => http.get(`/customers/${id}/addresses`),
};

// 物流追踪相关
export const trackingApi = {
  getTrackingInfo: (orderNumber: string) => http.get(`/tracking/${orderNumber}`)
};
