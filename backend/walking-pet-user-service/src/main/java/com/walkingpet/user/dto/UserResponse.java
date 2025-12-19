package com.walkingpet.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户响应
 */
@Data
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String token;
}
