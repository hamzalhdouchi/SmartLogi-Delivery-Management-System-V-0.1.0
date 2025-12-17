package com.smartlogi.smartlogiv010.security.controller;

import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.security.dto.JwtAuthResponse;
import com.smartlogi.smartlogiv010.security.dto.LoginRequest;
import com.smartlogi.smartlogiv010.security.dto.SignupRequest;
import com.smartlogi.smartlogiv010.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody SignupRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

