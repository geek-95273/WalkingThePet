package com.walkingpet.order.feign;

import com.walkingpet.common.core.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "sitter-service", path = "/api/sitters", url = "http://localhost:8004")
public interface SitterFeignClient {
    
    /**
     * 获取用户的宠托师信息
     */
    @GetMapping("/my-info")
    Result<Map<String, Object>> getMySitterInfo(@RequestHeader("X-User-Id") String userId);
}
