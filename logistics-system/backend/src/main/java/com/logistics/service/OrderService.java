package com.logistics.service;

import com.logistics.entity.Order;
import com.logistics.mapper.OrderMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单服务类
 */
@Service
public class OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    public Order findById(Long id) {
        return orderMapper.findById(id);
    }
    
    public Order findByOrderNumber(String orderNumber) {
        return orderMapper.findByOrderNumber(orderNumber);
    }
    
    public List<Order> findAll() {
        return orderMapper.findAll();
    }
    
    public List<Order> findByStatus(String status) {
        return orderMapper.findByStatus(status);
    }
    
    public List<Order> findByCustomerId(Long customerId) {
        return orderMapper.findByCustomerId(customerId);
    }
    
    public PageResult<Order> findByPage(PageRequest pageRequest) {
        // 获取搜索参数
        String keyword = pageRequest.getKeyword();
        String status = pageRequest.getStatus();
        String priority = pageRequest.getPriority();
        
        List<Order> orders;
        int total;
        
        // 如果有搜索条件，使用带条件的查询
        if ((keyword != null && !keyword.trim().isEmpty()) ||
            (status != null && !status.trim().isEmpty()) ||
            (priority != null && !priority.trim().isEmpty())) {
            orders = orderMapper.findByPageWithConditions(
                keyword, status, priority, 
                pageRequest.getOffset(), pageRequest.getPageSize()
            );
            total = orderMapper.countWithConditions(keyword, status, priority);
        } else {
            // 否则使用普通分页查询
            orders = orderMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
            total = orderMapper.count();
        }
        
        return PageResult.of(orders, total, pageRequest.getPageNum(), pageRequest.getPageSize());
    }
    
    public boolean save(Order order) {
        if (order.getId() == null) {
            // 新增订单，生成订单号
            order.setOrderNumber(generateOrderNumber());
            
            // 临时的修复：为缺失的字段设置默认值
            order.setCustomerId(1L); // 假设一个默认客户ID
            order.setCreatedBy(1L);   // 假设一个默认创建者ID
            
            if (order.getOrderStatus() == null) {
                order.setOrderStatus("pending");
            }
            if (order.getPaymentStatus() == null) {
                order.setPaymentStatus("pending");
            }
            if (order.getPriority() == null) {
                order.setPriority("normal");
            }
            return orderMapper.insert(order) > 0;
        } else {
            // 更新订单
            return orderMapper.update(order) > 0;
        }
    }
    
    public boolean updateStatus(Order order) {
        return orderMapper.updateStatus(order) > 0;
    }
    
    public boolean delete(Long id) {
        return orderMapper.delete(id) > 0;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int)(Math.random() * 1000));
        return "ORD" + timestamp + String.format("%03d", Integer.parseInt(random));
    }
    
    /**
     * 获取订单统计信息
     */
    public OrderStatistics getOrderStatistics() {
        List<Order> allOrders = orderMapper.findAll();
        
        int total = allOrders.size();
        int pending = (int) allOrders.stream().filter(o -> "pending".equals(o.getOrderStatus())).count();
        int inTransit = (int) allOrders.stream().filter(o -> "in_transit".equals(o.getOrderStatus())).count();
        int delivered = (int) allOrders.stream().filter(o -> "delivered".equals(o.getOrderStatus())).count();
        int cancelled = (int) allOrders.stream().filter(o -> "cancelled".equals(o.getOrderStatus())).count();
        BigDecimal totalAmount = allOrders.stream()
                                          .map(Order::getTotalAmount)
                                          .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new OrderStatistics(total, pending, inTransit, delivered, cancelled, totalAmount);
    }
    
    /**
     * 订单统计信息内部类
     */
    public static class OrderStatistics {
        public int total;
        public int pending;
        public int inTransit;
        public int delivered;
        public int cancelled;
        public BigDecimal totalAmount;
        
        public OrderStatistics(int total, int pending, int inTransit, int delivered, int cancelled, BigDecimal totalAmount) {
            this.total = total;
            this.pending = pending;
            this.inTransit = inTransit;
            this.delivered = delivered;
            this.cancelled = cancelled;
            this.totalAmount = totalAmount;
        }
    }
} 