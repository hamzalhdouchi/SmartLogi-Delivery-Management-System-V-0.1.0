package com.smartlogi.security.dto.Permission.request;


import lombok.Data;

@Data
public class CreatePermissionRequest {
    private String name;
    private String description;
}