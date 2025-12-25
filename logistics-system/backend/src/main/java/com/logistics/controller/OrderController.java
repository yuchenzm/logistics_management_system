package com.logistics.controller;

import com.logistics.entity.Order;
import com.logistics.service.OrderService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 获取所有订单
     */
    @GetMapping
    public Result<List<Order>> getAll() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }
    
    /**
     * 根据状态查询订单
     */
    @GetMapping("/status/{status}")
    public Result<List<Order>> getByStatus(@PathVariable String status) {
        List<Order> orders = orderService.findByStatus(status);
        return Result.success(orders);
    }
    
    /**
     * 根据客户ID查询订单
     */
    @GetMapping("/customer/{customerId}")
    public Result<List<Order>> getByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        return Result.success(orders);
    }
    
    /**
     * 分页查询订单
     */
    @PostMapping("/page")
    public Result<PageResult<Order>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Order> result = orderService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据订单号查询订单
     */
    @GetMapping("/number/{orderNumber}")
    public Result<Order> getByOrderNumber(@PathVariable String orderNumber) {
        Order order = orderService.findByOrderNumber(orderNumber);
        if (order != null) {
            return Result.success(order);
        } else {
            return Result.error("订单不存在");
        }
    }
    
    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order != null) {
            return Result.success(order);
        } else {
            return Result.error("订单不存在");
        }
    }
    
    /**
     * 保存订单（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Order order) {
        boolean success = orderService.save(order);
        if (success) {
            return Result.success(order.getId() == null ? "订单创建成功" : "订单更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新订单
     */
    @PutMapping("/{id}")
    public Result<Order> update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id); // 确保ID被设置
        boolean success = orderService.save(order);
        if (success) {
            Order updatedOrder = orderService.findById(id);
            return Result.success(updatedOrder);
        } else {
            return Result.error("订单更新失败");
        }
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        boolean success = orderService.updateStatus(order);
        if (success) {
            return Result.success("订单状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = orderService.delete(id);
        if (success) {
            return Result.success("订单删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    
    /**
     * 获取订单统计信息
     */
    @GetMapping("/statistics")
    public Result<OrderService.OrderStatistics> getStatistics() {
        OrderService.OrderStatistics statistics = orderService.getOrderStatistics();
        return Result.success(statistics);
    }
} 