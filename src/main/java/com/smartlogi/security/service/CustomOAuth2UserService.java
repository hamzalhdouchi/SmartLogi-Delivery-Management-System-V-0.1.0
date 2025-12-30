package com.smartlogi.security.service;

import com.smartlogi.security.dto.oauth2.OAuth2UserInfo;
import com.smartlogi.security.dto.oauth2.OAuth2UserInfoFactory;
import com.smartlogi.security.enums.AuthProvider;
import com.smartlogi.security.exception.OAuth2AuthenticationProcessingException;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.repository.RoleRepository;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final String DEFAULT_OAUTH2_ROLE = "ROLE_CLIENT";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            
            if (!user.getProvider().equals(AuthProvider.valueOf(registrationId.toUpperCase()))) {
                user = updateExistingUser(user, oAuth2UserInfo, registrationId);
            } else {
                user = updateExistingUser(user, oAuth2UserInfo, registrationId);
            }
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return new OAuth2UserPrincipal(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        
        User user = new User();
        user.setProvider(AuthProvider.valueOf(registrationId.toUpperCase()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setEmail(oAuth2UserInfo.getEmail());
        
        String fullName = oAuth2UserInfo.getName();
        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.split(" ", 2);
            user.setPrenom(nameParts[0]);
            if (nameParts.length > 1) {
                user.setNom(nameParts[1]);
            }
        }
        
        user.setPassword("YOUCODE_CODE");

        Role defaultRole = roleRepository.findByName(DEFAULT_OAUTH2_ROLE)
                .orElseThrow(() -> new OAuth2AuthenticationProcessingException(
                    "Default role '" + DEFAULT_OAUTH2_ROLE + "' not found"));
        user.setRole(defaultRole);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        existingUser.setProvider(AuthProvider.valueOf(registrationId.toUpperCase()));
        existingUser.setProviderId(oAuth2UserInfo.getId());

        String fullName = oAuth2UserInfo.getName();
        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.split(" ", 2);
            if (existingUser.getPrenom() == null || existingUser.getPrenom().isEmpty()) {
                existingUser.setPrenom(nameParts[0]);
            }
            if (nameParts.length > 1 && (existingUser.getNom() == null || existingUser.getNom().isEmpty())) {
                existingUser.setNom(nameParts[1]);
            }
        }

        return userRepository.save(existingUser);
    }
}
