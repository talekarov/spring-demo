package com.tkarov.demo.utils;

import com.tkarov.demo.dto.RoleAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.Set;


@Slf4j
public final class AuthenticationUtils {

    private AuthenticationUtils() {
        throw new IllegalArgumentException("Util class");
    }

    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getUsername();
            }
        }
        return StringUtils.EMPTY;
    }

    public static Set<RoleAuth> getAuthenticatedUserRoles() {
        Set<RoleAuth> roles = new HashSet<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                roles.add(RoleAuth.builder().name(authority.getAuthority()).build());
            }
        }
        return roles;
    }
}
