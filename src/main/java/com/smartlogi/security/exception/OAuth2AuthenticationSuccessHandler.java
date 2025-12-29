package com.smartlogi.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogi.security.service.JwtService;
import com.smartlogi.security.service.OAuth2UserPrincipal;
import com.smartlogi.smartlogiv010.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (response.isCommitted()) {
            logger.debug("Response has already been committed.");
            return;
        }

        clearAuthenticationAttributes(request);

        User user = extractUserFromAuthentication(authentication);
        String token = jwtService.generateToken(user);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "OAuth2 authentication successful");
        responseBody.put("timestamp", LocalDateTime.now().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        data.put("user", buildUserInfo(user));
        responseBody.put("data", data);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.getWriter().flush();
    }

    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("email", user.getEmail());
        userInfo.put("nom", user.getNom());
        userInfo.put("prenom", user.getPrenom());
        userInfo.put("provider", user.getProvider() != null ? user.getProvider().name() : null);
        return userInfo;
    }

    private User extractUserFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return ((OAuth2UserPrincipal) principal).getUser();
        } else if (principal instanceof User) {
            return (User) principal;
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }
}
