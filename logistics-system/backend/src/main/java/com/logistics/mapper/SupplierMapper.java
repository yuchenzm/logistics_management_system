/*
 * @Author: yuchenzm Wcm136677@163.com
 * @Date: 2025-07-03 00:12:58
 * @LastEditors: yuchenzm Wcm136677@163.com
 * @LastEditTime: 2025-07-06 17:15:50
 * @FilePath: /wuliu guanli/logistics-system/backend/src/main/java/com/logistics/mapper/SupplierMapper.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.logistics.mapper;

import com.logistics.entity.Supplier;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SupplierMapper {
    
    @Select("SELECT * FROM suppliers")
    List<Supplier> findAll();
    
    @Select("SELECT * FROM suppliers WHERE id = #{id}")
    Supplier findById(Integer id);
    
    @Select("SELECT * FROM suppliers WHERE service_type = #{serviceType}")
    List<Supplier> findByServiceType(String serviceType);
    
    @Select("SELECT * FROM suppliers WHERE status = #{status}")
    List<Supplier> findByStatus(String status);
    
    @Select("SELECT * FROM suppliers WHERE city = #{city}")
    List<Supplier> findByCity(String city);
    
    @Select("SELECT * FROM suppliers WHERE name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR contact_person LIKE CONCAT('%', #{keyword}, '%') " +
            "OR phone LIKE CONCAT('%', #{keyword}, '%')")
    List<Supplier> searchByKeyword(String keyword);
    
    @Insert("INSERT INTO suppliers (name, contact_person, phone, email, address, city, " +
            "province, postal_code, service_type, rating, status) " +
            "VALUES (#{name}, #{contactPerson}, #{phone}, #{email}, #{address}, #{city}, " +
            "#{province}, #{postalCode}, #{serviceType}, #{rating}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Supplier supplier);
    
    @Update("UPDATE suppliers SET name = #{name}, contact_person = #{contactPerson}, " +
            "phone = #{phone}, email = #{email}, address = #{address}, city = #{city}, " +
            "province = #{province}, postal_code = #{postalCode}, service_type = #{serviceType}, " +
            "rating = #{rating}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int update(Supplier supplier);
    
    @Update("UPDATE suppliers SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    
    @Update("UPDATE suppliers SET rating = #{rating}, updated_at = NOW() WHERE id = #{id}")
    int updateRating(@Param("id") Integer id, @Param("rating") java.math.BigDecimal rating);
    
    @Select("SELECT * FROM suppliers WHERE " +
            "(#{creditRating} = 'A' AND rating >= 4.5) OR " +
            "(#{creditRating} = 'B' AND rating >= 3.5 AND rating < 4.5) OR " +
            "(#{creditRating} = 'C' AND rating >= 2.5 AND rating < 3.5) OR " +
            "(#{creditRating} = 'D' AND rating < 2.5)")
    List<Supplier> findByCreditRating(String creditRating);
    
    @Update("UPDATE suppliers SET rating = " +
            "CASE #{creditRating} " +
            "WHEN 'A' THEN 5.0 " +
            "WHEN 'B' THEN 4.0 " +
            "WHEN 'C' THEN 3.0 " +
            "WHEN 'D' THEN 2.0 " +
            "ELSE 3.0 END, " +
            "updated_at = NOW() WHERE id = #{id}")
    int updateCreditRating(@Param("id") Integer id, @Param("creditRating") String creditRating);
    
    @Delete("DELETE FROM suppliers WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM suppliers")
    int count();
    
    @Select("SELECT COUNT(*) FROM suppliers WHERE status = #{status}")
    int countByStatus(String status);
    
    @Select("SELECT COUNT(*) FROM suppliers WHERE " +
            "(#{creditRating} = 'A' AND rating >= 4.5) OR " +
            "(#{creditRating} = 'B' AND rating >= 3.5 AND rating < 4.5) OR " +
            "(#{creditRating} = 'C' AND rating >= 2.5 AND rating < 3.5) OR " +
            "(#{creditRating} = 'D' AND rating < 2.5)")
    int countByCreditRating(String creditRating);
    
    @Select("SELECT DISTINCT city FROM suppliers WHERE city IS NOT NULL ORDER BY city")
    List<String> findAllCities();
    
    @Select("SELECT DISTINCT service_type FROM suppliers WHERE service_type IS NOT NULL ORDER BY service_type")
    List<String> findAllServiceTypes();
} 