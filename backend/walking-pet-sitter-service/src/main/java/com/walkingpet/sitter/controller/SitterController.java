package com.walkingpet.sitter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.walkingpet.common.core.domain.Result;
import com.walkingpet.sitter.dto.SitterJoinDTO;
import com.walkingpet.sitter.entity.Sitter;
import com.walkingpet.sitter.entity.SitterPet;
import com.walkingpet.sitter.entity.SitterService;
import com.walkingpet.sitter.service.SitterBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/sitters")
public class SitterController {
    
    @Autowired
    private SitterBusinessService sitterBusinessService;
    
    /**
     * 入驻宠托师
     */
    @PostMapping("/join")
    public Result<Map<String, Object>> joinSitter(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "u-1000") String userId,
            @RequestBody SitterJoinDTO dto) {
        try {
            Sitter sitter = sitterBusinessService.joinSitter(userId, dto);
            
            Map<String, Object> result = new HashMap<>();
            result.put("sitter_id", sitter.getSitterId());
            result.put("status", "ready");
            
            return Result.success(result, "入驻成功");
        } catch (Exception e) {
            log.error("入驻宠托师失败", e);
            return Result.fail("入驻失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取宠托师列表
     */
    @GetMapping
    public Result<Map<String, Object>> getSitterList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "service_type", required = false) String serviceType,
            @RequestParam(required = false) String gender) {
        try {
            IPage<Sitter> pageResult = sitterBusinessService.getSitterList(page, pageSize, serviceType, gender);
            
            List<Map<String, Object>> list = pageResult.getRecords().stream().map(sitter -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", sitter.getSitterId());
                item.put("name", sitter.getName());
                item.put("gender", sitter.getGender());
                item.put("distance", sitter.getDistance());
                item.put("rating", sitter.getRating());
                item.put("orders", sitter.getOrders());
                
                // 获取标签
                List<String> tags = sitterBusinessService.getSitterTags(sitter.getSitterId());
                item.put("tags", tags);
                
                return item;
            }).collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("page", pageResult.getCurrent());
            result.put("page_size", pageResult.getSize());
            result.put("total", pageResult.getTotal());
            
            return Result.success(result, "获取成功");
        } catch (Exception e) {
            log.error("获取宠托师列表失败", e);
            return Result.fail("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取宠托师详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getSitterDetail(@PathVariable String id) {
        try {
            Sitter sitter = sitterBusinessService.getSitterById(id);
            if (sitter == null) {
                return Result.fail("宠托师不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", sitter.getSitterId());
            result.put("name", sitter.getName());
            result.put("gender", sitter.getGender());
            result.put("distance", sitter.getDistance());
            result.put("rating", sitter.getRating());
            result.put("orders", sitter.getOrders());
            result.put("slogan", sitter.getSlogan());
            
            // 获取标签
            List<String> tags = sitterBusinessService.getSitterTags(sitter.getSitterId());
            result.put("tags", tags);
            
            // 获取宠物展示
            List<SitterPet> pets = sitterBusinessService.getSitterPets(sitter.getSitterId());
            List<Map<String, Object>> petList = pets.stream().map(pet -> {
                Map<String, Object> petMap = new HashMap<>();
                petMap.put("name", pet.getName());
                petMap.put("desc", pet.getDescription());
                petMap.put("cover", pet.getCover());
                return petMap;
            }).collect(Collectors.toList());
            result.put("pets", petList);
            
            // 获取服务列表
            List<SitterService> services = sitterBusinessService.getSitterServices(sitter.getSitterId());
            List<Map<String, Object>> serviceList = services.stream().map(service -> {
                Map<String, Object> serviceMap = new HashMap<>();
                serviceMap.put("type", service.getServiceType());
                serviceMap.put("title", service.getTitle());
                serviceMap.put("price", service.getPrice());
                serviceMap.put("duration", service.getDuration());
                return serviceMap;
            }).collect(Collectors.toList());
            result.put("services", serviceList);
            
            return Result.success(result, "获取成功");
        } catch (Exception e) {
            log.error("获取宠托师详情失败", e);
            return Result.fail("获取失败：" + e.getMessage());
        }
    }
}
