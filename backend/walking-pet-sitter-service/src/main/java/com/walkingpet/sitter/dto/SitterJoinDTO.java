package com.walkingpet.sitter.dto;

import lombok.Data;

import java.util.List;

@Data
public class SitterJoinDTO {
    private String name;
    private String gender;
    private String slogan;
    private List<String> tags;
    private List<PetDTO> pets;
    private List<ServiceDTO> services;
    
    @Data
    public static class PetDTO {
        private String name;
        private String desc;
        private String cover;
    }
    
    @Data
    public static class ServiceDTO {
        private String type;
        private String title;
        private String price;
        private String duration;
    }
}
