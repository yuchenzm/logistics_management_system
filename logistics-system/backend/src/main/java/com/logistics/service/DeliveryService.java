package com.logistics.service;

import com.logistics.entity.Delivery;
import com.logistics.mapper.DeliveryMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeliveryService {
    
    @Autowired
    private DeliveryMapper deliveryMapper;
    
    public List<Delivery> findAll() {
        return deliveryMapper.findAll();
    }
    
    /**
     * 分页查询配送
     */
    public PageResult<Delivery> findByPage(PageRequest pageRequest) {
        List<Delivery> deliveries = deliveryMapper.findAll();
        return new PageResult<>(deliveries, deliveries.size(), 1, deliveries.size());
    }
    
    public Delivery findById(Integer id) {
        return deliveryMapper.findById(id);
    }
    
    public Delivery findById(Long id) {
        return deliveryMapper.findById(id.intValue());
    }
    
    public List<Delivery> findByTransportId(Long transportId) {
        return deliveryMapper.findByTransportId(transportId.intValue());
    }
    
    /**
     * 根据配送员查询配送
     */
    public List<Delivery> findByDeliverer(String deliverer) {
        return deliveryMapper.findByDeliverer(deliverer);
    }
    
    public List<Delivery> findByStatus(String status) {
        return deliveryMapper.findByStatus(status);
    }
    
    public List<Delivery> findByRecipient(String recipient) {
        return deliveryMapper.findByRecipient(recipient);
    }
    
    /**
     * 根据运输编号查询配送
     */
    public List<Delivery> findByTransportNumber(String transportNumber) {
        return deliveryMapper.findByTransportNumber(transportNumber);
    }
    
    public List<Delivery> searchByKeyword(String keyword) {
        return deliveryMapper.searchByKeyword(keyword);
    }
    
    public boolean save(Delivery delivery) {
        try {
            if (delivery.getId() == null) {
                // 新增
                if (delivery.getStatus() == null) {
                    delivery.setStatus("pending");
                }
                deliveryMapper.insert(delivery);
            } else {
                // 更新
                deliveryMapper.update(delivery);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateStatus(Integer id, String status) {
        return deliveryMapper.updateStatus(id, status) > 0;
    }
    
    /**
     * 更新配送状态（通过Delivery对象）
     */
    public boolean updateStatus(Delivery delivery) {
        try {
            return deliveryMapper.updateStatus(delivery.getId(), delivery.getStatus()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 签收确认
     */
    public boolean signForDelivery(Delivery delivery) {
        try {
            delivery.setStatus("delivered");
            return deliveryMapper.confirmDelivery(delivery.getId(), delivery.getRecipient(), delivery.getSignatureRequired()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean deleteById(Integer id) {
        return deliveryMapper.deleteById(id) > 0;
    }
    
    /**
     * 删除配送（支持Long类型）
     */
    public boolean delete(Long id) {
        try {
            return deliveryMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int count() {
        return deliveryMapper.count();
    }
    
    public int countByStatus(String status) {
        return deliveryMapper.countByStatus(status);
    }
    
    public List<Delivery> findPendingDeliveries() {
        return deliveryMapper.findByStatus("pending");
    }
    
    public List<Delivery> findInTransitDeliveries() {
        return deliveryMapper.findByStatus("in_transit");
    }
    
    public List<Delivery> findDeliveredDeliveries() {
        return deliveryMapper.findByStatus("delivered");
    }
    
    public List<Delivery> findFailedDeliveries() {
        return deliveryMapper.findByStatus("failed");
    }
    
    public boolean retryFailedDelivery(Integer id) {
        return deliveryMapper.updateStatus(id, "pending") > 0;
    }
} 