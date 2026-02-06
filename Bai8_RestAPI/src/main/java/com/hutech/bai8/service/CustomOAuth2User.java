package com.hutech.bai8.service;

import com.hutech.bai8.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Wrapper class ket hop User entity voi OAuth2User
 * De Spring Security co the su dung ca 2:
 * - UserDetails (cho form login)
 * - OAuth2User (cho OAuth2 login)
 */
public class CustomOAuth2User implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    // Helper methods de truy cap User entity
    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public String getAvatarUrl() {
        return user.getAvatarUrl();
    }
}
