package com.logistics.mapper;

import com.logistics.entity.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 反馈映射器接口
 */
@Mapper
public interface FeedbackMapper {
    
    @Select("SELECT * FROM feedback WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Feedback> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM feedback WHERE id = #{id}")
    Feedback findById(@Param("id") Long id);
    
    @Insert("INSERT INTO feedback (user_id, type, title, content, status, create_time, update_time) " +
            "VALUES(#{userId}, #{type}, #{title}, #{content}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Feedback feedback);
    
    @Update("UPDATE feedback SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Delete("DELETE FROM feedback WHERE id = #{id}")
    int delete(@Param("id") Long id);
    
    @Select("SELECT * FROM feedback ORDER BY create_time DESC")
    List<Feedback> findAll();
    
    @Select("SELECT * FROM feedback WHERE status = #{status} ORDER BY create_time DESC")
    List<Feedback> findByStatus(@Param("status") String status);
} 