package com.walkingpet.bulletin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walkingpet.bulletin.entity.Bulletin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BulletinMapper extends BaseMapper<Bulletin> {
}
