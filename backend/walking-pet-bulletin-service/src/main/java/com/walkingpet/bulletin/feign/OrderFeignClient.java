package com.walkingpet.bulletin.feign;

import com.walkingpet.common.core.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "order-service", path = "/api/orders")
public interface OrderFeignClient {
    
    /**
     * 创建订单（用于公告创建时自动生成订单）
     */
    @PostMapping
    Result<Map<String, Object>> createOrder(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody Map<String, Object> orderData
    );
    
    /**
     * 更新订单状态（通过bulletinId）
     */
    @PutMapping("/{bulletinId}/status")
    Result<Map<String, Object>> updateOrderStatus(
            @PathVariable("bulletinId") String bulletinId,
            @RequestBody Map<String, String> statusData
    );
}
