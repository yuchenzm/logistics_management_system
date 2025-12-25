package com.logistics.mapper;

import com.logistics.entity.Driver;
import com.logistics.common.PageRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 司机数据访问层
 */
@Mapper
public interface DriverMapper {
    
    @Select("SELECT * FROM drivers WHERE id = #{id}")
    Driver findById(Long id);
    
    @Select("SELECT * FROM drivers WHERE license_number = #{licenseNumber}")
    Driver findByLicenseNumber(String licenseNumber);
    
    @Select("SELECT * FROM drivers WHERE status = 'available' ORDER BY rating DESC")
    List<Driver> findAvailableDrivers();
    
    @Select("SELECT * FROM drivers WHERE status = #{status} ORDER BY created_at DESC")
    List<Driver> findByStatus(String status);
    
    @Select("SELECT * FROM drivers WHERE name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "license_number LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%')")
    List<Driver> searchByKeyword(String keyword);
    
    @Select("SELECT * FROM drivers ORDER BY created_at DESC LIMIT #{offset}, #{size}")
    List<Driver> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM drivers")
    int count();
    
    // 多条件搜索分页查询
    @Select("<script>" +
            "SELECT * FROM drivers WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%') OR " +
            "license_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND status = #{status} " +
            "</if>" +
            "<if test='licenseType != null and licenseType != \"\"'>" +
            "AND license_type = #{licenseType} " +
            "</if>" +
            "ORDER BY created_at DESC LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Driver> findByPageWithConditions(PageRequest pageRequest);
    
    // 多条件搜索计数
    @Select("<script>" +
            "SELECT COUNT(*) FROM drivers WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%') OR " +
            "license_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "AND status = #{status} " +
            "</if>" +
            "<if test='licenseType != null and licenseType != \"\"'>" +
            "AND license_type = #{licenseType} " +
            "</if>" +
            "</script>")
    int countWithConditions(PageRequest pageRequest);
    
    @Insert("INSERT INTO drivers(name, license_number, phone, email, address, hire_date, " +
            "license_type, experience_years, rating, status) " +
            "VALUES(#{name}, #{licenseNumber}, #{phone}, #{email}, #{address}, #{hireDate}, " +
            "#{licenseType}, #{experienceYears}, #{rating}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Driver driver);
    
    @Update("UPDATE drivers SET name=#{name}, phone=#{phone}, email=#{email}, " +
            "address=#{address}, license_type=#{licenseType}, experience_years=#{experienceYears}, " +
            "rating=#{rating}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Driver driver);
    
    @Update("UPDATE drivers SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Delete("DELETE FROM drivers WHERE id = #{id}")
    int delete(Long id);
} 