package com.walkingpet.pet.controller;

import com.walkingpet.common.core.domain.Result;
import com.walkingpet.pet.dto.PetRequest;
import com.walkingpet.pet.entity.Pet;
import com.walkingpet.pet.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * 创建宠物档案
     */
    @PostMapping
    public Result<Pet> createPet(@RequestHeader("X-User-Id") String userId,
                                  @RequestBody PetRequest request) {
        log.info("创建宠物档案，用户: {}", userId);
        Pet pet = petService.createPet(userId, request);
        return Result.success(pet, "创建成功");
    }

    /**
     * 获取宠物列表
     */
    @GetMapping
    public Result<List<Pet>> getPets(@RequestHeader("X-User-Id") String userId) {
        log.info("获取宠物列表，用户: {}", userId);
        List<Pet> pets = petService.getUserPets(userId);
        return Result.success(pets);
    }

    /**
     * 获取宠物详情
     */
    @GetMapping("/{id}")
    public Result<Pet> getPet(@PathVariable String id) {
        log.info("获取宠物详情: {}", id);
        Pet pet = petService.getPetById(id);
        return Result.success(pet);
    }

    /**
     * 更新宠物档案
     */
    @PutMapping("/{id}")
    public Result<Pet> updatePet(@PathVariable String id,
                                  @RequestHeader("X-User-Id") String userId,
                                  @RequestBody PetRequest request) {
        log.info("更新宠物档案: {}", id);
        Pet pet = petService.updatePet(id, userId, request);
        return Result.success(pet, "更新成功");
    }

    /**
     * 删除宠物档案
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePet(@PathVariable String id,
                                   @RequestHeader("X-User-Id") String userId) {
        log.info("删除宠物档案: {}", id);
        petService.deletePet(id, userId);
        return Result.success(null, "删除成功");
    }

    /**
     * 根据用户ID获取宠物列表（内部接口）
     */
    @GetMapping("/user/{userId}")
    public Result<List<Pet>> getUserPets(@PathVariable String userId) {
        log.info("内部调用：获取用户宠物列表: {}", userId);
        List<Pet> pets = petService.getUserPets(userId);
        return Result.success(pets);
    }
}
