package com.smartlogi.smartlogiv010.security.config;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<GrantedAuthority> authorities = getAuthorities(user);

        System.out.println("=== USER LOADED ===");
        System.out.println("Email: " + email);
        System.out.println("Role: " + (user.getRole() != null ? user.getRole().getName() : "NO_ROLE"));
        System.out.println("Authorities: " + authorities);
        System.out.println("===================");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Role role = user.getRole();

        if (role == null) {
            System.out.println("WARNING: User has no role assigned");
            return authorities;
        }

        String roleName = role.getName().startsWith("ROLE_")
                ? role.getName()
                : "ROLE_" + role.getName();
        authorities.add(new SimpleGrantedAuthority(roleName));

        System.out.println("Adding role: " + roleName);

        if (role.getPermissions() != null) {
            for (Permission permission : role.getPermissions()) {
                System.out.println("Adding permission: " + permission.getName());
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return authorities;
    }
}
