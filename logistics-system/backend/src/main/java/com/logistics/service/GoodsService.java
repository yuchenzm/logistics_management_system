package com.logistics.service;

import com.logistics.entity.Goods;
import com.logistics.mapper.GoodsMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsService {
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    public List<Goods> findAll() {
        return goodsMapper.findAll();
    }
    
    /**
     * 分页查询货物
     */
    public PageResult<Goods> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasConditions = (pageRequest.getKeyword() != null && !pageRequest.getKeyword().trim().isEmpty()) ||
                              (pageRequest.getGoodsCategory() != null && !pageRequest.getGoodsCategory().trim().isEmpty()) ||
                              (pageRequest.getGoodsStatus() != null && !pageRequest.getGoodsStatus().trim().isEmpty());
        
        if (hasConditions) {
            // 有搜索条件，使用多条件查询
            List<Goods> goods = goodsMapper.findByPageWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getGoodsCategory(),
                pageRequest.getGoodsStatus(),
                pageRequest.getOffset(),
                pageRequest.getPageSize()
            );
            
            int total = goodsMapper.countWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getGoodsCategory(),
                pageRequest.getGoodsStatus()
            );
            
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            return new PageResult<>(goods, total, pageRequest.getPageNum(), totalPages);
        } else {
            // 无搜索条件，查询所有数据
            List<Goods> allGoods = goodsMapper.findAll();
            int total = allGoods.size();
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            
            // 手动分页
            int start = pageRequest.getOffset();
            int end = Math.min(start + pageRequest.getPageSize(), total);
            List<Goods> goods = allGoods.subList(start, end);
            
            return new PageResult<>(goods, total, pageRequest.getPageNum(), totalPages);
        }
    }
    
    /**
     * 根据分类查询货物
     */
    public List<Goods> findByCategory(String category) {
        return goodsMapper.findByCategory(category);
    }
    
    /**
     * 根据状态查询货物
     */
    public List<Goods> findByStatus(String status) {
        return goodsMapper.findByStatus(status);
    }
    
    /**
     * 根据SKU查询货物
     */
    public Goods findBySku(String sku) {
        return goodsMapper.findBySku(sku);
    }
    
    public Goods findById(Long id) {
        return goodsMapper.findById(id.intValue());
    }
    
    public List<Goods> searchByKeyword(String keyword) {
        return goodsMapper.searchByKeyword(keyword);
    }
    
    public boolean save(Goods goods) {
        try {
            if (goods.getId() == null) {
                goodsMapper.insert(goods);
            } else {
                goodsMapper.update(goods);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 更新货物状态
     */
    public boolean updateStatus(Long id, String status) {
        try {
            return goodsMapper.updateStatus(id.intValue(), status) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 更新货物状态（通过Goods对象）
     */
    public boolean updateStatus(Goods goods) {
        try {
            return goodsMapper.updateStatus(goods.getId(), goods.getStatus()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 删除货物
     */
    public boolean delete(Long id) {
        try {
            return goodsMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 原有方法
    public List<Goods> findByGoodsCategory(String category) {
        return goodsMapper.findByCategory(category);
    }
    
    public List<Goods> findByGoodsStatus(String status) {
        return goodsMapper.findByStatus(status);
    }
    
    public List<Goods> findByWarehouseId(Integer warehouseId) {
        return goodsMapper.findByWarehouseId(warehouseId);
    }
    
    public List<Goods> findFragileGoods() {
        return goodsMapper.findFragileGoods();
    }
    
    public List<Goods> findDangerousGoods() {
        return goodsMapper.findDangerousGoods();
    }
    
    public List<Goods> findByTemperatureRequirement(String temperatureRequirement) {
        return goodsMapper.findByTemperatureRequirement(temperatureRequirement);
    }
    
    public List<Goods> findActiveGoods() {
        return goodsMapper.findByStatus("active");
    }
    
    public List<Goods> findInactiveGoods() {
        return goodsMapper.findByStatus("inactive");
    }
    
    public int count() {
        return goodsMapper.count();
    }
    
    public int countByCategory(String category) {
        return goodsMapper.countByCategory(category);
    }
    
    public int countByStatus(String status) {
        return goodsMapper.countByStatus(status);
    }
} 