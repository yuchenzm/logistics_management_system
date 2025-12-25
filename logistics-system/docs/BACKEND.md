# 物流管理系统 - 后端技术文档

## 项目概述

物流管理系统后端基于 Spring Boot 框架开发，提供 RESTful API 服务，支持物流业务的完整管理功能。

### 技术栈

- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.0
- **ORM**: MyBatis (基于注解)
- **构建工具**: Maven
- **JDK版本**: Java 17+
- **端口**: 8080
- **密码加密**: 前端MD5加密后传输

## 项目结构

```
backend/
├── src/main/java/com/logistics/
│   ├── LogisticsApplication.java          # Spring Boot主启动类
│   ├── common/                            # 公共工具类
│   │   ├── JwtUtil.java                  # JWT (JSON Web Token) 生成和验证工具
│   │   ├── PageRequest.java              # 分页查询的请求参数封装
│   │   ├── PageResult.java               # 分页查询的返回结果封装
│   │   └── Result.java                   # 全局统一的API响应结果封装
│   ├── config/                            # 配置类 (此目录当前为空)
│   ├── controller/                        # 控制器层 (处理HTTP请求)
│   │   ├── AddressController.java        # 地址管理API接口
│   │   ├── ApiController.java            # 通用、非特定模块的API接口
│   │   ├── CustomerController.java       # 客户管理API接口
│   │   ├── DeliveryController.java       # 配送管理API接口
│   │   ├── DriverController.java         # 司机管理API接口
│   │   ├── ExpenseController.java        # 费用管理API接口
│   │   ├── FeedbackController.java       # 意见反馈API接口
│   │   ├── GoodsController.java          # 货物管理API接口
│   │   ├── InventoryController.java      # 库存管理API接口
│   │   ├── NotificationController.java   # 消息通知API接口
│   │   ├── OrderController.java          # 订单管理API接口
│   │   ├── ShippingRateController.java   # 运费模板API接口
│   │   ├── SupplierController.java       # 供应商管理API接口
│   │   ├── TransportController.java      # 运输管理API接口
│   │   ├── UserController.java           # 用户认证与个人资料API接口
│   │   ├── VehicleController.java        # 车辆管理API接口
│   │   └── WarehouseController.java      # 仓库管理API接口
│   ├── entity/                           # 数据实体类 (对应数据库表)
│   │   ├── Address.java                 # 地址信息实体
│   │   ├── Customer.java                # 客户信息实体
│   │   ├── Delivery.java                # 配送信息实体
│   │   ├── Driver.java                  # 司机信息实体
│   │   ├── Expense.java                 # 费用信息实体
│   │   ├── Feedback.java                # 意见反馈实体
│   │   ├── Goods.java                   # 货物信息实体
│   │   ├── Inventory.java               # 库存信息实体
│   │   ├── Notification.java            # 消息通知实体
│   │   ├── Order.java                   # 订单信息实体
│   │   ├── OrderDetail.java             # 订单详情实体
│   │   ├── OrderSummary.java            # 订单汇总视图实体
│   │   ├── ShippingRate.java            # 运费模板实体
│   │   ├── Supplier.java                # 供应商信息实体
│   │   ├── Transport.java               # 运输记录实体
│   │   ├── TransportSummary.java        # 运输汇总视图实体
│   │   ├── TransportTracking.java       # 运输轨迹实体
│   │   ├── User.java                    # 用户信息实体
│   │   ├── Vehicle.java                 # 车辆信息实体
│   │   └── Warehouse.java               # 仓库信息实体
│   ├── mapper/                          # 数据访问层 (MyBatis接口)
│   │   ├── AddressMapper.java           # `addresses`表的数据接口
│   │   ├── CustomerMapper.java          # `customers`表的数据接口
│   │   ├── DeliveryMapper.java          # `deliveries`表的数据接口
│   │   ├── DriverMapper.java            # `drivers`表的数据接口
│   │   ├── ExpenseMapper.java           # `expenses`表的数据接口
│   │   ├── FeedbackMapper.java          # `feedback`表的数据接口
│   │   ├── GoodsMapper.java             # `goods`表的数据接口
│   │   ├── InventoryMapper.java         # `inventory`表的数据接口
│   │   ├── NotificationMapper.java      # `notifications`表的数据接口
│   │   ├── OrderMapper.java             # `orders`表的数据接口
│   │   ├── ShippingRateMapper.java      # `shipping_rates`表的数据接口
│   │   ├── SupplierMapper.java          # `suppliers`表的数据接口
│   │   ├── TransportMapper.java         # `transports`表的数据接口
│   │   ├── UserMapper.java              # `users`表的数据接口
│   │   ├── VehicleMapper.java           # `vehicles`表的数据接口
│   │   └── WarehouseMapper.java         # `warehouses`表的数据接口
│   └── service/                         # 业务逻辑层 (实现核心业务)
│       ├── AddressService.java           # 地址管理的业务逻辑
│       ├── CustomerService.java          # 客户管理的业务逻辑
│       ├── DeliveryService.java          # 配送管理的业务逻辑
│       ├── DriverService.java            # 司机管理的业务逻辑
│       ├── ExpenseService.java           # 费用管理的业务逻辑
│       ├── FeedbackService.java          # 意见反馈的业务逻辑
│       ├── GoodsService.java             # 货物管理的业务逻辑
│       ├── InventoryService.java         # 库存管理的业务逻辑
│       ├── NotificationService.java      # 消息通知的业务逻辑
│       ├── OrderService.java             # 订单管理的业务逻辑
│       ├── ShippingRateService.java      # 运费模板的业务逻辑
│       ├── SupplierService.java          # 供应商管理的业务逻辑
│       ├── TransportService.java         # 运输管理的业务逻辑
│       ├── UserService.java              # 用户与认证的业务逻辑
│       ├── VehicleService.java           # 车辆管理的业务逻辑
│       └── WarehouseService.java         # 仓库管理的业务逻辑
└── src/main/resources/
    └── application.yml                   # Spring Boot核心配置文件
```

