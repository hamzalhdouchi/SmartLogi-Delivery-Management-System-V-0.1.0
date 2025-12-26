    package com.smartlogi.smartlogiv010.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.*;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
    @Table(name = "users")
    public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        private String nom;
        private String prenom;
        private String telephone;

        @Column(unique = true, nullable = false)
        private String email;

        private String adresse;

        @Column(nullable = false)
        private String password;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "zone_id")
        private Zone zone;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "role_id", nullable = false)
        private Role role;

        private boolean enabled = true;

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(role.getName()));

            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }

            return authorities;
        }

        @Override
        public boolean isAccountNonExpired() { return true; }

        @Override
        public boolean isAccountNonLocked() { return true; }

        @Override
        public boolean isCredentialsNonExpired() { return true; }

        @Override
        public boolean isEnabled() { return enabled; }
    }
