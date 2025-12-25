package com.logistics.service;

import com.logistics.entity.User;
import com.logistics.mapper.UserMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.nio.charset.StandardCharsets;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User findById(Long id) {
        return userMapper.findById(id);
    }
    
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    public List<User> findAllActive() {
        return userMapper.findAllActive();
    }
    
    public PageResult<User> findByPage(PageRequest pageRequest) {
        List<User> users = userMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
        int total = userMapper.count();
        return PageResult.of(users, total, pageRequest.getPageNum(), pageRequest.getPageSize());
    }
    
    public boolean save(User user) {
        if (user.getId() == null) {
            // 新增用户，密码加密
            // 密码已在客户端加密，直接存储
            user.setPassword(user.getPassword());
            user.setStatus("active");
            return userMapper.insert(user) > 0;
        } else {
            // 更新用户
            return userMapper.update(user) > 0;
        }
    }
    
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.findById(id);
        if (user == null) {
            return false;
        }
        
        String oldPasswordMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes(StandardCharsets.UTF_8));
        if (!oldPasswordMd5.equals(user.getPassword())) {
            return false;
        }
        
        String newPasswordMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
        return userMapper.updatePassword(id, newPasswordMd5) > 0;
    }
    
    public boolean delete(Long id) {
        return userMapper.delete(id) > 0;
    }
    
    /**
     * 用户登录
     */
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !"active".equals(user.getStatus())) {
            return null;
        }
        
        // 直接比较前端传来的MD5密码和数据库存储的MD5密码
        if (password.equals(user.getPassword())) {
            // 清除密码信息
            user.setPassword(null);
            return user;
        }
        
        return null;
    }
    
    /**
     * 用户注册
     */
    public User register(User user) {
        try {
            // 设置默认值
            user.setStatus("active");
            user.setRole("user");
            
            // 密码加密
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
            
            // 保存用户
            if (userMapper.insert(user) > 0) {
                return user;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 检查用户名是否已存在
     */
    public boolean existsByUsername(String username) {
        try {
            return userMapper.findByUsername(username) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查邮箱是否已存在
     */
    public boolean existsByEmail(String email) {
        try {
            return userMapper.findByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}