package com.walkingpet.pet.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 宠物请求
 */
@Data
public class PetRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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
}
