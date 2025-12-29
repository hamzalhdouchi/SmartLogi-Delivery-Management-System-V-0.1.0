package com.smartlogi.security.dto.oauth2;

import com.smartlogi.security.enums.AuthProvider;
import com.smartlogi.security.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

/**
 * Factory class to create appropriate OAuth2UserInfo based on provider.
 */
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.APPLE.toString())) {
            return new AppleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.OKTA.toString())) {
            return new OktaOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                "Login with " + registrationId + " is not supported");
        }
    }
}
