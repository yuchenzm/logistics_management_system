import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/',
      component: () => import('../components/Layout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('../views/Dashboard.vue')
        },
        {
          path: 'orders',
          name: 'orders',
          component: () => import('../views/Orders.vue')
        },
        {
          path: 'create-order',
          name: 'create-order',
          component: () => import('../views/CreateOrder.vue')
        },
        {
          path: 'orders/edit/:id',
          name: 'edit-order',
          component: () => import('../views/EditOrder.vue'),
          props: true
        },
        {
          path: 'order-detail/:id',
          name: 'order-detail',
          component: () => import('../views/OrderDetail.vue')
        },
        {
          path: 'tracking',
          name: 'tracking',
          component: () => import('../views/Tracking.vue')
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('../views/Profile.vue')
        },
        {
          path: 'addresses',
          name: 'addresses',
          component: () => import('../views/Addresses.vue')
        },
        {
          path: 'feedback',
          name: 'feedback',
          component: () => import('../views/Feedback.vue')
        },
        {
          path: 'notifications',
          name: 'notifications',
          component: () => import('../views/Notifications.vue')
        },
        {
          path: 'demo',
          name: 'demo',
          component: () => import('../views/DemoPage.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next({ name: 'login' })
  } else if ((to.name === 'login' || to.name === 'register') && token && to.name !== 'register') {
    next({ name: 'dashboard' })
  } else {
    next()
  }
})

export default router