## API 设计规范

### 统一响应格式
```json
{
  "success": true, // 或 false
  "code": 200,
  "message": "success",
  "data": {} // 或 null
}
```

### 分页查询
- **请求方式**: `POST /api/{resource}/page`
- **请求体**: `PageRequest` 对象 (包含 `pageNum`, `pageSize` 及其他过滤参数)
- **响应格式**: `PageResult` 对象 (包含 `list`, `total` 等)

### 通用接口规范
- **分页查询**: `POST /api/{module}/page`
- **按ID查询**: `GET /api/{module}/{id}`
- **新增**: `POST /api/{module}`
- **修改**: `PUT /api/{module}/{id}`
- **删除**: `DELETE /api/{module}/{id}`
- **查询用户特定资源**: `GET /api/{module}/user/{userId}` (例如: 获取某用户的所有地址)

## API 端点清单

<details>
<summary><b>用户认证 (/users)</b></summary>

- `POST /users/login`: 用户登录
- `POST /users/register`: 用户注册
- `GET /users/{id}`: 获取用户个人资料

</details>

<details>
<summary><b>订单 (/orders)</b></summary>

- `POST /orders/page`: 分页查询用户订单
- `POST /orders`: 创建新订单
- `GET /orders/{id}`: 获取订单详情
- `PUT /orders/{id}`: 更新订单信息
- `GET /orders/number/{orderNumber}`: 按订单号查询
- `GET /orders/statistics`: 获取订单统计

</details>

<details>
<summary><b>地址 (/addresses)</b></summary>

- `GET /addresses/user/{userId}`: 获取指定用户的所有地址
- `POST /addresses`: 为当前登录用户新增地址
- `PUT /addresses/{id}`: 更新指定地址
- `DELETE /addresses/{id}`: 删除指定地址
- `PUT /addresses/{id}/default`: 设置为默认地址

</details>

<details>
<summary><b>意见反馈 (/feedback)</b></summary>

- `GET /feedback/user/{userId}`: 获取指定用户的所有反馈
- `POST /feedback`: 提交新反馈

</details>

<details>
<summary><b>消息通知 (/notifications)</b></summary>

- `GET /notifications/user/{userId}`: 获取用户的所有通知
- `PUT /notifications/{id}/read`: 将单条通知标记为已读
- `PUT /notifications/user/{userId}/read-all`: 将用户所有通知标记为已读
- `DELETE /notifications/{id}`: 删除单条通知
- `DELETE /notifications/user/{userId}/all`: 删除用户所有通知

</details>

## 数据库设计

### 核心表结构
*所有表和关键字段都应包含中文注释，以提高可读性。*

<details>
<summary><b>订单表 (orders)</b> - `ALTER TABLE orders COMMENT = '订单表';`</summary>

```sql
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int NOT NULL,
  `origin_address_id` int DEFAULT NULL,
  `destination_address_id` int DEFAULT NULL,
  `goods_name` varchar(255) DEFAULT NULL,
  `goods_weight` decimal(10,2) DEFAULT NULL,
  `goods_volume` decimal(10,2) DEFAULT NULL,
  `order_status` varchar(50) DEFAULT NULL,
  `total_cost` decimal(10,2) DEFAULT NULL,
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`)
) ENGINE=InnoDB COMMENT='订单表';
```
</details>

<details>
<summary><b>地址表 (addresses)</b> - `ALTER TABLE addresses COMMENT = '地址表';`</summary>
```sql
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `contact_name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `detail_address` varchar(255) NOT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB COMMENT='地址表';
```
</details>

<details>
<summary><b>意见反馈表 (feedback)</b> - `ALTER TABLE feedback COMMENT = '意见反馈表';`</summary>
```sql
CREATE TABLE `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `content` text NOT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'submitted',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='意见反馈表';
```
</details>

<details>
<summary><b>消息通知表 (notifications)</b> - `ALTER TABLE notifications COMMENT = '消息通知表';`</summary>
```sql
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` text,
  `status` varchar(20) DEFAULT 'unread',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB COMMENT='消息通知表';
```
</details>

