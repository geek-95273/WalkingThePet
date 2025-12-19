package com.walkingpet.sitter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walkingpet.sitter.entity.Sitter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SitterMapper extends BaseMapper<Sitter> {
}
