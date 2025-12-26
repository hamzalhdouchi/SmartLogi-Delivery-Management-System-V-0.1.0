package com.smartlogi.security.service;

import com.smartlogi.security.exception.DuplicateResourceException;
import com.smartlogi.security.userMapper.UserMapper;
import com.smartlogi.smartlogiv010.entity.*;
import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.*;
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
    private final UserMapper userMapper;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final LivreurRepository livreurRepository;
    private final DestinataireRepository destinataireRepository;
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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User déjà existé");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        User user;
        if (role.getName().equals("ROLE_CLIENT")) {
            ClientExpediteur client = new ClientExpediteur();
            client.setNom(request.getNom());
            client.setPrenom(request.getPrenom());
            client.setEmail(request.getEmail());
            client.setTelephone(request.getTelephone());
            client.setAdresse(request.getAdresse());
            client.setPassword(passwordEncoder.encode(request.getPassword()));
            client.setRole(role);
            client.setEnabled(true);
            user = clientExpediteurRepository.save(client);

        } else if (role.getName().equals("ROLE_LIVREUR")) {
            Livreur livreur = new Livreur();
            livreur.setNom(request.getNom());
            livreur.setPrenom(request.getPrenom());
            livreur.setEmail(request.getEmail());
            livreur.setTelephone(request.getTelephone());
            livreur.setAdresse(request.getAdresse());
            livreur.setPassword(passwordEncoder.encode(request.getPassword()));
            livreur.setRole(role);
            livreur.setEnabled(true);
            user = livreurRepository.save(livreur);

        } else if (role.getName().equals("ROLE_DESTINATAIRE")) {
            Destinataire destinataire = new Destinataire();
            destinataire.setNom(request.getNom());
            destinataire.setPrenom(request.getPrenom());
            destinataire.setEmail(request.getEmail());
            destinataire.setTelephone(request.getTelephone());
            destinataire.setAdresse(request.getAdresse());
            destinataire.setPassword(passwordEncoder.encode(request.getPassword()));
            destinataire.setRole(role);
            destinataire.setEnabled(true);
            user = destinataireRepository.save(destinataire);

        }else {
            User admin = new User();
            admin.setNom(request.getNom());
            admin.setPrenom(request.getPrenom());
            admin.setEmail(request.getEmail());
            admin.setTelephone(request.getTelephone());
            admin.setAdresse(request.getAdresse());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole(role);
            admin.setEnabled(true);
            user = userRepository.save(admin);
        }
        return user;
    }
}
