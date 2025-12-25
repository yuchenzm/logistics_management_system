package com.logistics.controller;

import com.logistics.entity.Notification;
import com.logistics.service.NotificationService;
import com.logistics.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notifications")
@CrossOrigin
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * 根据用户ID获取通知列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Notification>> getByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        return Result.success(notifications);
    }

    /**
     * 分页查询用户通知
     */
    @PostMapping("/user/{userId}/page")
    public Result<PageResult<Notification>> getByUserIdAndPage(@PathVariable Long userId, @RequestBody PageRequest pageRequest) {
        PageResult<Notification> result = notificationService.findByUserIdAndPage(userId, pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据ID获取通知
     */
    @GetMapping("/{id}")
    public Result<Notification> getById(@PathVariable Long id) {
        Notification notification = notificationService.findById(id);
        if (notification != null) {
            return Result.success(notification);
        } else {
            return Result.error("通知不存在");
        }
    }
    
    /**
     * 创建通知
     */
    @PostMapping
    public Result<Notification> create(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.create(notification);
        if (createdNotification != null) {
            return Result.success("通知创建成功", createdNotification);
        } else {
            return Result.error("通知创建失败");
        }
    }
    
    /**
     * 标记为已读
     */
    @PutMapping("/{id}/read")
    public Result<String> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markAsRead(id);
        if (success) {
            return Result.success("标记已读成功");
        } else {
            return Result.error("标记已读失败");
        }
    }
    
    /**
     * 批量标记为已读
     */
    @PutMapping("/batch-read")
    public Result<String> batchMarkAsRead(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        boolean success = notificationService.batchMarkAsRead(ids);
        if (success) {
            return Result.success("批量标记已读成功");
        } else {
            return Result.error("批量标记已读失败");
        }
    }
    
    /**
     * 将用户所有通知标记为已读
     */
    @PutMapping("/user/{userId}/read-all")
    public Result<String> markAllAsReadForUser(@PathVariable Long userId) {
        boolean success = notificationService.markAllAsReadByUserId(userId);
        if (success) {
            return Result.success("全部标记已读成功");
        } else {
            return Result.error("全部标记已读失败");
        }
    }
    
    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = notificationService.delete(id);
        if (success) {
            return Result.success("通知删除成功");
        } else {
            return Result.error("通知删除失败");
        }
    }

    /**
     * 删除用户所有通知
     */
    @DeleteMapping("/user/{userId}/all")
    public Result<String> deleteAllForUser(@PathVariable Long userId) {
        boolean success = notificationService.deleteAllByUserId(userId);
        if (success) {
            return Result.success("全部删除成功");
        } else {
            return Result.error("全部删除失败");
        }
    }
    
    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count/{userId}")
    public Result<Integer> getUnreadCount(@PathVariable Long userId) {
        int count = notificationService.getUnreadCount(userId);
        return Result.success(count);
    }
} 