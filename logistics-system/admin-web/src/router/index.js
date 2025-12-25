import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表板' }
      },
      {
        path: '/orders',
        name: 'Orders',
        component: () => import('../views/Orders.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: '/customers',
        name: 'Customers',
        component: () => import('../views/Customers.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: '/drivers',
        name: 'Drivers',
        component: () => import('../views/Drivers.vue'),
        meta: { title: '司机管理' }
      },
      {
        path: '/vehicles',
        name: 'Vehicles',
        component: () => import('../views/Vehicles.vue'),
        meta: { title: '车辆管理' }
      },
      {
        path: '/transports',
        name: 'Transports',
        component: () => import('../views/Transports.vue'),
        meta: { title: '运输管理' }
      },
      {
        path: '/warehouses',
        name: 'Warehouses',
        component: () => import('../views/Warehouses.vue'),
        meta: { title: '仓库管理' }
      },
      {
        path: '/goods',
        name: 'Goods',
        component: () => import('../views/Goods.vue'),
        meta: { title: '货物管理' }
      },
      {
        path: '/inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue'),
        meta: { title: '库存管理' }
      },
      {
        path: '/suppliers',
        name: 'Suppliers',
        component: () => import('../views/Suppliers.vue'),
        meta: { title: '供应商管理' }
      },
      {
        path: '/expenses',
        name: 'Expenses',
        component: () => import('../views/Expenses.vue'),
        meta: { title: '费用管理' }
      },
      {
        path: '/deliveries',
        name: 'Deliveries',
        component: () => import('../views/Deliveries.vue'),
        meta: { title: '配送管理' }
      },
      {
        path: '/shipping-rates',
        name: 'ShippingRates',
        component: () => import('../views/ShippingRates.vue'),
        meta: { title: '运费管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path === '/login' || to.path === '/register') {
    if (token && to.path !== '/register') {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router 