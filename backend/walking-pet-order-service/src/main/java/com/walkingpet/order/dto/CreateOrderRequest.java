package com.walkingpet.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建订单请求
 */
@Data
public class CreateOrderRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sitterId;
    private String serviceType;
    private String serviceTime;
    private String address;
    private String petId;
    private String walkerGender;
    private String remark;
    private String status; // 可选，用于区分主人下单和宠托师接单
}
