package com.smartlogi.security.service;

import com.smartlogi.security.exception.DuplicateResourceException;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.security.exception.AuthenticationExceptionhandler;
import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
import com.smartlogi.smartlogiv010.repository.RoleRepository;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import com.smartlogi.security.config.JwtService;
import com.smartlogi.security.dto.authDto.response.JwtAuthResponse;
import com.smartlogi.security.dto.authDto.request.LoginRequest;
import com.smartlogi.security.dto.authDto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse login(LoginRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        return new JwtAuthResponse(token, "Bearer");
    }


    public User signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User déga excité");
        }
        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        user.setRole(role);

        return userRepository.save(user);
    }

}
