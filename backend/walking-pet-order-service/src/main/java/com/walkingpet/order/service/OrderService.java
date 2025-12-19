package com.walkingpet.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.walkingpet.common.core.exception.BusinessException;
import com.walkingpet.order.dto.CompleteOrderRequest;
import com.walkingpet.order.dto.CreateOrderRequest;
import com.walkingpet.order.entity.Order;
import com.walkingpet.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 创建订单
     */
    public Order createOrder(String userId, CreateOrderRequest request) {
        if (!StringUtils.hasText(request.getSitterId()) || 
            !StringUtils.hasText(request.getServiceType()) ||
            !StringUtils.hasText(request.getServiceTime()) ||
            !StringUtils.hasText(request.getAddress())) {
            throw new BusinessException("必填信息不能为空");
        }

        Order order = new Order();
        order.setOrderId("o-" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setSitterId(request.getSitterId());
        order.setServiceType(request.getServiceType());
        order.setAddress(request.getAddress());
        order.setPetId(request.getPetId());
        order.setWalkerGender(request.getWalkerGender());
        order.setRemark(request.getRemark());
        
        // 设置状态：如果指定了status则使用指定的，否则默认为"待宠托师接单"
        order.setStatus(StringUtils.hasText(request.getStatus()) ? 
                        request.getStatus() : "待宠托师接单");
        
        // 解析服务时间
        try {
            LocalDateTime serviceTime = LocalDateTime.parse(request.getServiceTime(), formatter);
            order.setServiceTime(serviceTime);
        } catch (Exception e) {
            throw new BusinessException("服务时间格式错误，应为：yyyy-MM-dd HH:mm");
        }

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderMapper.insert(order);
        log.info("创建订单成功: {}", order.getOrderId());
        return order;
    }

    /**
     * 获取用户的订单列表
     */
    public List<Order> getUserOrders(String userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreatedAt);
        return orderMapper.selectList(wrapper);
    }

    /**
     * 获取订单详情
     */
    public Order getOrderById(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    /**
     * 宠托师接单
     */
    public Order acceptOrder(String orderId, String sitterId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"待宠托师接单".equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus("已接单");
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        
        log.info("宠托师接单成功: orderId={}, sitterId={}", orderId, sitterId);
        return order;
    }

    /**
     * 完成订单
     */
    public Order completeOrder(CompleteOrderRequest request) {
        Order order = orderMapper.selectById(request.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (!"已接单".equals(order.getStatus())) {
            throw new BusinessException("只有已接单的订单才能完成");
        }

        if (!StringUtils.hasText(request.getCompleteContent())) {
            throw new BusinessException("完成内容不能为空");
        }

        if (!StringUtils.hasText(request.getCompleteImage())) {
            throw new BusinessException("完成图片不能为空");
        }

        order.setStatus("已完成");
        order.setCompleteContent(request.getCompleteContent());
        order.setCompleteImage(request.getCompleteImage());
        order.setCompletedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderMapper.updateById(order);
        log.info("订单完成: {}", request.getOrderId());
        return order;
    }
}
