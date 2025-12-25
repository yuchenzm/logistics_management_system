package com.logistics.service;

import com.logistics.entity.Customer;
import com.logistics.mapper.CustomerMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 客户服务类
 */
@Service
public class CustomerService {
    
    @Autowired
    private CustomerMapper customerMapper;
    
    public Customer findById(Long id) {
        return customerMapper.findById(id);
    }
    
    public List<Customer> findAllActive() {
        return customerMapper.findAllActive();
    }
    
    public List<Customer> searchByKeyword(String keyword) {
        return customerMapper.searchByKeyword(keyword);
    }
    
    public PageResult<Customer> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasSearchConditions = 
            StringUtils.hasText(pageRequest.getKeyword()) ||
            StringUtils.hasText(pageRequest.getCity()) ||
            StringUtils.hasText(pageRequest.getCreditRating());
        
        if (hasSearchConditions) {
            // 使用多条件搜索
            List<Customer> customers = customerMapper.findByPageWithConditions(pageRequest);
            int total = customerMapper.countWithConditions(pageRequest);
            return PageResult.of(customers, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        } else {
            // 使用普通分页查询
            List<Customer> customers = customerMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
            int total = customerMapper.count();
            return PageResult.of(customers, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        }
    }
    
    public boolean save(Customer customer) {
        if (customer.getId() == null) {
            // 新增客户
            if (customer.getStatus() == null) {
                customer.setStatus("active");
            }
            if (customer.getCustomerType() == null) {
                customer.setCustomerType("individual");
            }
            if (customer.getCreditRating() == null) {
                customer.setCreditRating("B");
            }
            return customerMapper.insert(customer) > 0;
        } else {
            // 更新客户
            return customerMapper.update(customer) > 0;
        }
    }
    
    public boolean delete(Long id) {
        return customerMapper.delete(id) > 0;
    }
} 