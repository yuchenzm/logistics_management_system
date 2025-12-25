package com.logistics.mapper;

import com.logistics.entity.Warehouse;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WarehouseMapper {
    
    @Select("SELECT * FROM warehouses")
    List<Warehouse> findAll();
    
    @Select("SELECT * FROM warehouses WHERE id = #{id}")
    Warehouse findById(Integer id);
    
    @Select("SELECT * FROM warehouses WHERE code = #{code}")
    Warehouse findByCode(String code);
    
    @Select("SELECT * FROM warehouses WHERE city = #{city}")
    List<Warehouse> findByCity(String city);
    
    @Select("SELECT * FROM warehouses WHERE warehouse_type = #{warehouseType}")
    List<Warehouse> findByType(String warehouseType);
    
    @Select("SELECT * FROM warehouses WHERE status = #{status}")
    List<Warehouse> findByStatus(String status);
    
    @Select("SELECT * FROM warehouses WHERE manager_id = #{managerId}")
    List<Warehouse> findByManagerId(Integer managerId);
    
    @Select("SELECT * FROM warehouses WHERE name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR city LIKE CONCAT('%', #{keyword}, '%')")
    List<Warehouse> searchByKeyword(String keyword);
    
    @Insert("INSERT INTO warehouses (name, code, address, city, province, postal_code, " +
            "capacity, manager_id, warehouse_type, status) " +
            "VALUES (#{name}, #{code}, #{address}, #{city}, #{province}, #{postalCode}, " +
            "#{capacity}, #{managerId}, #{warehouseType}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Warehouse warehouse);
    
    @Update("UPDATE warehouses SET name = #{name}, code = #{code}, address = #{address}, " +
            "city = #{city}, province = #{province}, postal_code = #{postalCode}, " +
            "capacity = #{capacity}, manager_id = #{managerId}, warehouse_type = #{warehouseType}, " +
            "status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int update(Warehouse warehouse);
    
    @Update("UPDATE warehouses SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    
    @Delete("DELETE FROM warehouses WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM warehouses")
    int count();
    
    @Select("SELECT COUNT(*) FROM warehouses WHERE status = #{status}")
    int countByStatus(String status);
    
    @Select("SELECT DISTINCT city FROM warehouses WHERE city IS NOT NULL ORDER BY city")
    List<String> findAllCities();
    
    @Select("SELECT DISTINCT warehouse_type FROM warehouses WHERE warehouse_type IS NOT NULL ORDER BY warehouse_type")
    List<String> findAllTypes();
    
    // 多条件分页查询
    @Select("<script>" +
            "SELECT * FROM warehouses WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR city LIKE CONCAT('%', #{keyword}, '%') " +
            "OR address LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='warehouseType != null and warehouseType != \"\"'>" +
            "AND warehouse_type = #{warehouseType} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND status = #{status} " +
            "</if>" +
            "ORDER BY created_at DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Warehouse> findByPageWithConditions(@Param("keyword") String keyword,
                                           @Param("warehouseType") String warehouseType,
                                           @Param("status") String status,
                                           @Param("offset") int offset,
                                           @Param("pageSize") int pageSize);
    
    // 多条件计数查询
    @Select("<script>" +
            "SELECT COUNT(*) FROM warehouses WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR city LIKE CONCAT('%', #{keyword}, '%') " +
            "OR address LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='warehouseType != null and warehouseType != \"\"'>" +
            "AND warehouse_type = #{warehouseType} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND status = #{status} " +
            "</if>" +
            "</script>")
    int countWithConditions(@Param("keyword") String keyword,
                          @Param("warehouseType") String warehouseType,
                          @Param("status") String status);
} 