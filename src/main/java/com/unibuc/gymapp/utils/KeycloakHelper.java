package com.unibuc.gymapp.utils;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;

public class KeycloakHelper {
    public static Long getUserId(Authentication authentication) {
        return Long.parseLong(((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext()
                .getToken().getPreferredUsername());
    }
}
