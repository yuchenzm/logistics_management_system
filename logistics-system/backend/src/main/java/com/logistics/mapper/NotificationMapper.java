package com.logistics.mapper;

import com.logistics.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 通知映射器接口
 */
@Mapper
public interface NotificationMapper {
    
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Notification> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{size} OFFSET #{offset}")
    List<Notification> findByUserIdAndPage(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM notifications WHERE id = #{id}")
    Notification findById(@Param("id") Long id);
    
    @Insert("INSERT INTO notifications (user_id, type, title, content, status, create_time) " +
            "VALUES (#{userId}, #{type}, #{title}, #{content}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Notification notification);
    
    @Update("UPDATE notifications SET status = 'read', read_time = NOW() WHERE id = #{id}")
    int markAsRead(@Param("id") Long id);
    
    @Update("<script>" +
            "UPDATE notifications SET status = 'read', read_time = NOW() WHERE id IN " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int batchMarkAsRead(@Param("ids") List<Long> ids);
    
    @Update("UPDATE notifications SET status = 'read', read_time = NOW() WHERE user_id = #{userId} AND status = 'unread'")
    int markAllAsReadByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM notifications WHERE id = #{id}")
    int delete(@Param("id") Long id);
    
    @Delete("DELETE FROM notifications WHERE user_id = #{userId}")
    int deleteAllByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId} AND status = 'unread'")
    int getUnreadCount(@Param("userId") Long userId);
} 