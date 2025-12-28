package com.walkingpet.pet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.walkingpet.common.core.exception.BusinessException;
import com.walkingpet.pet.dto.PetRequest;
import com.walkingpet.pet.entity.Pet;
import com.walkingpet.pet.mapper.PetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宠物服务
 */
@Slf4j
@Service
public class PetService {

    @Autowired
    private PetMapper petMapper;

    /**
     * 创建宠物档案
     */
    public Pet createPet(String userId, PetRequest request) {
        if (!StringUtils.hasText(request.getName()) || !StringUtils.hasText(request.getType())) {
            throw new BusinessException("宠物名称和类型不能为空");
        }
        
        log.info("创建宠物档案 - userId: {}, name: {}, 图片数据长度: {}", 
                userId, request.getName(), 
                request.getImage() != null ? request.getImage().length() : 0);

        Pet pet = new Pet();
        BeanUtils.copyProperties(request, pet);
        pet.setPetId("pet-" + System.currentTimeMillis());
        pet.setUserId(userId);
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());

        petMapper.insert(pet);
        log.info("宠物档案创建成功: {}", pet.getPetId());
        return pet;
    }

    /**
     * 获取用户的宠物列表
     */
    public List<Pet> getUserPets(String userId) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pet::getUserId, userId);
        wrapper.orderByDesc(Pet::getCreatedAt);
        return petMapper.selectList(wrapper);
    }

    /**
     * 获取宠物详情
     */
    public Pet getPetById(String petId) {
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        return pet;
    }

    /**
     * 更新宠物档案
     */
    public Pet updatePet(String petId, String userId, PetRequest request) {
        if (request == null) {
            throw new BusinessException("请求参数不能为空");
        }
        
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作");
        }

        BeanUtils.copyProperties(request, pet);
        pet.setUpdatedAt(LocalDateTime.now());
        petMapper.updateById(pet);
        return pet;
    }

    /**
     * 删除宠物档案
     */
    public void deletePet(String petId, String userId) {
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作");
        }

        petMapper.deleteById(petId);
    }
}
