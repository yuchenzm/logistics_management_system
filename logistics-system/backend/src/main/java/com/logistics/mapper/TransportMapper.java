package com.logistics.mapper;

import com.logistics.entity.Transport;
import com.logistics.common.PageRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 运输数据访问层
 */
@Mapper
public interface TransportMapper {
    
    @Select("SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE t.id = #{id}")
    Transport findById(Long id);
    
    @Select("SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE t.transport_number = #{transportNumber}")
    Transport findByTransportNumber(String transportNumber);
    
    @Select("SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE t.transport_status = #{status} ORDER BY t.created_at DESC")
    List<Transport> findByStatus(String status);
    
    @Select("SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE t.transport_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "o.order_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "d.name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.license_plate LIKE CONCAT('%', #{keyword}, '%')")
    List<Transport> searchByKeyword(String keyword);
    
    @Select("SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "ORDER BY t.created_at DESC LIMIT #{offset}, #{size}")
    List<Transport> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM transports")
    int count();
    
    // 多条件搜索分页查询
    @Select("<script>" +
            "SELECT t.*, o.order_number, d.name as driver_name, v.license_plate as vehicle_license_plate, " +
            "o.origin_city, o.destination_city " +
            "FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (t.transport_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "o.order_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "d.name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.license_plate LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND t.transport_status = #{status} " +
            "</if>" +
            "<if test='transportStatus != null and transportStatus != \"\"'>" +
            "AND t.transport_status = #{transportStatus} " +
            "</if>" +
            "<if test='transportType != null and transportType != \"\"'>" +
            "AND t.transport_type = #{transportType} " +
            "</if>" +
            "ORDER BY t.created_at DESC LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Transport> findByPageWithConditions(PageRequest pageRequest);
    
    // 多条件搜索计数
    @Select("<script>" +
            "SELECT COUNT(*) FROM transports t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN drivers d ON t.driver_id = d.id " +
            "LEFT JOIN vehicles v ON t.vehicle_id = v.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (t.transport_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "o.order_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "d.name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.license_plate LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND t.transport_status = #{status} " +
            "</if>" +
            "<if test='transportStatus != null and transportStatus != \"\"'>" +
            "AND t.transport_status = #{transportStatus} " +
            "</if>" +
            "<if test='transportType != null and transportType != \"\"'>" +
            "AND t.transport_type = #{transportType} " +
            "</if>" +
            "</script>")
    int countWithConditions(PageRequest pageRequest);
    
    @Insert("INSERT INTO transports(transport_number, order_id, vehicle_id, driver_id, " +
            "start_time, estimated_duration, distance, fuel_cost, toll_cost, other_costs, " +
            "total_cost, transport_status, notes) " +
            "VALUES(#{transportNumber}, #{orderId}, #{vehicleId}, #{driverId}, " +
            "#{startTime}, #{estimatedDuration}, #{distance}, #{fuelCost}, #{tollCost}, " +
            "#{otherCosts}, #{totalCost}, #{transportStatus}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Transport transport);
    
    @Update("UPDATE transports SET transport_number=#{transportNumber}, order_id=#{orderId}, " +
            "vehicle_id=#{vehicleId}, driver_id=#{driverId}, start_time=#{startTime}, " +
            "estimated_duration=#{estimatedDuration}, distance=#{distance}, fuel_cost=#{fuelCost}, " +
            "toll_cost=#{tollCost}, other_costs=#{otherCosts}, total_cost=#{totalCost}, " +
            "transport_status=#{transportStatus}, notes=#{notes}, updated_at=NOW() WHERE id=#{id}")
    int update(Transport transport);
    
    @Delete("DELETE FROM transports WHERE id = #{id}")
    int delete(Long id);
} 