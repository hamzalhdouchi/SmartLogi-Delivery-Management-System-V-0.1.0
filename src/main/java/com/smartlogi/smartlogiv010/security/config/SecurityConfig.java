//package com.smartlogi.smartlogiv010.security.config;
//
//import com.smartlogi.smartlogiv010.security.filter.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final UserDetailsService userDetailsService;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeRequests(auth -> auth
//                        .requestMatchers("/api/login").permitAll()
//                        .requestMatchers("/api/register").permitAll()
//                        .requestMatchers("/api/colis").hasRole("Gestionnaire")
//                        .requestMatchers("/api/clients-expediteurs").hasRole("Gestionnaire")
//                        .requestMatchers("/api/colis/destinataire/")
//                )
//                .formLogin(form -> form
//                        .usernameParameter("email")
//                .passwordParameter("password")
//                )
//return http.build();
//    }
//}
