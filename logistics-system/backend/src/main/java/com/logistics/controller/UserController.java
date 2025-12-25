package com.logistics.controller;

import com.logistics.entity.User;
import com.logistics.service.UserService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import com.logistics.common.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        logger.info("Login attempt - Username: {}", username);
        logger.info("Login attempt - Received password hash: {}", password);

        User user = userService.login(username, password);
        
        // 调试日志：验证服务层返回的用户对象
        if (user != null) {
            logger.info("用户登录成功: {}", user.getUsername());
        } else {
            logger.warn("用户登录失败: 用户名或密码不正确");
        }
        if (user != null) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", user);
            
            return Result.success("登录成功", responseData);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(user.getUsername())) {
            return Result.error("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
            return Result.error("邮箱已被使用");
        }
        
        // 创建新用户
        User newUser = userService.register(user);
        if (newUser != null) {
            newUser.setPassword(null); // 不返回密码
            return Result.success("注册成功", newUser);
        } else {
            return Result.error("注册失败");
        }
    }
    
    /**
     * 获取所有活跃用户
     */
    @GetMapping("/active")
    public Result<List<User>> getAllActive() {
        List<User> users = userService.findAllActive();
        return Result.success(users);
    }
    
    /**
     * 分页查询用户
     */
    @PostMapping("/page")
    public Result<PageResult<User>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<User> result = userService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }
    
    /**
     * 保存用户（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody User user) {
        boolean success = userService.save(user);
        if (success) {
            return Result.success(user.getId() == null ? "用户创建成功" : "用户更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/{id}/password")
    public Result<String> updatePassword(@PathVariable Long id, 
                                       @RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        
        boolean success = userService.updatePassword(id, oldPassword, newPassword);
        if (success) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("原密码错误");
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = userService.delete(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}