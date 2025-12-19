package com.walkingpet.bulletin.dto;

import lombok.Data;

@Data
public class BulletinCreateDTO {
    private String serviceType;
    private String title;
    private String address;
    private String petId;
    private String petName;
    private String petType;
    private String serviceTime;
    private String walkerGender;
    private String remark;
}