## MyBatis 配置

项目使用 **MyBatis注解** 而非XML文件来映射SQL语句。Mapper接口和SQL注解的示例如下：

```java
// package com.logistics.mapper;

import com.logistics.entity.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM addresses WHERE user_id = #{userId}")
    List<Address> findByUserId(@Param("userId") Long userId);
    
    // ... 其他方法
}
```
因此，`application.yml` 中**不应**包含 `mybatis.mapper-locations` 配置。

### application.yml
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xuedi?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: your_password # 请替换为你的密码
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  type-aliases-package: com.logistics.entity
  configuration:
    map-underscore-to-camel-case: true
    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开发时可开启
```

## 核心问题与解决方案

### 1. 分页查询除零错误
**问题**: PageResult构造函数中当pageSize为0时导致除零异常
```java
// 错误代码
this.pages = (int) Math.ceil((double) total / pageSize);
```

**解决方案**: 添加pageSize检查
```java
public PageResult(List<T> list, long total, int pageNum, int pageSize) {
    this.list = list;
    this.total = total;
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.pages = pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0;
}
```

### 2. 字段映射不一致问题
**问题**: 实体类字段名与数据库字段名不匹配，前端传递下划线命名，后端使用驼峰命名

**解决方案**: 
- 配置MyBatis自动映射: `map-underscore-to-camel-case: true`
- 在实体类中添加getter/setter映射方法
```java
// Order实体类示例
public String getGoodsDescription() {
    return this.specialInstructions;
}

public String getStatus() {
    return this.orderStatus;
}
```

### 3. JSON反序列化冲突
**问题**: Warehouse实体类中存在重载的setId方法导致Jackson反序列化失败
```java
// 冲突代码
public void setId(Long id) { this.id = id.intValue(); }
public void setId(Integer id) { this.id = id; }
```

**解决方案**: 合并为通用方法
```java
public void setId(Object id) { 
    if (id instanceof Long) {
        this.id = ((Long) id).intValue();
    } else if (id instanceof Integer) {
        this.id = (Integer) id;
    } else if (id != null) {
        this.id = Integer.valueOf(id.toString());
    }
}
```

### 4. 多条件搜索实现
**问题**: 原始Mapper只支持基本分页，不支持条件搜索

**解决方案**: 扩展Mapper添加多条件搜索方法
```java
// CustomerMapper示例
List<Customer> findByPageWithConditions(
    @Param("offset") int offset, 
    @Param("pageSize") int pageSize,
    @Param("keyword") String keyword,
    @Param("city") String city,
    @Param("creditRating") String creditRating
);

int countWithConditions(
    @Param("keyword") String keyword,
    @Param("city") String city, 
    @Param("creditRating") String creditRating
);
```

### 5. 类型转换错误
**问题**: Driver实体的rating字段类型不匹配
```java
// 错误代码
driver.setRating(0.0);
```

**解决方案**: 使用正确的BigDecimal类型
```java
driver.setRating(java.math.BigDecimal.ZERO);
```

### 6. 端口占用问题
**问题**: Spring Boot启动失败，端口8080被占用

**解决方案**: 
```bash
# 查找占用进程
lsof -i :8080
# 停止占用进程
kill -9 PID
```

### 7. 运输记录结束时间缺失
**问题**: 已完成状态的运输记录endTime字段为null

**解决方案**: SQL数据修复
```sql
UPDATE transports 
SET end_time = DATE_ADD(start_time, INTERVAL estimated_duration HOUR) 
WHERE transport_status = '已完成' AND end_time IS NULL;
```

## 部署指南

### 1. 环境要求
- JDK 8+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库初始化
```sql
CREATE DATABASE xuedi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 启动命令
```bash
# 开发环境
mvn spring-boot:run

# 生产环境
mvn clean package
java -jar target/logistics-backend.jar
```

### 4. 健康检查
- 应用状态: `GET http://localhost:8080/api/health`
- API文档: `GET http://localhost:8080/api/docs`

## 性能优化

### 1. 数据库优化
- 添加必要的索引
- 使用分页查询避免全表扫描
- 优化复杂查询的SQL语句

### 2. 缓存策略
- 对频繁查询的数据进行缓存
- 使用Redis缓存热点数据

### 3. 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

## 日志配置

日志文件位置: `logs/backend.log`

日志级别配置:
```yaml
logging:
  level:
    com.logistics: DEBUG
    org.springframework: INFO
    com.mysql: WARN
```

## 安全注意事项

1. 数据库密码加密存储
2. API接口添加身份验证
3. 敏感数据脱敏处理
4. SQL注入防护
5. XSS攻击防护

## 监控指标

1. 应用性能监控 (APM)
2. 数据库连接池监控
3. API响应时间监控
4. 错误率统计
5. 系统资源使用情况

---

*最后更新时间: 2024年12月* 