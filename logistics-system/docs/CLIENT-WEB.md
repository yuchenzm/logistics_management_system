# Client-Web (客户业务端) 详细说明文档

本文档详细介绍了 `client-web` 前端应用的结构、核心功能、技术实现，并为二次开发和维护提供指导。

## 1. 概述

`client-web` 是面向终端客户的前端应用，允许客户进行在线下单、管理订单、跟踪货物、查询费用和管理个人信息。它与后端API服务紧密集成，提供了一个现代、响应式的用户体验。

- **访问端口:** `3001`
- **目标用户:** 客户 (customer)

## 2. 技术栈

- **框架:** Vue 3 (Composition API)
- **构建工具:** Vite
- **语言:** TypeScript
- **UI 组件库:** Element Plus
- **路由:** Vue Router
- **状态管理:** Pinia
- **HTTP客户端:** Axios

## 3. 详细项目结构

```
client-web/
├── src/
│   ├── assets/           # 静态资源 (CSS, 图片)
│   ├── components/       # 可复用的Vue组件
│   ├── router/
│   │   └── index.ts      # 路由配置文件，包含路由守卫
│   ├── stores/           # Pinia 状态管理模块
│   ├── types/            # TypeScript 类型定义
│   ├── utils/            # 通用工具函数
│   │   ├── http.ts       # Axios 实例封装，包含请求/响应拦截器
│   │   ├── api.ts        # API 接口函数定义
│   │   └── auth.ts       # 认证相关工具 (读写Token和用户信息)
│   ├── views/            # 页面级视图组件
│   │   ├── Login.vue     # 登录页面
│   │   ├── Register.vue  # 注册页面
│   │   ├── Dashboard.vue # 客户工作台/仪表盘
│   │   ├── Orders.vue    # 订单列表页
│   │   ├── CreateOrder.vue # 创建新订单页
│   │   ├── EditOrder.vue # 编辑订单页
│   │   ├── OrderDetail.vue # 订单详情页
│   │   ├── Tracking.vue  # 物流实时跟踪页
│   │   └── Profile.vue   # 个人信息管理页
│   ├── App.vue           # 应用根组件
│   └── main.ts           # 应用入口文件
│
├── public/               # 公共静态资源 (不会被Vite处理)
├── index.html            # HTML 入口文件
├── package.json          # 项目依赖和脚本配置
└── vite.config.ts        # Vite 配置文件
```

## 4. 核心功能与页面

### 4.1 用户认证
- **相关页面:** `Login.vue`, `Register.vue`
- **实现方式:**
  1. 用户在 `Login.vue` 输入凭据，密码在前端通过 **MD5** 加密。
  2. 调用 `api.ts` 中定义的登录接口，通过 `http.ts` 发送请求。
  3. 登录成功后，后端返回 JWT Token 和用户信息。
  4. `auth.ts` 工具函数将 Token 和用户信息存入 `localStorage`。
  5. Vue Router 的全局前置守卫 (`beforeEach`) 会检查 `localStorage` 中是否存在Token，以保护需要认证的页面。

### 4.2 订单管理
- **相关页面:** `Orders.vue`, `CreateOrder.vue`, `EditOrder.vue`, `OrderDetail.vue`
- **流程:**
  - `Orders.vue`: 显示客户的所有订单列表，支持分页、搜索和筛选。
  - `CreateOrder.vue`: 一个复杂表单，用于客户填写发货、收货信息和货物详情以创建新订单。
  - `EditOrder.vue`: 与创建页面类似，但用于修改未发货的订单。
  - `OrderDetail.vue`: 展示单个订单的完整信息，包括状态历史、费用明细等。

### 4.3 物流跟踪
- **相关页面:** `Tracking.vue`
- **功能:** 用户输入订单号，该页面会调用后端接口查询订单的实时运输轨迹，并在地图或时间线上可视化展示。

### 4.4 个人中心
- **相关页面:** `Profile.vue`, `Addresses.vue`
- **功能:**
  - `Profile.vue`: 允许用户查看和修改自己的基本信息。
  - `Addresses.vue`: 管理客户常用的发货和收货地址簿。

## 5. API交互

应用的HTTP通信完全由 `src/utils/http.ts` 中封装的 Axios 实例处理。

- **请求拦截器:**
  - 在每个发往后端的请求头中自动附加 `Authorization` 字段，其值为从 `localStorage` 中获取的 JWT Token。
  - `config.headers['Authorization'] = getToken()`

- **响应拦截器:**
  - **成功处理:** 对后端返回的数据进行预处理，直接返回 `response.data.data` 部分，简化了组件中的调用。
  - **错误处理:** 统一处理HTTP状态码错误和后端业务逻辑错误（通过 `response.data.code` 判断）。例如：
    - 对于 `401 Unauthorized` 错误，会自动清除本地存储的认证信息并重定向到登录页。
    - 对于其他业务错误，会使用 Element Plus 的 `ElMessage` 组件弹出错误提示。

- **API定义:**
  - `src/utils/api.ts` 文件中定义了所有与后端交互的函数（如 `login`, `getOrders`, `createOrder` 等）。组件中应调用这些函数，而不是直接使用axios，以实现更好的代码组织和复用。

## 6. 开发与维护指南

- **添加新页面:**
  1. 在 `src/views/` 下创建新的 `.vue` 文件。
  2. 在 `src/router/index.ts` 中添加新的路由配置，并根据需要添加 `meta: { requiresAuth: true }` 来启用路由守卫。

- **添加新API:**
  1. 在 `src/utils/api.ts` 中添加新的接口调用函数。
  2. 在组件中通过 `import` 调用该函数。

- **状态管理:**
  - 对于跨组件共享的、响应式的状态（如当前用户信息），应使用 Pinia 在 `src/stores/` 中创建模块进行管理。

---
**文档版本**: v1.0  
**更新时间**: 2025-07-08
**维护人员**: 雨辰