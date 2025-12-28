package com.walkingpet.bulletin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.walkingpet.bulletin.dto.BulletinAcceptDTO;
import com.walkingpet.bulletin.dto.BulletinCreateDTO;
import com.walkingpet.bulletin.entity.Bulletin;
import com.walkingpet.bulletin.service.BulletinService;
import com.walkingpet.common.core.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/bulletins")
public class BulletinController {
    
    @Autowired
    private BulletinService bulletinService;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * 创建公告
     */
    @PostMapping
        public Result<Map<String, Object>> createBulletin(
            @RequestHeader(value = "X-User-Id") String userId,
            @RequestBody BulletinCreateDTO dto) {
        try {
            Bulletin bulletin = bulletinService.createBulletin(userId, dto);
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", bulletin.getBulletinId());
            result.put("service_type", bulletin.getServiceType());
            result.put("title", bulletin.getTitle());
            result.put("address", bulletin.getAddress());
            result.put("pet_id", bulletin.getPetId());
            result.put("pet_name", bulletin.getPetName());
            result.put("pet_type", bulletin.getPetType());
            result.put("service_time", bulletin.getServiceTime() != null ? 
                bulletin.getServiceTime().format(formatter) : null);
            result.put("walker_gender", bulletin.getWalkerGender());
            result.put("remark", bulletin.getRemark());
            
            return Result.success(result, "创建成功");
        } catch (Exception e) {
            log.error("创建公告失败", e);
            return Result.fail("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取公告列表
     */
    @GetMapping
    public Result<Map<String, Object>> getBulletinList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "service_type", required = false) String serviceType,
            @RequestParam(name = "mine", required = false, defaultValue = "false") Boolean mine,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        try {
            IPage<Bulletin> pageResult = bulletinService.getBulletinList(page, pageSize, serviceType, userId, mine);
            
            List<Map<String, Object>> list = pageResult.getRecords().stream().map(bulletin -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", bulletin.getBulletinId());
                item.put("title", bulletin.getTitle());
                item.put("status", bulletin.getStatus());
                item.put("service_type", bulletin.getServiceType());
                item.put("address", bulletin.getAddress());
                item.put("service_time", bulletin.getServiceTime() != null ? 
                    bulletin.getServiceTime().format(formatter) : null);
                item.put("walker_gender", bulletin.getWalkerGender());
                item.put("distance", bulletin.getDistance());
                return item;
            }).collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("page", pageResult.getCurrent());
            result.put("page_size", pageResult.getSize());
            result.put("total", pageResult.getTotal());
            
            return Result.success(result, "获取成功");
        } catch (Exception e) {
            log.error("获取公告列表失败", e);
            return Result.fail("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取公告详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getBulletinDetail(@PathVariable String id) {
        try {
            Bulletin bulletin = bulletinService.getBulletinById(id);
            if (bulletin == null) {
                return Result.fail("公告不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", bulletin.getBulletinId());
            result.put("title", bulletin.getTitle());
            result.put("status", bulletin.getStatus());
            result.put("service_type", bulletin.getServiceType());
            result.put("address", bulletin.getAddress());
            result.put("pet_id", bulletin.getPetId());
            result.put("pet_name", bulletin.getPetName());
            result.put("pet_type", bulletin.getPetType());
            result.put("service_time", bulletin.getServiceTime() != null ? 
                bulletin.getServiceTime().format(formatter) : null);
            result.put("walker_gender", bulletin.getWalkerGender());
            result.put("remark", bulletin.getRemark());
            result.put("distance", bulletin.getDistance());
            result.put("sitter_id", bulletin.getSitterId());
            
            return Result.success(result, "获取成功");
        } catch (Exception e) {
            log.error("获取公告详情失败", e);
            return Result.fail("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 接单
     */
    @PostMapping("/{id}/accept")
    public Result<Map<String, Object>> acceptBulletin(
            @PathVariable String id,
            @RequestBody BulletinAcceptDTO dto,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        try {
            Bulletin bulletin = bulletinService.acceptBulletin(id, dto, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", bulletin.getBulletinId());
            result.put("status", bulletin.getStatus());
            result.put("sitter_id", bulletin.getSitterId());
            
            return Result.success(result, "接单成功");
        } catch (Exception e) {
            log.error("接单失败", e);
            return Result.fail("接单失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
        public Result<Map<String, Object>> deleteBulletin(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Id") String userId) {
        try {
            bulletinService.deleteBulletin(id, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", id);
            result.put("deleted", true);
            
            return Result.success(result, "删除成功");
        } catch (Exception e) {
            log.error("删除公告失败", e);
            return Result.fail("删除失败：" + e.getMessage());
        }
    }
}
