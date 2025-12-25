package com.logistics.entity;

import java.time.LocalDateTime;

/**
 * 反馈实体类
 */
public class Feedback {
    private Long id;
    private Long userId;           // 反馈用户ID
    private String type;           // 反馈类型：bug-问题反馈，suggestion-建议，complaint-投诉
    private String title;          // 反馈标题
    private String content;        // 反馈内容
    private String status;         // 状态：pending-待处理，processing-处理中，resolved-已解决
    private String reply;          // 回复内容
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // 构造函数
    public Feedback() {}

    public Feedback(Long userId, String type, String title, String content) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = "pending";
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", reply='" + reply + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 