package com.logistics.service;

import com.logistics.entity.Address;
import com.logistics.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地址服务类
 */
@Service
public class AddressService {
    
    @Autowired
    private AddressMapper addressMapper;
    
    /**
     * 根据用户ID查找地址列表
     */
    public List<Address> findByUserId(Long userId) {
            return addressMapper.findByUserId(userId);
    }
    
    /**
     * 根据ID查找地址
     */
    public Address findById(Long id) {
            return addressMapper.findById(id);
    }
    
    /**
     * 保存地址
     */
    public Address save(Address address) {
            address.setCreateTime(LocalDateTime.now());
            address.setUpdateTime(LocalDateTime.now());
            
            // 如果设置为默认地址，先将其他地址设为非默认
            if (address.getIsDefault()) {
                clearDefaultAddress(address.getUserId());
            }
            
            addressMapper.save(address);
            return address;
    }
    
    /**
     * 更新地址
     */
    public Address update(Address address) {
            address.setUpdateTime(LocalDateTime.now());
            
            // 如果设置为默认地址，先将其他地址设为非默认
            if (address.getIsDefault()) {
                clearDefaultAddress(address.getUserId());
            }
            
            addressMapper.update(address);
            return address;
    }
    
    /**
     * 设置默认地址
     */
    public boolean setDefault(Long id) {
            Address address = findById(id);
            if (address != null) {
                clearDefaultAddress(address.getUserId());
                addressMapper.setDefault(id);
                return true;
            }
            return false;
    }
    
    /**
     * 删除地址
     */
    public boolean delete(Long id) {
            return addressMapper.delete(id) > 0;
    }
    
    /**
     * 清除用户的默认地址
     */
    private void clearDefaultAddress(Long userId) {
            addressMapper.clearDefaultAddress(userId);
    }
} 