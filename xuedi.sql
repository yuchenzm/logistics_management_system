-- MySQL dump 10.13  Distrib 8.4.0, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: xuedi
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `contact_name` varchar(255) NOT NULL,
  `contact_phone` varchar(20) NOT NULL,
  `province` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) DEFAULT NULL,
  `address` text NOT NULL,
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` (`id`, `user_id`, `contact_name`, `contact_phone`, `province`, `city`, `district`, `address`, `is_default`, `create_time`, `update_time`) VALUES (1,14,'雨辰','18699228877','陕西省','西安市','长安区','西安明德理工学院\n',0,'2025-07-08 09:32:25','2025-07-08 09:32:36');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  `city` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `customer_type` enum('individual','company') DEFAULT 'individual',
  `credit_rating` enum('A','B','C','D') DEFAULT 'B',
  `status` enum('active','inactive') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `city`, `province`, `postal_code`, `customer_type`, `credit_rating`, `status`, `created_at`, `updated_at`) VALUES (1,'北京科技有限公司','张经理','010-12345678','zhang@bjtech.com','北京市朝阳区科技大厦','北京','北京市',NULL,'company','B','active','2025-07-02 09:31:17','2025-07-02 09:31:17'),(2,'上海贸易公司','李总','021-87654321','li@shtrade.com','上海市浦东新区商务中心','上海','上海市',NULL,'company','B','active','2025-07-02 09:31:17','2025-07-02 09:31:17'),(3,'广州个人客户','王先生','020-11111111','wang@email.com','广州市天河区住宅小区','广州','广东省',NULL,'individual','B','active','2025-07-02 09:31:17','2025-07-02 09:31:17'),(14,'阿里巴巴集团','马云','0571-85022088','contact@alibaba.com','杭州市余杭区文一西路969号','杭州',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(15,'腾讯科技','马化腾','0755-86013388','contact@tencent.com','深圳市南山区科技园','深圳',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(16,'百度公司','李彦宏','010-59928888','contact@baidu.com','北京市海淀区上地十街10号','北京',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(17,'京东集团','刘强东','400-606-5500','contact@jd.com','北京市朝阳区北辰世纪中心','北京',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(18,'美团','王兴','010-57846555','contact@meituan.com','北京市朝阳区望京东路6号','北京',NULL,NULL,'individual','B','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(19,'顺丰速运','王卫','400-811-1111','contact@sf-express.com','深圳市福田区益田路6009号','深圳',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(20,'中通快递','赖梅松','400-827-0270','contact@zto.com','上海市青浦区华新镇','上海',NULL,NULL,'individual','B','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(21,'圆通速递','喻渭蛟','95554','contact@yto.net.cn','上海市青浦区华新镇','上海',NULL,NULL,'individual','B','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(22,'华为技术','任正非','400-822-9999','contact@huawei.com','深圳市龙岗区坂田华为基地','深圳',NULL,NULL,'individual','A','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(23,'小米科技','雷军','400-100-5678','contact@mi.com','北京市海淀区清河中街68号','北京',NULL,NULL,'individual','B','active','2025-07-02 11:49:46','2025-07-02 11:49:46'),(24,'yuuyu','马云的','18592013390','','','',NULL,NULL,'individual','A','active','2025-07-02 15:37:23','2025-07-08 03:36:15');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deliveries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transport_id` int NOT NULL,
  `delivery_address` text,
  `delivery_contact` varchar(50) DEFAULT NULL,
  `delivery_phone` varchar(20) DEFAULT NULL,
  `scheduled_time` timestamp NULL DEFAULT NULL,
  `actual_delivery_time` timestamp NULL DEFAULT NULL,
  `delivery_status` enum('pending','out_for_delivery','delivered','failed','returned') DEFAULT 'pending',
  `signature_required` tinyint(1) DEFAULT '1',
  `recipient_name` varchar(50) DEFAULT NULL,
  `delivery_notes` text,
  `proof_of_delivery` text,
  `attempts` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `transport_id` (`transport_id`),
  KEY `idx_deliveries_status` (`delivery_status`),
  CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`transport_id`) REFERENCES `transports` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drivers`
--

DROP TABLE IF EXISTS `drivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drivers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `license_number` varchar(30) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  `hire_date` date DEFAULT NULL,
  `license_type` varchar(10) DEFAULT NULL,
  `experience_years` int DEFAULT '0',
  `rating` decimal(3,2) DEFAULT '0.00',
  `status` enum('available','busy','off_duty','inactive') DEFAULT 'available',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_number` (`license_number`),
  KEY `idx_drivers_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='司机信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drivers`
