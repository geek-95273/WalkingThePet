package com.walkingpet.sitter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sitter_tags")
public class SitterTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String sitterId;
    
    private String tag;
}
