/**
 * API相关类型定义
 */

// 通用API响应接口
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
  success: boolean;
}

// 分页响应接口
export interface PageResponse<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

// 订单统计接口
export interface OrderStats {
  pending: number;
  inTransit: number;
  delivered: number;
  total: number;
}

// 订单状态类型
export type OrderStatus = 'pending' | 'confirmed' | 'picked_up' | 'in_transit' | 'delivered' | 'cancelled';

// 订单优先级类型
export type PriorityLevel = 'low' | 'medium' | 'high';

// 订单接口
export interface Order {
  id: string
  orderNumber: string
  customerId: string
  orderStatus: OrderStatus
  priority: PriorityLevel
  originAddress: string
  destinationAddress: string
  totalWeight: number // 之前这里是 weight
  totalAmount: number
  createdAt: string
  updatedAt: string
  remarks?: string
  totalVolume?: number
  transportId?: number
}

// 用户信息接口
export interface User {
  id: number;
  username: string;
  email?: string;
  phone?: string;
  realName?: string;
  avatar?: string;
  role: string;
}

// 登录响应数据接口
export interface LoginResponse {
  token: string;
  user: User;
} 