package com.logistics.mapper;

import com.logistics.entity.Vehicle;
import com.logistics.common.PageRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 车辆数据访问层
 */
@Mapper
public interface VehicleMapper {
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.id = #{id}")
    Vehicle findById(Long id);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.license_plate = #{licensePlate}")
    Vehicle findByLicensePlate(String licensePlate);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.status = 'available' " +
            "ORDER BY v.capacity_weight DESC")
    List<Vehicle> findAvailableVehicles();
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.status = #{status} " +
            "ORDER BY v.created_at DESC")
    List<Vehicle> findByStatus(String status);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.vehicle_type = #{vehicleType} " +
            "ORDER BY v.created_at DESC")
    List<Vehicle> findByType(String vehicleType);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.driver_id = #{driverId}")
    List<Vehicle> findByDriverId(Long driverId);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE v.license_plate LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.brand LIKE CONCAT('%', #{keyword}, '%') OR v.model LIKE CONCAT('%', #{keyword}, '%')")
    List<Vehicle> searchByKeyword(String keyword);
    
    @Select("SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id ORDER BY v.created_at DESC LIMIT #{offset}, #{size}")
    List<Vehicle> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM vehicles")
    int count();
    
    @Select("<script>" +
            "SELECT v.*, d.name as driver_name FROM vehicles v " +
            "LEFT JOIN drivers d ON v.driver_id = d.id WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (v.license_plate LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.brand LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.model LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='vehicleType != null and vehicleType != \"\"'>" +
            "AND v.vehicle_type = #{vehicleType} " +
            "</if>" +
            "<if test='brand != null and brand != \"\"'>" +
            "AND v.brand = #{brand} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND v.status = #{status} " +
            "</if>" +
            "ORDER BY v.created_at DESC LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Vehicle> findByPageWithConditions(PageRequest pageRequest);
    
    @Select("<script>" +
            "SELECT COUNT(*) FROM vehicles v WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (v.license_plate LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.brand LIKE CONCAT('%', #{keyword}, '%') OR " +
            "v.model LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='vehicleType != null and vehicleType != \"\"'>" +
            "AND v.vehicle_type = #{vehicleType} " +
            "</if>" +
            "<if test='brand != null and brand != \"\"'>" +
            "AND v.brand = #{brand} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND v.status = #{status} " +
            "</if>" +
            "</script>")
    int countWithConditions(PageRequest pageRequest);
    
    @Insert("INSERT INTO vehicles(license_plate, brand, model, vehicle_type, capacity_weight, " +
            "capacity_volume, fuel_type, year_manufactured, driver_id, status) " +
            "VALUES(#{licensePlate}, #{brand}, #{model}, #{vehicleType}, #{capacityWeight}, " +
            "#{capacityVolume}, #{fuelType}, #{yearManufactured}, #{driverId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Vehicle vehicle);
    
    @Update("UPDATE vehicles SET license_plate=#{licensePlate}, brand=#{brand}, " +
            "model=#{model}, vehicle_type=#{vehicleType}, capacity_weight=#{capacityWeight}, " +
            "capacity_volume=#{capacityVolume}, fuel_type=#{fuelType}, " +
            "year_manufactured=#{yearManufactured}, driver_id=#{driverId}, " +
            "status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Vehicle vehicle);
    
    @Update("UPDATE vehicles SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Update("UPDATE vehicles SET driver_id=#{driverId}, updated_at=NOW() WHERE id=#{id}")
    int assignDriver(@Param("id") Long id, @Param("driverId") Long driverId);
    
    @Delete("DELETE FROM vehicles WHERE id = #{id}")
    int delete(Long id);
} 