package com.logistics.service;

import com.logistics.entity.Transport;
import com.logistics.mapper.TransportMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 运输服务类
 */
@Service
public class TransportService {
    
    @Autowired
    private TransportMapper transportMapper;
    
    public Transport findById(Long id) {
        return transportMapper.findById(id);
    }
    
    public Transport findByTransportNumber(String transportNumber) {
        return transportMapper.findByTransportNumber(transportNumber);
    }
    
    public List<Transport> findByStatus(String status) {
        return transportMapper.findByStatus(status);
    }
    
    public List<Transport> searchByKeyword(String keyword) {
        return transportMapper.searchByKeyword(keyword);
    }
    
    public PageResult<Transport> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasSearchConditions = 
            StringUtils.hasText(pageRequest.getKeyword()) ||
            StringUtils.hasText(pageRequest.getStatus()) ||
            StringUtils.hasText(pageRequest.getTransportStatus()) ||
            StringUtils.hasText(pageRequest.getTransportType());
        
        if (hasSearchConditions) {
            // 使用多条件搜索
            List<Transport> transports = transportMapper.findByPageWithConditions(pageRequest);
            int total = transportMapper.countWithConditions(pageRequest);
            return PageResult.of(transports, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        } else {
            // 使用普通分页查询
            List<Transport> transports = transportMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
            int total = transportMapper.count();
            return PageResult.of(transports, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        }
    }
    
    public boolean save(Transport transport) {
        if (transport.getId() == null) {
            // 新增运输
            if (transport.getTransportStatus() == null) {
                transport.setTransportStatus("planned");
            }
            if (transport.getTotalCost() == null) {
                transport.setTotalCost(java.math.BigDecimal.ZERO);
            }
            return transportMapper.insert(transport) > 0;
        } else {
            // 更新运输
            return transportMapper.update(transport) > 0;
        }
    }
    
    public boolean updateStatus(Long id, String status) {
        Transport transport = transportMapper.findById(id);
        if (transport != null) {
            transport.setTransportStatus(status);
            return transportMapper.update(transport) > 0;
        }
        return false;
    }
    
    public boolean delete(Long id) {
        return transportMapper.delete(id) > 0;
    }
    
    /**
     * 运输统计信息
     */
    public static class TransportStatistics {
        private long totalTransports;
        private long plannedTransports;
        private long inProgressTransports;
        private long completedTransports;
        private double totalRevenue;
        private double averageDistance;
        
        // getter和setter省略...
        public long getTotalTransports() { return totalTransports; }
        public void setTotalTransports(long totalTransports) { this.totalTransports = totalTransports; }
        public long getPlannedTransports() { return plannedTransports; }
        public void setPlannedTransports(long plannedTransports) { this.plannedTransports = plannedTransports; }
        public long getInProgressTransports() { return inProgressTransports; }
        public void setInProgressTransports(long inProgressTransports) { this.inProgressTransports = inProgressTransports; }
        public long getCompletedTransports() { return completedTransports; }
        public void setCompletedTransports(long completedTransports) { this.completedTransports = completedTransports; }
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
        public double getAverageDistance() { return averageDistance; }
        public void setAverageDistance(double averageDistance) { this.averageDistance = averageDistance; }
    }
} 