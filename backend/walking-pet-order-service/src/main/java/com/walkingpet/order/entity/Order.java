package com.walkingpet.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("orders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    private String userId;

    private String sitterId;

    private String sitterName;

    private String bulletinId;

    private String status;

    private String serviceType;

    private LocalDateTime serviceTime;

    private String address;

    private String petId;

    private String walkerGender;

    private String remark;

    private String completeContent;

    private String completeImage;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
