package com.smartlogi.security.dto.oauth2;

import java.util.Map;

/**
 * Abstract class representing OAuth2 user information.
 * Each provider (Google, Facebook, Apple, Okta) extends this class.
 */
public abstract class OAuth2UserInfo {
    
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
