package com.walkingpet.pet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 宠物实体
 */
@Data
@TableName("pets")
public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String petId;

    private String userId;

    private String type;

    private String name;

    private String age;

    private String gender;

    private String weight;

    private String breed;

    private Boolean aggressive;

    private Boolean rabiesVaccine;

    private String intro;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
