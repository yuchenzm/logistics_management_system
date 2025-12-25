package com.logistics.mapper;

import com.logistics.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
    
    @Select("SELECT * FROM users WHERE status = 'active' ORDER BY created_at DESC")
    List<User> findAllActive();
    
    @Select("SELECT * FROM users ORDER BY created_at DESC LIMIT #{offset}, #{size}")
    List<User> findByPage(@Param("offset") int offset, @Param("size") int size);
    
    @Select("SELECT COUNT(*) FROM users")
    int count();
    
    @Insert("INSERT INTO users(username, password, email, phone, role, status) " +
            "VALUES(#{username}, #{password}, #{email}, #{phone}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Update("UPDATE users SET email=#{email}, phone=#{phone}, role=#{role}, " +
            "status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(User user);
    
    @Update("UPDATE users SET password=#{password}, updated_at=NOW() WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    int delete(Long id);
} 