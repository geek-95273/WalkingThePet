package com.walkingpet.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册请求
 */
@Data
public class RegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String confirmPassword;
}
