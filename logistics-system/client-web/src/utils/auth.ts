import type { User } from '@/types/api.d.ts';

/**
 * 从 localStorage 获取用户信息
 * @returns User | null
 */
export function getUserInfo(): User | null {
  const userInfoStr = localStorage.getItem('userInfo');
  if (userInfoStr) {
    try {
      return JSON.parse(userInfoStr) as User;
    } catch (e) {
      console.error('Error parsing user info from localStorage', e);
      // 如果解析失败，清除无效的数据
      localStorage.removeItem('userInfo');
      return null;
    }
  }
  return null;
}

/**
 * 从 localStorage 获取 token
 * @returns string | null
 */
export function getToken(): string | null {
  return localStorage.getItem('token');
}

/**
 * 将用户信息存入 localStorage
 * @param user User
 */
export function setUserInfo(user: User) {
  localStorage.setItem('userInfo', JSON.stringify(user));
}

/**
 * 将 token 存入 localStorage
 * @param token string
 */
export function setToken(token: string) {
  localStorage.setItem('token', token);
}

/**
 * 清除 localStorage 中的认证信息
 */
export function clearAuthInfo() {
  localStorage.removeItem('userInfo');
  localStorage.removeItem('token');
} 