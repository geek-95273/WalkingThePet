package com.walkingpet.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 完成订单请求
 */
@Data
public class CompleteOrderRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String completeContent;
    private String completeImage;
}
