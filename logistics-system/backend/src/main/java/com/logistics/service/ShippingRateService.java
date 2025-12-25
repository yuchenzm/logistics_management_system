package com.logistics.service;

import com.logistics.entity.ShippingRate;
import com.logistics.mapper.ShippingRateMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;

@Service
public class ShippingRateService {
    
    @Autowired
    private ShippingRateMapper shippingRateMapper;
    
    public List<ShippingRate> findAll() {
        return shippingRateMapper.findAll();
    }
    
    /**
     * 分页查询运费率
     */
    public PageResult<ShippingRate> findByPage(PageRequest pageRequest) {
        List<ShippingRate> rates = shippingRateMapper.findAll();
        return new PageResult<>(rates, rates.size(), 1, rates.size());
    }
    
    /**
     * 根据出发城市查询运费率
     */
    public List<ShippingRate> findByOriginCity(String originCity) {
        return shippingRateMapper.findByOriginCity(originCity);
    }
    
    /**
     * 根据目的地城市查询运费率
     */
    public List<ShippingRate> findByDestinationCity(String destinationCity) {
        return shippingRateMapper.findByDestinationCity(destinationCity);
    }
    
    /**
     * 根据关键词搜索运费率
     */
    public List<ShippingRate> searchByKeyword(String keyword) {
        return shippingRateMapper.searchByKeyword(keyword);
    }
    
    public ShippingRate findById(Integer id) {
        return shippingRateMapper.findById(id);
    }
    
    public ShippingRate findById(Long id) {
        return shippingRateMapper.findById(id.intValue());
    }
    
    public List<ShippingRate> findByRoute(String originCity, String destinationCity) {
        return shippingRateMapper.findByRoute(originCity, destinationCity);
    }
    
    public List<ShippingRate> findByTransportType(String transportType) {
        return shippingRateMapper.findByTransportType(transportType);
    }
    
    public List<ShippingRate> findByStatus(String status) {
        return shippingRateMapper.findByStatus(status);
    }
    
    public List<ShippingRate> findActiveRates() {
        return shippingRateMapper.findActiveRates();
    }
    
    public List<ShippingRate> findApplicableRates(String originCity, String destinationCity, 
                                                String transportType, BigDecimal weight) {
        return shippingRateMapper.findApplicableRates(originCity, destinationCity, transportType, weight);
    }
    
    public boolean save(ShippingRate shippingRate) {
        try {
            if (shippingRate.getId() == null) {
                // 新增
                if (shippingRate.getStatus() == null) {
                    shippingRate.setStatus("active");
                }
                if (shippingRate.getFuelSurchargeRate() == null) {
                    shippingRate.setFuelSurchargeRate(BigDecimal.ZERO);
                }
                shippingRateMapper.insert(shippingRate);
            } else {
                // 更新
                shippingRateMapper.update(shippingRate);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 批量更新运费率
     */
    public boolean batchUpdate(List<ShippingRate> shippingRates) {
        try {
            for (ShippingRate rate : shippingRates) {
                shippingRateMapper.update(rate);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateStatus(Integer id, String status) {
        return shippingRateMapper.updateStatus(id, status) > 0;
    }
    
    public boolean deleteById(Integer id) {
        return shippingRateMapper.deleteById(id) > 0;
    }
    
    /**
     * 删除运费率（支持Long类型）
     */
    public boolean delete(Long id) {
        try {
            return shippingRateMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int count() {
        return shippingRateMapper.count();
    }
    
    public int countByStatus(String status) {
        return shippingRateMapper.countByStatus(status);
    }
    
    public List<String> findAllOriginCities() {
        return shippingRateMapper.findAllOriginCities();
    }
    
    public List<String> findAllDestinationCities() {
        return shippingRateMapper.findAllDestinationCities();
    }
    
    public BigDecimal calculateShippingCost(String originCity, String destinationCity, 
                                          String transportType, BigDecimal weight, BigDecimal distance) {
        List<ShippingRate> rates = findApplicableRates(originCity, destinationCity, transportType, weight);
        if (!rates.isEmpty()) {
            ShippingRate rate = rates.get(0); // 取第一个适用的费率
            return rate.calculateShippingCost(weight, distance);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 计算运费（通过请求对象）
     */
    public Double calculateShippingCost(ShippingRate calculateRequest) {
        try {
            BigDecimal weight = calculateRequest.getWeightRangeMin() != null ? 
                calculateRequest.getWeightRangeMin() : BigDecimal.valueOf(1.0);
            BigDecimal distance = BigDecimal.valueOf(100.0); // 默认距离
            
            BigDecimal cost = calculateShippingCost(
                calculateRequest.getOriginCity(),
                calculateRequest.getDestinationCity(),
                calculateRequest.getTransportType(),
                weight,
                distance
            );
            return cost.doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }
} 