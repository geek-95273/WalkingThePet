package com.walkingpet.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.walkingpet.common.core.exception.BusinessException;
import com.walkingpet.common.security.utils.JwtUtil;
import com.walkingpet.common.security.utils.PasswordUtil;
import com.walkingpet.user.dto.LoginRequest;
import com.walkingpet.user.dto.RegisterRequest;
import com.walkingpet.user.dto.UserResponse;
import com.walkingpet.user.entity.User;
import com.walkingpet.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户服务
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    public UserResponse register(RegisterRequest request) {
        // 参数校验
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new BusinessException("用户名和密码不能为空");
        }
        
        if (request.getUsername().length() < 3) {
            throw new BusinessException("用户名至少3位");
        }

        if (request.getPassword().length() < 6) {
            throw new BusinessException("密码至少6位");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUserId("u-" + System.currentTimeMillis());
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.encrypt(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);

        // 返回结果
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        
        return response;
    }

    /**
     * 用户登录
     */
    public UserResponse login(LoginRequest request) {
        // 参数校验
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new BusinessException("用户名和密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.verify(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 生成Token
        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername());

        // 返回结果
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setToken(token);

        return response;
    }

    /**
     * 验证Token
     */
    public String validateToken(String token) {
        return JwtUtil.validateToken(token);
    }
}
