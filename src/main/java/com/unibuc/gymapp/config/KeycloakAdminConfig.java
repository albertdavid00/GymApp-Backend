package com.unibuc.gymapp.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${my.keycloak.admin.username}")
    private String adminKeycloakUsername;

    @Value("${my.keycloak.admin.password}")
    private String adminKeycloakPassword;

    @Value("${my.keycloak.admin.realm}")
    private String adminKeycloakRealm;

    @Value("${my.keycloak.admin.client}")
    private String adminKeycloakClient;

    @Bean
    public Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .username(adminKeycloakUsername)
                .grantType(OAuth2Constants.PASSWORD)
                .password(adminKeycloakPassword)
                .realm(adminKeycloakRealm)
                .clientId(adminKeycloakClient)
                .build();
    }
}
