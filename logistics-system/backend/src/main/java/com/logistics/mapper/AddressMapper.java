package com.logistics.mapper;

import com.logistics.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 地址映射器接口
 */
@Mapper
public interface AddressMapper {
    
    @Select("SELECT * FROM addresses WHERE user_id = #{userId} ORDER BY is_default DESC, update_time DESC")
    List<Address> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM addresses WHERE id = #{id}")
    Address findById(@Param("id") Long id);
    
    @Insert("INSERT INTO addresses (user_id, contact_name, contact_phone, province, city, district, address, is_default, create_time, update_time) " +
            "VALUES(#{userId}, #{contactName}, #{contactPhone}, #{province}, #{city}, #{district}, #{address}, #{isDefault}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Address address);
    
    @Update("UPDATE addresses SET " +
            "contact_name = #{contactName}, " +
            "contact_phone = #{contactPhone}, " +
            "province = #{province}, " +
            "city = #{city}, " +
            "district = #{district}, " +
            "address = #{address}, " +
            "is_default = #{isDefault}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    int update(Address address);
    
    @Update("UPDATE addresses SET is_default = true WHERE id = #{id}")
    int setDefault(@Param("id") Long id);
    
    @Delete("DELETE FROM addresses WHERE id = #{id}")
    int delete(@Param("id") Long id);
    
    @Update("UPDATE addresses SET is_default = false WHERE user_id = #{userId}")
    int clearDefaultAddress(@Param("userId") Long userId);
} 