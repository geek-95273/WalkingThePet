package com.walkingpet.bulletin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bulletins")
public class Bulletin {
    @TableId(type = IdType.ASSIGN_ID)
    private String bulletinId;
    
    private String userId;
    
    private String serviceType;
    
    private String title;
    
    private String status;
    
    private String address;
    
    private String petId;
    
    private String petName;
    
    private String petType;
    
    private LocalDateTime serviceTime;
    
    private String walkerGender;
    
    private String remark;
    
    private String sitterId;
    
    private String distance;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
