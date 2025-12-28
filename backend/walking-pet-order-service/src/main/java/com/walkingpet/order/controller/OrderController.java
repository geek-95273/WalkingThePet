package com.walkingpet.order.controller;

import com.walkingpet.common.core.domain.Result;
import com.walkingpet.order.dto.CompleteOrderRequest;
import com.walkingpet.order.dto.CreateOrderRequest;
import com.walkingpet.order.entity.Order;
import com.walkingpet.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单（主人下单）
     */
    @PostMapping
    public Result<Order> createOrder(@RequestHeader("X-User-Id") String userId,
                                      @RequestBody CreateOrderRequest request) {
        log.info("======== 创建订单请求 ========");
        log.info("用户ID: {}", userId);
        log.info("公告ID: {}", request.getBulletinId());
        log.info("服务类型: {}", request.getServiceType());
        log.info("服务时间: {}", request.getServiceTime());
        log.info("地址: {}", request.getAddress());
        log.info("宠物: {} - {}", request.getPetName(), request.getPetType());
        log.info("状态: {}", request.getStatus());
        // 如果有bulletinId，说明是从公告创建，传递给service
        Order order = orderService.createOrder(userId, request, request.getBulletinId());
        log.info("订单创建成功: {}", order.getOrderId());
        return Result.success(order, "创建成功");
    }

    /**
     * 获取订单列表
     */
    @GetMapping
    public Result<List<Order>> getOrders(@RequestHeader("X-User-Id") String userId) {
        log.info("获取订单列表，用户: {}", userId);
        List<Order> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable String id,
                                   @RequestHeader("X-User-Id") String userId) {
        log.info("获取订单详情: {}", id);
        Order order = orderService.getOrderById(id, userId);
        return Result.success(order);
    }

    /**
     * 宠托师接单
     */
    @PostMapping("/{id}/accept")
    public Result<Order> acceptOrder(@PathVariable String id,
                                      @RequestHeader("X-User-Id") String userId) {
        log.info("宠托师接单: orderId={}, userId={}", id, userId);
        Order order = orderService.acceptOrder(id, userId);
        return Result.success(order, "接单成功");
    }

    /**
     * 完成订单
     */
    @PostMapping("/{id}/complete")
    public Result<Order> completeOrder(@PathVariable String id,
                                        @RequestBody CompleteOrderRequest request,
                                        @RequestHeader("X-User-Id") String userId) {
        log.info("完成订单: {}", id);
        request.setOrderId(id);
        Order order = orderService.completeOrder(request, userId);
        return Result.success(order, "订单已完成");
    }

    /**
     * 从公告创建订单（内部接口，由Bulletin Service调用）
     */
    @PostMapping("/bulletin/{bulletinId}")
    public Result<Order> createOrderFromBulletin(@PathVariable String bulletinId,
                                                   @RequestBody CreateOrderRequest request,
                                                   @RequestHeader("X-User-Id") String userId) {
        log.info("从公告创建订单: bulletinId={}, userId={}", bulletinId, userId);
        request.setStatus("已接单"); // 宠托师接单时直接设为已接单
        request.setSitterId(userId); // 将接单宠托师绑定为当前登录用户
        Order order = orderService.createOrder(userId, request, bulletinId);
        return Result.success(order, "订单创建成功");
    }
    
    /**
     * 通过bulletinId更新订单状态（内部接口，由Bulletin Service调用）
     */
    @PutMapping("/{bulletinId}/status")
    public Result<Order> updateOrderStatus(@PathVariable String bulletinId,
                                           @RequestBody java.util.Map<String, String> statusData) {
        log.info("更新订单状态: bulletinId={}, statusData={}", bulletinId, statusData);
        String status = statusData.get("status");
        String sitterId = statusData.get("sitterId");
        Order order = orderService.updateOrderByBulletinId(bulletinId, status, sitterId);
        return Result.success(order, "订单状态更新成功");
    }}