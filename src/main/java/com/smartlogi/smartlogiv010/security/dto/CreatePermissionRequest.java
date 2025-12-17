package com.smartlogi.smartlogiv010.security.dto;


import lombok.Data;

@Data
public class CreatePermissionRequest {
    private String name;
    private String description;
}