package com.hutech.bai8.service;

import com.hutech.bai8.model.Role;
import com.hutech.bai8.model.User;
import com.hutech.bai8.repository.RoleRepository;
import com.hutech.bai8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service xu ly OAuth2 user tu Google (hoac Facebook, GitHub, etc.)
 * 
 * Flow:
 * 1. User click "Login with Google"
 * 2. Google tra ve thong tin user (email, name, picture, sub)
 * 3. Kiem tra user da ton tai trong DB chua (theo provider + providerId)
 * 4. Neu chua co -> tao user moi
 * 5. Neu co roi -> cap nhat thong tin (avatar, name)
 * 6. Tra ve OAuth2User de Spring Security tao session
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Goi default service de lay thong tin user tu Google
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        // Lay provider name (google, facebook, github, etc.)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        // Xu ly user theo provider
        return processOAuth2User(provider, oauth2User);
    }

    private OAuth2User processOAuth2User(String provider, OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        String providerId;
        String email;
        String name;
        String avatarUrl;
        
        // Parse thong tin tuy theo provider
        if ("google".equals(provider)) {
            providerId = (String) attributes.get("sub");          // Google user ID
            email = (String) attributes.get("email");              // Email
            name = (String) attributes.get("name");                // Full name
            avatarUrl = (String) attributes.get("picture");        // Avatar URL
        } else {
            // Co the them Facebook, GitHub, etc. o day
            throw new OAuth2AuthenticationException("Provider " + provider + " is not supported");
        }
        
        log.info("OAuth2 Login - Provider: {}, Email: {}, Name: {}", provider, email, name);
        
        // Tim user trong DB
        Optional<User> existingUser = userRepository.findByProviderAndProviderId(provider, providerId);
        
        User user;
        if (existingUser.isPresent()) {
            // User da ton tai -> cap nhat thong tin
            user = existingUser.get();
            user.setFullName(name);
            user.setAvatarUrl(avatarUrl);
            user = userRepository.save(user);
            log.info("OAuth2 User updated: {}", user.getEmail());
        } else {
            // Kiem tra email da ton tai chua (user co the da dang ky bang form truoc do)
            Optional<User> userByEmail = userRepository.findByEmail(email);
            
            if (userByEmail.isPresent()) {
                // Link OAuth2 vao tai khoan cu
                user = userByEmail.get();
                user.setProvider(provider);
                user.setProviderId(providerId);
                user.setAvatarUrl(avatarUrl);
                user = userRepository.save(user);
                log.info("OAuth2 linked to existing user: {}", user.getEmail());
            } else {
                // Tao user moi
                user = createNewOAuth2User(provider, providerId, email, name, avatarUrl);
                log.info("OAuth2 New user created: {}", user.getEmail());
            }
        }
        
        // Tra ve CustomOAuth2User (wrap User entity + OAuth2 attributes)
        return new CustomOAuth2User(user, attributes);
    }

    private User createNewOAuth2User(String provider, String providerId, String email, String name, String avatarUrl) {
        // Lay role USER
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER", "Nguoi dung thuong")));
        
        User user = new User();
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setEmail(email);
        user.setFullName(name);
        user.setAvatarUrl(avatarUrl);
        user.setEnabled(true);
        
        // Tao username tu email (phan truoc @)
        String username = email.split("@")[0] + "_" + provider;
        // Kiem tra username trung
        int counter = 0;
        String originalUsername = username;
        while (userRepository.existsByUsername(username)) {
            counter++;
            username = originalUsername + counter;
        }
        user.setUsername(username);
        
        // Tao random password (user se khong dung password nay, chi login qua OAuth2)
        user.setPassword(UUID.randomUUID().toString());
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
}
