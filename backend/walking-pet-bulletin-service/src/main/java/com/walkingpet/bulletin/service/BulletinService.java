package com.walkingpet.bulletin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.walkingpet.bulletin.dto.BulletinAcceptDTO;
import com.walkingpet.bulletin.dto.BulletinCreateDTO;
import com.walkingpet.bulletin.entity.Bulletin;
import com.walkingpet.bulletin.mapper.BulletinMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class BulletinService {
    
    @Autowired
    private BulletinMapper bulletinMapper;
    
    @Autowired
    private com.walkingpet.bulletin.feign.OrderFeignClient orderFeignClient;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * 创建公告（订单由前端创建）
     */
    public Bulletin createBulletin(String userId, BulletinCreateDTO dto) {
        Bulletin bulletin = new Bulletin();
        bulletin.setBulletinId("b-" + System.currentTimeMillis());
        bulletin.setUserId(userId);
        bulletin.setServiceType(dto.getServiceType());
        bulletin.setTitle(dto.getTitle());
        bulletin.setStatus("待接单");
        bulletin.setAddress(dto.getAddress());
        bulletin.setPetId(dto.getPetId());
        bulletin.setPetName(dto.getPetName());
        bulletin.setPetType(dto.getPetType());
        
        // 解析服务时间
        if (StringUtils.hasText(dto.getServiceTime())) {
            bulletin.setServiceTime(LocalDateTime.parse(dto.getServiceTime(), formatter));
        }
        
        bulletin.setWalkerGender(dto.getWalkerGender());
        bulletin.setRemark(dto.getRemark());
        bulletin.setDistance("1.2km"); // 模拟距离
        bulletin.setCreatedAt(LocalDateTime.now());
        bulletin.setUpdatedAt(LocalDateTime.now());
        
        bulletinMapper.insert(bulletin);
        log.info("公告创建成功: bulletinId={}", bulletin.getBulletinId());
        
        return bulletin;
    }
    
    /**
     * 获取公告列表（分页）
     * 默认只返回待接单的公告，个人中心查看自己的公告时可以看到所有状态
     */
    public IPage<Bulletin> getBulletinList(Integer page, Integer pageSize, String serviceType, String userId, Boolean mine) {
        Page<Bulletin> pageParam = new Page<>(page, pageSize);
        
        LambdaQueryWrapper<Bulletin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(serviceType)) {
            wrapper.eq(Bulletin::getServiceType, serviceType);
        }
        if (Boolean.TRUE.equals(mine) && StringUtils.hasText(userId)) {
            // 查看我的公告时，显示所有状态
            wrapper.eq(Bulletin::getUserId, userId);
        } else {
            // 公共公告列表，只显示待接单的
            wrapper.eq(Bulletin::getStatus, "待接单");
        }
        wrapper.orderByDesc(Bulletin::getCreatedAt);
        
        return bulletinMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 获取公告详情
     */
    public Bulletin getBulletinById(String id) {
        LambdaQueryWrapper<Bulletin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bulletin::getBulletinId, id);
        return bulletinMapper.selectOne(wrapper);
    }
    
    /**
     * 接单（同时更新对应的订单状态和宠托师信息）
     */
    public Bulletin acceptBulletin(String id, BulletinAcceptDTO dto, String userId) {
        Bulletin bulletin = getBulletinById(id);
        if (bulletin == null) {
            throw new RuntimeException("公告不存在");
        }
        
        if (!"待接单".equals(bulletin.getStatus())) {
            throw new RuntimeException("该公告已被接单");
        }

        if (StringUtils.hasText(userId) && userId.equals(bulletin.getUserId())) {
            throw new RuntimeException("不能接自己发布的公告");
        }
        
        bulletin.setSitterId(dto.getSitterId());
        bulletin.setStatus("已接单");
        bulletin.setUpdatedAt(LocalDateTime.now());
        
        bulletinMapper.updateById(bulletin);
        
        // 同时通过bulletinId更新订单状态和宠托师信息
        try {
            java.util.Map<String, String> statusData = new java.util.HashMap<>();
            statusData.put("bulletinId", bulletin.getBulletinId());
            statusData.put("status", "已接单");
            statusData.put("sitterId", userId); // 宠托师的userId
            
            orderFeignClient.updateOrderStatus(bulletin.getBulletinId(), statusData);
            log.info("公告接单成功，同时更新订单: bulletinId={}, sitterUserId={}", bulletin.getBulletinId(), userId);
        } catch (Exception e) {
            log.error("更新订单失败，但公告状态已更新: bulletinId={}", bulletin.getBulletinId(), e);
            // 不影响接单流程，只记录日志
        }
        
        return bulletin;
    }
    
    /**
     * 删除公告
     */
    public void deleteBulletin(String id, String userId) {
        LambdaQueryWrapper<Bulletin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bulletin::getBulletinId, id)
               .eq(Bulletin::getUserId, userId);
        
        Bulletin bulletin = bulletinMapper.selectOne(wrapper);
        if (bulletin == null) {
            throw new RuntimeException("公告不存在或无权删除");
        }
        
        bulletinMapper.deleteById(bulletin);
    }
}
