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
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * 创建公告
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
        return bulletin;
    }
    
    /**
     * 获取公告列表（分页）
     */
    public IPage<Bulletin> getBulletinList(Integer page, Integer pageSize, String serviceType) {
        Page<Bulletin> pageParam = new Page<>(page, pageSize);
        
        LambdaQueryWrapper<Bulletin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(serviceType)) {
            wrapper.eq(Bulletin::getServiceType, serviceType);
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
     * 接单
     */
    public Bulletin acceptBulletin(String id, BulletinAcceptDTO dto) {
        Bulletin bulletin = getBulletinById(id);
        if (bulletin == null) {
            throw new RuntimeException("公告不存在");
        }
        
        if (!"待接单".equals(bulletin.getStatus())) {
            throw new RuntimeException("该公告已被接单");
        }
        
        bulletin.setSitterId(dto.getSitterId());
        bulletin.setStatus("已接单");
        bulletin.setUpdatedAt(LocalDateTime.now());
        
        bulletinMapper.updateById(bulletin);
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
