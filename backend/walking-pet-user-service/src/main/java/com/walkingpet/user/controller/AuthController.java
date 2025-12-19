package com.walkingpet.user.controller;

import com.walkingpet.common.core.domain.Result;
import com.walkingpet.user.dto.LoginRequest;
import com.walkingpet.user.dto.RegisterRequest;
import com.walkingpet.user.dto.UserResponse;
import com.walkingpet.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@RequestBody RegisterRequest request) {
        log.info("用户注册: {}", request.getUsername());
        UserResponse response = userService.register(request);
        return Result.success(response, "注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserResponse> login(@RequestBody LoginRequest request) {
        log.info("用户登录: {}", request.getUsername());
        UserResponse response = userService.login(request);
        return Result.success(response, "登录成功");
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        log.info("用户登出");
        return Result.success(null, "登出成功");
    }

    /**
     * 验证Token（内部接口，由Gateway调用）
     */
    @GetMapping("/validate")
    public Result<String> validateToken(@RequestParam String token) {
        String userId = userService.validateToken(token);
        if (userId == null) {
            return Result.fail("Token无效或已过期");
        }
        return Result.success(userId);
    }
}
