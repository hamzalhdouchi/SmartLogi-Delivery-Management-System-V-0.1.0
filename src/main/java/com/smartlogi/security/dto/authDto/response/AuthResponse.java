package com.smartlogi.security.dto.authDto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthResponse {

    private UserResponse userDetail;
    private String token;
}
