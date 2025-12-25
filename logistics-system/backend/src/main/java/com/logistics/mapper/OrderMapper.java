package com.logistics.mapper;

import com.logistics.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 订单数据访问层
 */
@Mapper
public interface OrderMapper {
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE o.id = #{id}")
    Order findById(Long id);
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE o.order_number = #{orderNumber}")
    Order findByOrderNumber(String orderNumber);
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "ORDER BY o.created_at DESC")
    List<Order> findAll();
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE o.order_status = #{status} ORDER BY o.created_at DESC")
    List<Order> findByStatus(String status);
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE o.customer_id = #{customerId} ORDER BY o.created_at DESC")
    List<Order> findByCustomerId(Long customerId);
    
    @Select("SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "ORDER BY o.created_at DESC LIMIT #{offset}, #{size}")
    List<Order> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("<script>" +
            "SELECT o.*, c.name as customer_name, c.phone as customer_phone " +
            "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (o.order_number LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR o.special_instructions LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND o.order_status = #{status} " +
            "</if>" +
            "<if test='priority != null and priority != \"\"'>" +
            "AND o.priority = #{priority} " +
            "</if>" +
            "ORDER BY o.created_at DESC " +
            "LIMIT #{offset}, #{size}" +
            "</script>")
    List<Order> findByPageWithConditions(@Param("keyword") String keyword, 
                                        @Param("status") String status, 
                                        @Param("priority") String priority,
                                        @Param("offset") int offset, 
                                        @Param("size") int size);
    
    @Select("<script>" +
            "SELECT COUNT(*) FROM orders o LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (o.order_number LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR o.special_instructions LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND o.order_status = #{status} " +
            "</if>" +
            "<if test='priority != null and priority != \"\"'>" +
            "AND o.priority = #{priority} " +
            "</if>" +
            "</script>")
    int countWithConditions(@Param("keyword") String keyword, 
                           @Param("status") String status, 
                           @Param("priority") String priority);
    
    @Select("SELECT COUNT(*) FROM orders")
    int count();
    
    @Insert("INSERT INTO orders(order_number, customer_id, origin_address, origin_city, " +
            "origin_province, destination_address, destination_city, destination_province, " +
            "pickup_date, expected_delivery_date, total_weight, total_volume, total_amount, " +
            "payment_status, order_status, priority, special_instructions, created_by) " +
            "VALUES(#{orderNumber}, #{customerId}, #{originAddress}, #{originCity}, " +
            "#{originProvince}, #{destinationAddress}, #{destinationCity}, #{destinationProvince}, " +
            "#{pickupDate}, #{expectedDeliveryDate}, #{totalWeight}, #{totalVolume}, #{totalAmount}, " +
            "#{paymentStatus}, #{orderStatus}, #{priority}, #{specialInstructions}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);
    
    @Update("UPDATE orders SET order_status=#{orderStatus}, payment_status=#{paymentStatus}, " +
            "delivery_date=#{deliveryDate}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(Order order);
    
    @Update("UPDATE orders SET " +
            "order_status=#{orderStatus}, " +
            "priority=#{priority}, " +
            "origin_address=#{originAddress}, " +
            "destination_address=#{destinationAddress}, " +
            "total_weight=#{totalWeight}, " +
            "total_volume=#{totalVolume}, " +
            "remarks=#{remarks}, " +
            "updated_at=NOW() WHERE id=#{id}")
    int update(Order order);
    
    @Delete("DELETE FROM orders WHERE id = #{id}")
    int delete(Long id);
} 