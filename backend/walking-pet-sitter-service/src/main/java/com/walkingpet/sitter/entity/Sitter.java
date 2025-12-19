package com.walkingpet.sitter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sitters")
public class Sitter {
    @TableId(type = IdType.ASSIGN_ID)
    private String sitterId;
    
    private String userId;
    
    private String name;
    
    private String gender;
    
    private String slogan;
    
    private BigDecimal rating;
    
    private Integer orders;
    
    private String distance;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