--

LOCK TABLES `drivers` WRITE;
/*!40000 ALTER TABLE `drivers` DISABLE KEYS */;
INSERT INTO `drivers` (`id`, `name`, `license_number`, `phone`, `email`, `address`, `hire_date`, `license_type`, `experience_years`, `rating`, `status`, `created_at`, `updated_at`) VALUES (1,'刘师傅','A1234567890','13900139001','liu@driver.com',NULL,NULL,'A2',10,0.00,'available','2025-07-02 09:32:01','2025-07-02 09:32:01'),(2,'陈师傅','B1234567890','13900139002','chen@driver.com',NULL,NULL,'B2',8,0.00,'available','2025-07-02 09:32:01','2025-07-02 09:32:01'),(3,'赵师傅','C1234567890','13900139003','zhao@driver.com',NULL,NULL,'C1',5,0.00,'available','2025-07-02 09:32:01','2025-07-02 09:32:01'),(4,'李师傅','B2201901001','13900001001',NULL,NULL,'2020-01-15','B2',5,4.50,'available','2025-07-02 11:51:27','2025-07-02 17:45:50'),(5,'王师傅','B2201901002','13900001002',NULL,NULL,'2020-03-20','B2',13,4.20,'available','2025-07-02 11:51:27','2025-07-02 17:51:23'),(6,'张师傅','A2201901003','13900001003',NULL,NULL,'2019-08-10','A2',8,4.80,'busy','2025-07-02 11:51:27','2025-07-02 11:51:27'),(7,'陈师傅','B2201901004','13900001004',NULL,NULL,'2021-02-01','B2',2,4.00,'available','2025-07-02 11:51:27','2025-07-02 11:51:27'),(8,'赵师傅','A2201901005','13900001005',NULL,NULL,'2020-12-15','A2',6,4.60,'busy','2025-07-02 11:51:27','2025-07-02 11:51:27');
/*!40000 ALTER TABLE `drivers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `position` varchar(50) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  CONSTRAINT `fk_employees_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` (`id`, `user_id`, `name`, `position`, `hire_date`, `created_at`, `updated_at`) VALUES (2,NULL,'employee1','普通员工','2025-07-08','2025-07-08 03:33:10','2025-07-08 03:33:10');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transport_id` int DEFAULT NULL,
  `expense_type` enum('fuel','toll','maintenance','insurance','parking','other') NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `description` text,
  `receipt_number` varchar(50) DEFAULT NULL,
  `expense_date` date DEFAULT NULL,
  `approved_by` int DEFAULT NULL,
  `approval_status` enum('pending','approved','rejected') DEFAULT 'pending',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `transport_id` (`transport_id`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`transport_id`) REFERENCES `transports` (`id`) ON DELETE SET NULL,
  CONSTRAINT `expenses_ibfk_2` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='费用记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` (`id`, `transport_id`, `expense_type`, `amount`, `description`, `receipt_number`, `expense_date`, `approved_by`, `approval_status`, `created_at`, `updated_at`) VALUES (6,10,'fuel',1200.50,'上海到北京运输燃油费','FUEL20250103001','2025-01-03',NULL,'approved','2025-07-02 17:27:24','2025-07-02 19:08:56'),(7,10,'toll',380.00,'高速公路过路费','TOLL20250103001','2025-01-03',NULL,NULL,'2025-07-02 17:27:24','2025-07-02 19:02:15'),(8,NULL,'maintenance',800.00,'车辆保养维修费用','MAINT20250102001','2025-01-02',NULL,NULL,'2025-07-02 17:27:24','2025-07-02 19:02:20'),(9,11,'fuel',450.00,'广州到深圳燃油费','FUEL20250104001','2025-01-04',NULL,'approved','2025-07-02 17:27:24','2025-07-02 19:08:42'),(10,9,'parking',50.00,'停车费','PARK20250103001','2025-01-03',NULL,NULL,'2025-07-02 17:27:24','2025-07-02 19:02:24'),(11,NULL,'other',2000.00,'车辆保险费','INS20250101001','2025-01-01',1,'approved','2025-07-02 17:27:24','2025-07-02 17:27:24');
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'pending',
  `reply` text,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id_feedback` (`user_id`),
  KEY `idx_status_feedback` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='意见反馈表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` (`id`, `user_id`, `type`, `title`, `content`, `status`, `reply`, `create_time`, `update_time`) VALUES (1,14,'suggestion','1111','3333','pending',NULL,'2025-07-08 09:38:40','2025-07-08 09:38:40'),(2,14,'ui_issue','哦就哦就哦','in ','pending',NULL,'2025-07-08 09:46:46','2025-07-08 09:46:46');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `category` enum('electronics','clothing','food','books','furniture') DEFAULT NULL,
  `description` text,
  `weight` decimal(10,2) DEFAULT NULL,
  `volume` decimal(10,2) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `fragile` tinyint(1) DEFAULT '0',
  `hazardous` tinyint(1) DEFAULT '0',
  `temperature_requirements` enum('normal','frozen','refrigerated') DEFAULT 'normal',
  `supplier_id` int DEFAULT NULL,
  `status` enum('active','discontinued') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `goods_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='货物信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` (`id`, `name`, `sku`, `category`, `description`, `weight`, `volume`, `unit_price`, `fragile`, `hazardous`, `temperature_requirements`, `supplier_id`, `status`, `created_at`, `updated_at`) VALUES (1,'电子产品A','ELEC001','electronics','高端电子产品',2.50,0.05,500.00,0,0,'normal',NULL,'active','2025-07-02 09:42:54','2025-07-02 18:54:53'),(2,'服装批次B','CLTH002','clothing','春季服装系列',1.20,0.08,200.00,0,0,'normal',NULL,'active','2025-07-02 09:42:54','2025-07-06 07:58:54'),(3,'食品包装C','FOOD003','food','速冻食品包装',5.00,0.12,50.00,0,0,'normal',NULL,'active','2025-07-02 09:42:54','2025-07-06 07:58:57');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `warehouse_id` int NOT NULL,
  `goods_id` int NOT NULL,
  `quantity` int DEFAULT '0',
  `reserved_quantity` int DEFAULT '0',
  `location` varchar(50) DEFAULT NULL,
  `batch_number` varchar(50) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_warehouse_goods` (`warehouse_id`,`goods_id`,`batch_number`),
  KEY `idx_inventory_warehouse` (`warehouse_id`),
  KEY `idx_inventory_goods` (`goods_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`) ON DELETE CASCADE,
  CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'unread',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `goods_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `volume` decimal(10,2) DEFAULT NULL,
  `special_handling` text,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `goods_id` (`goods_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `order_summary`
--

DROP TABLE IF EXISTS `order_summary`;
/*!50001 DROP VIEW IF EXISTS `order_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `order_summary` AS SELECT 
 1 AS `order_id`,
 1 AS `order_number`,
 1 AS `customer_name`,
 1 AS `customer_phone`,
 1 AS `origin_city`,
 1 AS `destination_city`,
 1 AS `total_weight`,
 1 AS `total_amount`,
 1 AS `order_status`,
 1 AS `payment_status`,
 1 AS `expected_delivery_date`,
 1 AS `order_date`,
 1 AS `created_by_user`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_number` varchar(50) NOT NULL,
  `customer_id` int NOT NULL,
  `origin_address` text,
  `origin_city` varchar(50) DEFAULT NULL,
  `origin_province` varchar(50) DEFAULT NULL,
  `destination_address` text,
  `destination_city` varchar(50) DEFAULT NULL,
  `destination_province` varchar(50) DEFAULT NULL,
  `pickup_date` date DEFAULT NULL,
  `delivery_date` date DEFAULT NULL,
  `expected_delivery_date` date DEFAULT NULL,
  `total_weight` decimal(10,2) DEFAULT NULL,
  `total_volume` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_status` enum('pending','paid','partial','refunded') DEFAULT 'pending',
  `order_status` enum('pending','confirmed','picked_up','in_transit','delivered','cancelled') DEFAULT 'pending',
  `priority` enum('low','normal','high','urgent') DEFAULT 'normal',
  `remarks` text,
  `created_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`),
  KEY `created_by` (`created_by`),
  KEY `idx_orders_customer_id` (`customer_id`),
  KEY `idx_orders_status` (`order_status`),
  KEY `idx_orders_date` (`created_at`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` (`id`, `order_number`, `customer_id`, `origin_address`, `origin_city`, `origin_province`, `destination_address`, `destination_city`, `destination_province`, `pickup_date`, `delivery_date`, `expected_delivery_date`, `total_weight`, `total_volume`, `total_amount`, `payment_status`, `order_status`, `priority`, `remarks`, `created_by`, `created_at`, `updated_at`) VALUES (8,'ORD202501020001',14,'北京市朝阳区建国路88号','北京',NULL,'上海市浦东新区陆家嘴环路1000号','上海',NULL,'2025-01-03',NULL,'2025-01-05',50.50,2.50,450.00,'pending','in_transit','high','电子产品，易碎物品，请小心搬运',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(9,'ORD202501020002',15,'杭州市余杭区文一西路969号','杭州',NULL,'深圳市南山区科技园','深圳',NULL,'2025-01-04',NULL,'2025-01-06',80.00,4.00,320.00,'pending','pending','normal','服装货品，防潮包装',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(10,'ORD202501020003',16,'深圳市福田区益田路6009号','深圳',NULL,'广州市天河区珠江新城','广州',NULL,'2025-01-05',NULL,'2025-01-07',120.00,6.50,280.00,'pending','confirmed','low','食品用品，保质期注意',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(11,'ORD202501020004',17,'上海市青浦区华新镇','上海',NULL,'北京市海淀区中关村','北京',NULL,'2025-01-06',NULL,'2025-01-08',35.00,1.80,220.00,'pending','pending','normal','办公用品，标准包装',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(12,'ORD202501020005',18,'北京市朝阳区望京东路6号','北京',NULL,'成都市高新区天府大道','成都',NULL,'2025-01-07',NULL,'2025-01-10',200.00,8.00,680.00,'pending','confirmed','high','机械设备，重型货物，需专用设备',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(13,'ORD202501020006',19,'深圳市龙岗区坂田华为基地','深圳',NULL,'西安市高新区科技路','西安',NULL,'2025-01-08',NULL,'2025-01-11',75.00,3.20,420.00,'pending','pending','high','通信设备，防静电包装',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(14,'ORD202501020007',20,'北京市海淀区清河中街68号','北京',NULL,'武汉市洪山区光谷大道','武汉',NULL,'2025-01-09',NULL,'2025-01-12',25.00,1.20,180.00,'pending','in_transit','normal','手机配件，精密包装',NULL,'2025-07-02 11:57:39','2025-07-02 11:57:39'),(15,'ORD20250703014230919',1,'北京市朝阳区建国路88号','北京',NULL,'上海市浦东新区陆家嘴环路1000号','上海',NULL,NULL,NULL,NULL,50.50,2.50,950.00,'pending','pending','high','电子产品，易碎品，请小心搬运',NULL,'2025-07-02 17:42:30','2025-07-02 17:43:10'),(16,'ORD-202401',1,'杭州市西湖区文一西路969号','杭州','浙江','深圳市南山区科技园','深圳','广东','2024-07-20',NULL,'2024-07-25',500.00,2.50,1500.00,'pending','in_transit','high',NULL,1,'2025-07-06 03:07:44','2025-07-06 03:07:44'),(17,'ORD-202402',1,'杭州市滨江区网商路699号','杭州','浙江','北京市海淀区中关村','北京','北京','2024-07-21',NULL,'2024-07-26',1200.00,5.00,3200.00,'pending','pending','normal',NULL,1,'2025-07-06 03:07:44','2025-07-06 03:07:44'),(18,'ORD20250706172300179',1,'yy','广州',NULL,'ee','杭州',NULL,NULL,NULL,NULL,0.08,0.01,221.98,'pending','pending','normal','aaa',1,'2025-07-06 09:23:00','2025-07-06 09:23:00'),(19,'ORD20250707195807132',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 11:58:07','2025-07-07 11:58:07'),(20,'ORD20250707195830268',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 11:58:30','2025-07-07 11:58:30'),(21,'ORD20250707195840915',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 11:58:40','2025-07-07 11:58:40'),(22,'ORD20250707195847778',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 11:58:47','2025-07-07 11:58:47'),(23,'ORD20250707200232268',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 12:02:32','2025-07-07 12:02:32'),(24,'ORD20250707200233612',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 12:02:33','2025-07-07 12:02:33'),(25,'ORD20250707200233091',1,'宁夏',NULL,NULL,'西安',NULL,NULL,'2025-06-30',NULL,'2025-07-07',2.00,1.00,100.00,'pending','pending','low','电子设备',1,'2025-07-07 12:02:33','2025-07-07 12:02:33'),(27,'ORD20250707200508225',1,'订单',NULL,NULL,'存储',NULL,NULL,NULL,NULL,NULL,90.00,0.01,675.00,'pending','confirmed','urgent',NULL,1,'2025-07-07 12:05:08','2025-07-07 14:03:36'),(28,'ORDdbcd0dbe-5bb0-11f0-8660-7ec975eb3f75',1,'北京测试仓库',NULL,NULL,'上海测试客户',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1500.00,'pending','in_transit','normal',NULL,NULL,'2025-07-08 04:06:00','2025-07-08 04:06:00');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping_rates`
--

DROP TABLE IF EXISTS `shipping_rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping_rates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `origin_city` varchar(50) DEFAULT NULL,
  `destination_city` varchar(50) DEFAULT NULL,
  `transport_type` enum('express','standard','economy') DEFAULT 'standard',
  `weight_range_min` decimal(10,2) DEFAULT NULL,
  `weight_range_max` decimal(10,2) DEFAULT NULL,
  `base_rate` decimal(10,2) DEFAULT NULL,
  `rate_per_kg` decimal(10,2) DEFAULT NULL,
  `rate_per_km` decimal(10,2) DEFAULT NULL,
  `fuel_surcharge_rate` decimal(5,4) DEFAULT '0.0000',
  `effective_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运费模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_rates`
--

LOCK TABLES `shipping_rates` WRITE;
/*!40000 ALTER TABLE `shipping_rates` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_rates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  `city` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `service_type` enum('transport','warehouse','packaging','insurance') DEFAULT 'transport',
  `rating` decimal(3,2) DEFAULT '0.00',
  `status` enum('active','inactive') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `transport_summary`
--

DROP TABLE IF EXISTS `transport_summary`;
/*!50001 DROP VIEW IF EXISTS `transport_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `transport_summary` AS SELECT 
 1 AS `transport_id`,
 1 AS `transport_number`,
 1 AS `order_number`,
 1 AS `customer_name`,
 1 AS `driver_name`,
 1 AS `license_plate`,
 1 AS `vehicle_brand`,
 1 AS `transport_status`,
 1 AS `start_time`,
 1 AS `end_time`,
 1 AS `distance`,
 1 AS `total_cost`,
 1 AS `origin_city`,
 1 AS `destination_city`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `transport_tracking`
--

DROP TABLE IF EXISTS `transport_tracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transport_tracking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transport_id` int NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `latitude` decimal(10,6) DEFAULT NULL,
  `longitude` decimal(10,6) DEFAULT NULL,
  `status_update` varchar(200) DEFAULT NULL,
  `tracking_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `notes` text,
  PRIMARY KEY (`id`),
  KEY `transport_id` (`transport_id`),
  CONSTRAINT `transport_tracking_ibfk_1` FOREIGN KEY (`transport_id`) REFERENCES `transports` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运输轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transport_tracking`
--

LOCK TABLES `transport_tracking` WRITE;
/*!40000 ALTER TABLE `transport_tracking` DISABLE KEYS */;
INSERT INTO `transport_tracking` (`id`, `transport_id`, `location`, `latitude`, `longitude`, `status_update`, `tracking_time`, `notes`) VALUES (1,13,'北京市',NULL,NULL,'车辆已从【北京仓库】出发','2025-07-08 03:07:55',NULL),(2,13,'天津市',NULL,NULL,'到达【天津中转站】','2025-07-08 04:07:55',NULL);
/*!40000 ALTER TABLE `transport_tracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transports`
--

DROP TABLE IF EXISTS `transports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transport_number` varchar(50) NOT NULL,
  `order_id` int NOT NULL,
  `vehicle_id` int DEFAULT NULL,
  `driver_id` int DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `estimated_duration` int DEFAULT NULL,
  `actual_duration` int DEFAULT NULL,
  `distance` decimal(10,2) DEFAULT NULL,
  `fuel_cost` decimal(10,2) DEFAULT NULL,
  `toll_cost` decimal(10,2) DEFAULT NULL,
  `other_costs` decimal(10,2) DEFAULT NULL,
  `total_cost` decimal(10,2) DEFAULT NULL,
  `transport_status` enum('planned','in_progress','completed','cancelled','delayed') DEFAULT 'planned',
  `notes` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transport_number` (`transport_number`),
  KEY `order_id` (`order_id`),
  KEY `vehicle_id` (`vehicle_id`),
  KEY `driver_id` (`driver_id`),
  KEY `idx_transports_status` (`transport_status`),
  KEY `idx_transports_date` (`created_at`),
  CONSTRAINT `transports_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transports_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`) ON DELETE SET NULL,
  CONSTRAINT `transports_ibfk_3` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运输记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transports`
--

LOCK TABLES `transports` WRITE;
/*!40000 ALTER TABLE `transports` DISABLE KEYS */;
INSERT INTO `transports` (`id`, `transport_number`, `order_id`, `vehicle_id`, `driver_id`, `start_time`, `end_time`, `estimated_duration`, `actual_duration`, `distance`, `fuel_cost`, `toll_cost`, `other_costs`, `total_cost`, `transport_status`, `notes`, `created_at`, `updated_at`) VALUES (9,'TRP202501020001',8,1,1,'2025-01-03 00:00:00','2025-01-05 00:00:00',2880,NULL,1200.50,480.00,150.00,50.00,680.00,'completed','高速公路运输，预计48小时到达','2025-07-02 12:00:20','2025-07-02 18:35:39'),(10,'TRP202501020002',10,18,2,'2025-01-05 01:00:00','2025-01-06 07:00:00',1800,NULL,120.00,50.00,15.00,10.00,75.00,'completed','城际短途运输','2025-07-02 12:00:20','2025-07-02 18:35:39'),(11,'TRP202501020003',12,19,3,'2025-01-06 23:00:00',NULL,4320,NULL,1850.00,740.00,200.00,80.00,1020.00,'planned','长途运输，重型货物','2025-07-02 12:00:20','2025-07-02 12:00:20'),(12,'TRP202501020004',14,2,4,'2025-01-09 02:00:00','2025-01-11 14:00:00',3600,NULL,1050.00,420.00,120.00,40.00,580.00,'completed','标准货运，中长途','2025-07-02 12:00:20','2025-07-02 18:36:49'),(13,'TRANS1214cbb4-5bb1-11f0-8660-7ec975eb3f75',28,1,1,'2025-07-08 04:07:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'in_progress',NULL,'2025-07-08 04:07:31','2025-07-08 04:07:31');
/*!40000 ALTER TABLE `transports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT 'user',
  `status` varchar(20) DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表 (旧，建议弃用)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `status`, `created_at`, `last_login_at`) VALUES (1,'alibaba','e10adc3949ba59abbe56e057f20f883e','alibaba@example.com','12345678901','user','active','2025-07-06 03:02:16',NULL),(2,'tencent','$2a$10$zY.oWMMaRV9qhD5F1LOoxeW6ipBEafmwY6IktGjGUuqJZwuVuF7rO','tencent@example.com','12345678902','user','active','2025-07-06 03:02:16',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('admin','manager','employee','driver','customer') DEFAULT 'employee',
  `employee_id` int DEFAULT NULL,
  `driver_id` int DEFAULT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_users_employees_idx` (`employee_id`),
  KEY `fk_users_drivers_idx` (`driver_id`),
  CONSTRAINT `fk_users_drivers` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_users_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `password`, `email`, `phone`, `role`, `employee_id`, `driver_id`, `status`, `created_at`, `updated_at`) VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e','admin@logistics.com','13800138001','admin',NULL,NULL,'active','2025-07-02 09:30:40','2025-07-02 12:13:32'),(2,'manager1','e10adc3949ba59abbe56e057f20f883e','manager1@logistics.com','13800138002','manager',NULL,NULL,'active','2025-07-02 09:30:40','2025-07-02 12:13:32'),(3,'driver1','e10adc3949ba59abbe56e057f20f883e','driver1@logistics.com','13800138003','driver',NULL,1,'active','2025-07-02 09:30:40','2025-07-08 03:33:24'),(4,'employee1','e10adc3949ba59abbe56e057f20f883e','employee1@logistics.com','13800138004','employee',2,NULL,'active','2025-07-02 09:30:40','2025-07-08 03:33:17'),(10,'employee2','e10adc3949ba59abbe56e057f20f883e','employee2@logistics.com','13800138003','employee',NULL,NULL,'active','2025-07-02 11:46:11','2025-07-02 12:13:58'),(11,'driver2','e10adc3949ba59abbe56e057f20f883e','driver2@logistics.com','13800138004','driver',NULL,2,'active','2025-07-02 11:46:11','2025-07-08 03:33:30'),(12,'driver3','e10adc3949ba59abbe56e057f20f883e','driver3@logistics.com','13800138005','driver',NULL,NULL,'active','2025-07-02 11:46:11','2025-07-02 12:13:58'),(13,'manager2','e10adc3949ba59abbe56e057f20f883e','manager2@logistics.com','13800138006','manager',NULL,NULL,'active','2025-07-02 11:46:11','2025-07-02 12:13:58'),(14,'alibaba','e10adc3949ba59abbe56e057f20f883e','alibaba@logistics.com','0571-85022088','customer',NULL,NULL,'active','2025-07-04 06:04:18','2025-07-04 06:04:18'),(15,'tencent','e10adc3949ba59abbe56e057f20f883e','tencent@logistics.com','0755-86013388','customer',NULL,NULL,'active','2025-07-04 06:04:18','2025-07-04 06:04:18');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(20) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `vehicle_type` enum('truck','van','pickup','container') DEFAULT 'truck',
  `capacity_weight` decimal(10,2) DEFAULT NULL,
  `capacity_volume` decimal(10,2) DEFAULT NULL,
  `fuel_type` enum('gasoline','diesel','electric','hybrid') DEFAULT 'diesel',
  `year_manufactured` int DEFAULT NULL,
  `driver_id` int DEFAULT NULL,
  `status` enum('available','in_use','maintenance','inactive') DEFAULT 'available',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_plate` (`license_plate`),
  KEY `driver_id` (`driver_id`),
  KEY `idx_vehicles_status` (`status`),
  CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='车辆信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` (`id`, `license_plate`, `brand`, `model`, `vehicle_type`, `capacity_weight`, `capacity_volume`, `fuel_type`, `year_manufactured`, `driver_id`, `status`, `created_at`, `updated_at`) VALUES (1,'京A12345','东风','DFL1160','truck',15000.00,40.00,'diesel',NULL,1,'available','2025-07-02 09:34:46','2025-07-02 09:34:46'),(2,'沪B67890','解放','CA1180','truck',12000.00,35.00,'diesel',NULL,2,'available','2025-07-02 09:34:46','2025-07-02 09:34:46'),(3,'粤C11111','福田','BJ1049','van',3000.00,15.00,'diesel',NULL,3,'available','2025-07-02 09:34:46','2025-07-02 09:34:46'),(18,'京B23456','东风','天龙','truck',15.00,45.00,'diesel',2020,2,'available','2025-07-02 11:53:53','2025-07-02 11:53:53'),(19,'京C34567','重汽','豪沃','truck',20.00,60.00,'diesel',2018,3,'in_use','2025-07-02 11:53:53','2025-07-02 11:53:53'),(20,'京D45678','江淮','帅铃','van',5.00,15.00,'diesel',2021,4,'available','2025-07-02 11:53:53','2025-07-02 11:53:53'),(21,'京E56789','陕汽','德龙','truck',25.00,75.00,'diesel',2019,5,'in_use','2025-07-02 11:53:53','2025-07-02 11:53:53'),(22,'沪A11111','福田','欧曼','truck',12.00,36.00,'diesel',2020,NULL,'available','2025-07-02 11:53:53','2025-07-02 11:53:53'),(23,'浙A22222','五菱','荣光','van',3.00,9.00,'diesel',2021,NULL,'available','2025-07-02 11:53:53','2025-07-02 11:53:53'),(24,'苏B33333','江铃','顺达','truck',8.00,24.00,'diesel',2020,NULL,'available','2025-07-02 11:53:53','2025-07-02 11:53:53');
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouses`
--

DROP TABLE IF EXISTS `warehouses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `code` varchar(20) NOT NULL,
  `address` text,
  `city` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `capacity` decimal(10,2) DEFAULT NULL,
  `manager_id` int DEFAULT NULL,
  `warehouse_type` enum('storage','distribution','cold_storage','hazardous') DEFAULT 'storage',
  `status` enum('active','inactive','maintenance') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `warehouses_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='仓库信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouses`
--

LOCK TABLES `warehouses` WRITE;
/*!40000 ALTER TABLE `warehouses` DISABLE KEYS */;
INSERT INTO `warehouses` (`id`, `name`, `code`, `address`, `city`, `province`, `postal_code`, `capacity`, `manager_id`, `warehouse_type`, `status`, `created_at`, `updated_at`) VALUES (1,'北京中心仓库','BJ001','北京市大兴区物流园区','北京','北京市',NULL,10000.00,2,'storage','maintenance','2025-07-02 09:38:54','2025-07-02 18:41:06'),(2,'上海配送中心','SH001','上海市松江区工业园','上海','上海市',NULL,8000.00,2,'storage','active','2025-07-02 09:38:54','2025-07-02 09:38:54'),(3,'广州南方仓库','GZ001','广州市白云区物流基地','广州','广东省',NULL,6000.00,2,'storage','active','2025-07-02 09:38:54','2025-07-02 09:38:54');
/*!40000 ALTER TABLE `warehouses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `order_summary`
--

/*!50001 DROP VIEW IF EXISTS `order_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `order_summary` AS select `o`.`id` AS `order_id`,`o`.`order_number` AS `order_number`,`c`.`name` AS `customer_name`,`c`.`phone` AS `customer_phone`,`o`.`origin_city` AS `origin_city`,`o`.`destination_city` AS `destination_city`,`o`.`total_weight` AS `total_weight`,`o`.`total_amount` AS `total_amount`,`o`.`order_status` AS `order_status`,`o`.`payment_status` AS `payment_status`,`o`.`expected_delivery_date` AS `expected_delivery_date`,`o`.`created_at` AS `order_date`,`u`.`username` AS `created_by_user` from ((`orders` `o` left join `customers` `c` on((`o`.`customer_id` = `c`.`id`))) left join `users` `u` on((`o`.`created_by` = `u`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `transport_summary`
--

/*!50001 DROP VIEW IF EXISTS `transport_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `transport_summary` AS select `t`.`id` AS `transport_id`,`t`.`transport_number` AS `transport_number`,`o`.`order_number` AS `order_number`,`c`.`name` AS `customer_name`,`d`.`name` AS `driver_name`,`v`.`license_plate` AS `license_plate`,`v`.`brand` AS `vehicle_brand`,`t`.`transport_status` AS `transport_status`,`t`.`start_time` AS `start_time`,`t`.`end_time` AS `end_time`,`t`.`distance` AS `distance`,`t`.`total_cost` AS `total_cost`,`o`.`origin_city` AS `origin_city`,`o`.`destination_city` AS `destination_city` from ((((`transports` `t` left join `orders` `o` on((`t`.`order_id` = `o`.`id`))) left join `customers` `c` on((`o`.`customer_id` = `c`.`id`))) left join `drivers` `d` on((`t`.`driver_id` = `d`.`id`))) left join `vehicles` `v` on((`t`.`vehicle_id` = `v`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-08 17:13:25
