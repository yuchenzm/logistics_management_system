# 物流管理系统 (Logistics Management System)

基于Spring Boot + Vue.js的现代化物流管理平台，采用前后端分离架构，支持多端应用。

## 📋 项目架构

### 系统组成
```
logistics-system/
├── backend/              # Spring Boot 后端API服务
├── admin-web/           # 管理员后台系统
├── client-web/          # 客户端应用
├── user-web/            # 用户端应用（员工/司机）
├── common/              # 公共组件和工具
└── docs/                # 项目文档
```

### 应用说明

#### 🔧 Backend (后端服务)
- **技术栈**: Spring Boot 3.1.5 + MyBatis + MySQL + JWT
- **端口**: 8080
- **功能**: 提供统一的RESTful API服务
- **数据库**: MySQL (xuedi)

#### 🖥️ Admin Web (管理员后台)
- **技术栈**: Vue.js 3 + Element Plus + TypeScript
- **端口**: 3000
- **用户角色**: 管理员 (admin)
- **功能**: 
  - 系统全局配置
  - 用户权限管理
  - 订单管理
  - 客户管理
  - 司机管理
  - 车辆管理
  - 运输管理
  - 数据统计报表

#### 👤 Client Web (客户端)
- **技术栈**: Vue.js 3 + Element Plus + TypeScript
- **端口**: 3001
- **用户角色**: 客户 (customer)
- **功能**:
  - 在线下单
  - 订单查询
  - 物流跟踪
  - 费用查询
  - 个人信息管理

#### 👥 User Web (用户端)
- **技术栈**: Vue.js 3 + Element Plus + TypeScript
- **端口**: 3002
- **用户角色**: 员工 (employee)、司机 (driver)、经理 (manager)
- **功能**:
  - 任务管理
  - 订单处理
  - 运输跟踪
  - 工作报告
  - 个人工作台

## 🚀 快速开始

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 20.0+
- **MySQL**: 8.0+
- **Maven**: 3.8+

### 数据库配置
1. 创建数据库 `xuedi`
2. 配置连接信息：`127.0.0.1:3306`，用户名：`root`，密码：`12345678`

### 启动顺序

#### 1. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

#### 2. 启动管理员后台
```bash
cd admin-web
npm install
npm run dev
```
访问：http://localhost:3000

#### 3. 启动客户端
```bash
cd client-web
npm install
npm run dev
```
访问：http://localhost:3001

#### 4. 启动用户端
```bash
cd user-web
npm install
npm run dev
```
访问：http://localhost:3002

### 🔄 一键启动脚本
```bash
./start-all.sh    # 启动所有服务
./stop-all.sh     # 停止所有服务
```

## 👤 演示账号

### 管理员后台 (admin-web)
- 管理员：`admin` / `123456`
- 经理：`manager1` / `123456`

### 客户端 (client-web)
- 阿里巴巴：`alibaba` / `123456`
- 腾讯科技：`tencent` / `123456`

### 用户端 (user-web)
- 员工：`employee1` / `123456`
- 司机：`driver1` / `123456`
- 经理：`manager1` / `123456`

## 🛠️ 技术栈

### 后端技术
- **Spring Boot 3.1.5** - 主框架
- **MyBatis** - ORM框架
- **MySQL 8.0** - 数据库
- **JWT** - 身份认证
- **Maven** - 项目管理

### 前端技术
- **Vue.js 3** - 前端框架
- **TypeScript** - 类型支持
- **Element Plus** - UI组件库
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Vite** - 构建工具
- **Axios** - HTTP客户端

## 📊 功能模块

### 核心业务
- 📦 **订单管理** - 订单创建、处理、跟踪
- 🚚 **运输管理** - 运输计划、调度、跟踪
- 👤 **客户管理** - 客户档案、信用管理
- 🚗 **车辆管理** - 车辆档案、调度分配
- 👨‍💼 **司机管理** - 司机档案、任务分配
- 📈 **数据报表** - 业务统计、性能分析

### 权限系统
- 🔐 **多角色权限** - 管理员、经理、员工、司机、客户
- 🛡️ **JWT认证** - 无状态身份验证
- 🚪 **路由守卫** - 前端权限控制

## 🌐 API文档

后端API遵循RESTful设计规范：
```
GET    /api/users          # 获取用户列表
POST   /api/users/login    # 用户登录
GET    /api/orders         # 获取订单列表
POST   /api/orders         # 创建订单
GET    /api/transports     # 获取运输列表
...
```

## 🔧 开发指南

### 添加新功能
1. **后端**: 在`backend/src/main/java/com/logistics/`中添加相应的Entity、Mapper、Service、Controller
2. **前端**: 在各自的前端项目中添加页面和组件
3. **权限**: 在路由守卫中配置相应的权限控制

### 部署说明
- **开发环境**: 使用内置服务器
- **生产环境**: 
  - 后端：打包为JAR文件部署
  - 前端：构建静态文件部署到Nginx

## 🤝 贡献指南
1. Fork 项目
2. 创建功能分支
3. 提交变更
4. 推送到分支
5. 创建 Pull Request

## 📄 许可证
MIT License

---
⭐ 如果这个项目对 