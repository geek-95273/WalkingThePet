package com.walkingpet.sitter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.walkingpet.sitter.dto.SitterJoinDTO;
import com.walkingpet.sitter.entity.Sitter;
import com.walkingpet.sitter.entity.SitterPet;
import com.walkingpet.sitter.entity.SitterService;
import com.walkingpet.sitter.entity.SitterTag;
import com.walkingpet.sitter.mapper.SitterMapper;
import com.walkingpet.sitter.mapper.SitterPetMapper;
import com.walkingpet.sitter.mapper.SitterServiceMapper;
import com.walkingpet.sitter.mapper.SitterTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SitterBusinessService {
    
    @Autowired
    private SitterMapper sitterMapper;
    
    @Autowired
    private SitterTagMapper sitterTagMapper;
    
    @Autowired
    private SitterPetMapper sitterPetMapper;
    
    @Autowired
    private SitterServiceMapper sitterServiceMapper;
    
    /**
     * 入驻宠托师
     */
    @Transactional(rollbackFor = Exception.class)
    public Sitter joinSitter(String userId, SitterJoinDTO dto) {
        // 创建宠托师基本信息
        Sitter sitter = new Sitter();
        sitter.setSitterId("s-" + System.currentTimeMillis());
        sitter.setUserId(userId);
        sitter.setName(dto.getName());
        sitter.setGender(dto.getGender());
        sitter.setSlogan(dto.getSlogan());
        sitter.setRating(new BigDecimal("5.0"));
        sitter.setOrders(0);
        sitter.setDistance("1.5km");
        sitter.setCreatedAt(LocalDateTime.now());
        sitter.setUpdatedAt(LocalDateTime.now());
        
        sitterMapper.insert(sitter);
        
        // 保存标签
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            for (String tag : dto.getTags()) {
                SitterTag sitterTag = new SitterTag();
                sitterTag.setSitterId(sitter.getSitterId());
                sitterTag.setTag(tag);
                sitterTagMapper.insert(sitterTag);
            }
        }
        
        // 保存宠物展示
        if (dto.getPets() != null && !dto.getPets().isEmpty()) {
            for (SitterJoinDTO.PetDTO petDTO : dto.getPets()) {
                SitterPet pet = new SitterPet();
                pet.setSitterId(sitter.getSitterId());
                pet.setName(petDTO.getName());
                pet.setDescription(petDTO.getDesc());
                pet.setCover(petDTO.getCover());
                sitterPetMapper.insert(pet);
            }
        }
        
        // 保存服务配置
        if (dto.getServices() != null && !dto.getServices().isEmpty()) {
            for (SitterJoinDTO.ServiceDTO serviceDTO : dto.getServices()) {
                SitterService service = new SitterService();
                service.setSitterId(sitter.getSitterId());
                service.setServiceType(serviceDTO.getType());
                service.setTitle(serviceDTO.getTitle());
                service.setPrice(serviceDTO.getPrice());
                service.setDuration(serviceDTO.getDuration());
                sitterServiceMapper.insert(service);
            }
        }
        
        return sitter;
    }
    
    /**
     * 获取宠托师列表
     */
    public IPage<Sitter> getSitterList(Integer page, Integer pageSize, String serviceType, String gender) {
        Page<Sitter> pageParam = new Page<>(page, pageSize);
        
        LambdaQueryWrapper<Sitter> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(gender)) {
            wrapper.eq(Sitter::getGender, gender);
        }
        wrapper.orderByDesc(Sitter::getCreatedAt);
        
        IPage<Sitter> result = sitterMapper.selectPage(pageParam, wrapper);
        
        // 如果需要按服务类型过滤，需要关联查询服务表
        if (StringUtils.hasText(serviceType)) {
            List<Sitter> filtered = result.getRecords().stream()
                .filter(sitter -> {
                    LambdaQueryWrapper<SitterService> serviceWrapper = new LambdaQueryWrapper<>();
                    serviceWrapper.eq(SitterService::getSitterId, sitter.getSitterId())
                                 .eq(SitterService::getServiceType, serviceType);
                    return sitterServiceMapper.selectCount(serviceWrapper) > 0;
                })
                .collect(Collectors.toList());
            result.setRecords(filtered);
        }
        
        return result;
    }
    
    /**
     * 获取宠托师详情
     */
    public Sitter getSitterById(String id) {
        LambdaQueryWrapper<Sitter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sitter::getSitterId, id);
        return sitterMapper.selectOne(wrapper);
    }
    
    /**
     * 获取宠托师标签
     */
    public List<String> getSitterTags(String sitterId) {
        LambdaQueryWrapper<SitterTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SitterTag::getSitterId, sitterId);
        return sitterTagMapper.selectList(wrapper).stream()
            .map(SitterTag::getTag)
            .collect(Collectors.toList());
    }
    
    /**
     * 获取宠托师宠物展示
     */
    public List<SitterPet> getSitterPets(String sitterId) {
        LambdaQueryWrapper<SitterPet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SitterPet::getSitterId, sitterId);
        return sitterPetMapper.selectList(wrapper);
    }
    
    /**
     * 获取宠托师服务列表
     */
    public List<SitterService> getSitterServices(String sitterId) {
        LambdaQueryWrapper<SitterService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SitterService::getSitterId, sitterId);
        return sitterServiceMapper.selectList(wrapper);
    }
}
