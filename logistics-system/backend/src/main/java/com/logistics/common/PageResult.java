package com.logistics.common;

import lombok.Data;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResult<T> {
    private List<T> records;    // 数据列表
    private long total;         // 总记录数
    private int pageNum;        // 当前页码
    private int pageSize;       // 每页大小
    private long totalPages;    // 总页数
    
    public PageResult() {}
    
    public PageResult(List<T> records, long total, int pageNum, int pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        // 防止除零错误
        this.totalPages = pageSize > 0 ? (total + pageSize - 1) / pageSize : 0;
    }
    
    public static <T> PageResult<T> of(List<T> records, long total, int pageNum, int pageSize) {
        return new PageResult<>(records, total, pageNum, pageSize);
    }
} 