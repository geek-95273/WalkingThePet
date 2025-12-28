package com.walkingpet.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.walkingpet.common.core.domain.Result;
import com.walkingpet.common.core.exception.BusinessException;
import com.walkingpet.order.dto.CompleteOrderRequest;
import com.walkingpet.order.dto.CreateOrderRequest;
import com.walkingpet.order.entity.Order;
import com.walkingpet.order.feign.SitterFeignClient;
import com.walkingpet.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private SitterFeignClient sitterFeignClient;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 创建订单
     */
    public Order createOrder(String userId, CreateOrderRequest request, String bulletinId) {
        log.info("创建订单请求 - userId: {}, sitterId: {}, serviceType: {}, serviceTime: {}, address: {}", 
                userId, request.getSitterId(), request.getServiceType(), 
                request.getServiceTime(), request.getAddress());
        
        // 如果有bulletinId，说明是从公告创建，这时sitterId可以为空
        boolean fromBulletin = StringUtils.hasText(bulletinId);
        
        if (!fromBulletin && !StringUtils.hasText(request.getSitterId())) {
            throw new BusinessException("宠托师信息不能为空");
        }
        
        if (!StringUtils.hasText(request.getServiceType()) ||
            !StringUtils.hasText(request.getServiceTime()) ||
            !StringUtils.hasText(request.getAddress())) {
            log.error("必填字段验证失败 - serviceType: {}, serviceTime: {}, address: {}", 
                    request.getServiceType(), 
                    request.getServiceTime(), request.getAddress());
            throw new BusinessException("必填信息不能为空");
        }

        Order order = new Order();
        order.setOrderId("o-" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setSitterId(request.getSitterId());
        order.setSitterName(request.getSitterName());
        order.setServiceType(request.getServiceType());
        order.setAddress(request.getAddress());
        order.setPetId(request.getPetId());
        order.setPetName(request.getPetName());
        order.setPetType(request.getPetType());
        order.setWalkerGender(request.getWalkerGender());
        order.setRemark(request.getRemark());
        order.setBulletinId(bulletinId);
        
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
     * 获取用户的订单列表（包括作为用户下的单和作为宠托师接的单）
     * 注意：这里查询userId等于当前用户的订单（我下的单）
     * 以及sitterId对应的宠托师userId等于当前用户的订单（别人给我下的单）
     */
    public List<Order> getUserOrders(String userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        // 查询：1. 我作为用户下的单（userId = 当前用户）
        //      2. 我接的单（sitterId = 当前用户，表示我作为宠托师接了单）
        // 注意：sitterId存储的是宠托师的userId而不是sitterId
        wrapper.and(w -> w.eq(Order::getUserId, userId).or().eq(Order::getSitterId, userId));
        wrapper.orderByDesc(Order::getCreatedAt);
        return orderMapper.selectList(wrapper);
    }

    /**
     * 获取订单详情
     */
    public Order getOrderById(String orderId, String userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (StringUtils.hasText(userId) && !userId.equals(order.getUserId())) {
            throw new BusinessException("无权限查看订单");
        }
        return order;
    }

    /**
     * 宠托师接单
     */
    public Order acceptOrder(String orderId, String sitterUserId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"待宠托师接单".equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }

        if (order.getUserId().equals(sitterUserId)) {
            throw new BusinessException("不能接自己的订单");
        }

        // 查询宠托师名字
        String sitterName = null;
        try {
            Result<Map<String, Object>> sitterInfo = sitterFeignClient.getMySitterInfo(sitterUserId);
            if (sitterInfo != null && sitterInfo.getSuccess() != null && sitterInfo.getSuccess() && sitterInfo.getBody() != null) {
                sitterName = (String) sitterInfo.getBody().get("name");
                log.info("查询到宠托师名字: {}", sitterName);
            }
        } catch (Exception e) {
            log.error("查询宠托师信息失败: userId={}", sitterUserId, e);
        }

        order.setStatus("已接单");
        order.setSitterId(sitterUserId); // 将接单人绑定为当前用户，便于权限校验
        if (sitterName != null) {
            order.setSitterName(sitterName); // 设置宠托师名字
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        
        log.info("宠托师接单成功: orderId={}, sitterUserId={}, sitterName={}", orderId, sitterUserId, sitterName);
        return order;
    }

    /**
     * 完成订单
     */
    public Order completeOrder(CompleteOrderRequest request, String userId) {
        Order order = orderMapper.selectById(request.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 仅允许已接单的宠托师账号完成订单
        if (!StringUtils.hasText(order.getSitterId()) || !order.getSitterId().equals(userId)) {
            throw new BusinessException("仅接单的宠托师可完成订单");
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
    
    /**
     * 通过bulletinId更新订单状态和sitterId（用于公告接单后同步更新）
     */
    public Order updateOrderByBulletinId(String bulletinId, String status, String sitterUserId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBulletinId, bulletinId);
        Order order = orderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (StringUtils.hasText(status)) {
            order.setStatus(status);
        }
        if (StringUtils.hasText(sitterUserId)) {
            order.setSitterId(sitterUserId);
        }
        order.setUpdatedAt(LocalDateTime.now());
        
        orderMapper.updateById(order);
        log.info("通过bulletinId更新订单: bulletinId={}, status={}, sitterId={}", bulletinId, status, sitterUserId);
        return order;
    }
}
