package com.smartlogi.smartlogiv010.security.service;

import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.exception.AuthenticationExceptionhandler;
import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
import com.smartlogi.smartlogiv010.repository.RoleRepository;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import com.smartlogi.smartlogiv010.security.config.JwtService;
import com.smartlogi.smartlogiv010.security.dto.JwtAuthResponse;
import com.smartlogi.smartlogiv010.security.dto.LoginRequest;
import com.smartlogi.smartlogiv010.security.dto.SignupRequest;
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (RuntimeException e) {
            throw new AuthenticationExceptionhandler("Email ou mot de passe incorrect. Veuillez réessayer.");

        }

        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        return new JwtAuthResponse(token, "Bearer");
    }

    public User signup(SignupRequest request) {
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
