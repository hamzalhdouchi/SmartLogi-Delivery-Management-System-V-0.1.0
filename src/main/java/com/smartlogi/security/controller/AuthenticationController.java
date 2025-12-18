package com.smartlogi.security.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.security.dto.authDto.response.JwtAuthResponse;
import com.smartlogi.security.dto.authDto.request.LoginRequest;
import com.smartlogi.security.dto.authDto.request.SignupRequest;
import com.smartlogi.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupRequest request) {
        User user = authService.signup(request);
        ApiResponse<User> response = ApiResponse.<User>builder()
                .success(true)
                .message("User récupérer avec succès")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }
}

