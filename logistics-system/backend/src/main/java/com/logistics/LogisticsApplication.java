/*
 * @Author: yuchenzm Wcm136677@163.com
 * @Date: 2025-07-02 17:56:05
 * @LastEditors: yuchenzm Wcm136677@163.com
 * @LastEditTime: 2025-07-03 03:20:39
 * @FilePath: /wuliu guanli/logistics-system/backend/src/main/java/com/logistics/LogisticsApplication.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.logistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * 物流管理系统主应用程序
 * @author System
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.logistics.mapper")
public class LogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
        System.out.println("===========================================");
        System.out.println("物流管理系统启动成功！");
        System.out.println("访问地址：http://localhost:8080/api");
        System.out.println("===========================================");
    }
} 