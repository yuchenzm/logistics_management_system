/*
 * @Author: yuchenzm Wcm136677@163.com
 * @Date: 2025-07-02 17:57:07
 * @LastEditors: yuchenzm Wcm136677@163.com
 * @LastEditTime: 2025-07-02 20:04:01
 * @FilePath: /wuliu guanli/logistics-system/backend/src/main/java/com/logistics/mapper/CustomerMapper.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.logistics.mapper;

import com.logistics.entity.Customer;
import com.logistics.common.PageRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 客户数据访问层
 */
@Mapper
public interface CustomerMapper {
    
    @Select("SELECT * FROM customers WHERE id = #{id}")
    Customer findById(Long id);
    
    @Select("SELECT * FROM customers WHERE status = 'active' ORDER BY created_at DESC")
    List<Customer> findAllActive();
    
    @Select("SELECT * FROM customers WHERE name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "contact_person LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%')")
    List<Customer> searchByKeyword(String keyword);
    
    @Select("SELECT * FROM customers ORDER BY created_at DESC LIMIT #{offset}, #{size}")
    List<Customer> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM customers")
    int count();
    
    // 多条件搜索分页查询
    @Select("<script>" +
            "SELECT * FROM customers WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "contact_person LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='city != null and city != \"\"'>" +
            "AND city = #{city} " +
            "</if>" +
            "<if test='creditRating != null and creditRating != \"\"'>" +
            "AND credit_rating = #{creditRating} " +
            "</if>" +
            "ORDER BY created_at DESC LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Customer> findByPageWithConditions(PageRequest pageRequest);
    
    // 多条件搜索计数
    @Select("<script>" +
            "SELECT COUNT(*) FROM customers WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "contact_person LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='city != null and city != \"\"'>" +
            "AND city = #{city} " +
            "</if>" +
            "<if test='creditRating != null and creditRating != \"\"'>" +
            "AND credit_rating = #{creditRating} " +
            "</if>" +
            "</script>")
    int countWithConditions(PageRequest pageRequest);
    
    @Insert("INSERT INTO customers(name, contact_person, phone, email, address, city, " +
            "province, postal_code, customer_type, credit_rating, status) " +
            "VALUES(#{name}, #{contactPerson}, #{phone}, #{email}, #{address}, #{city}, " +
            "#{province}, #{postalCode}, #{customerType}, #{creditRating}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Customer customer);
    
    @Update("UPDATE customers SET name=#{name}, contact_person=#{contactPerson}, " +
            "phone=#{phone}, email=#{email}, address=#{address}, city=#{city}, " +
            "province=#{province}, postal_code=#{postalCode}, customer_type=#{customerType}, " +
            "credit_rating=#{creditRating}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Customer customer);
    
    @Delete("DELETE FROM customers WHERE id = #{id}")
    int delete(Long id);
} 