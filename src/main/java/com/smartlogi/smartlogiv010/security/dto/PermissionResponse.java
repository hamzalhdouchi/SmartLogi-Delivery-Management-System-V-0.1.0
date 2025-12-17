package com.smartlogi.smartlogiv010.security.dto;


import com.smartlogi.smartlogiv010.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private String id;
    private String name;
    private String description;

}

