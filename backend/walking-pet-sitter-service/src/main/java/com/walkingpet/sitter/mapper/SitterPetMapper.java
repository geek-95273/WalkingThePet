package com.walkingpet.sitter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walkingpet.sitter.entity.SitterPet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SitterPetMapper extends BaseMapper<SitterPet> {
}
