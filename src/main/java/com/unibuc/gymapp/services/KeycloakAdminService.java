package com.unibuc.gymapp.services;

import com.unibuc.gymapp.clients.AuthClient;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
@Slf4j
public class KeycloakAdminService {
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String client;
    private final Keycloak keycloak;
    private final AuthClient authClient;
    private final UserRepository userRepository;
    private RealmResource realmResource;

    @Autowired
    public KeycloakAdminService(Keycloak keycloak, AuthClient authClient, UserRepository userRepository) {
        this.keycloak = keycloak;
        this.authClient = authClient;
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void initRealmResource() {
        this.realmResource = this.keycloak.realm(realm);
    }

    public void addUserToKeycloak(User user, String password, String role) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getId().toString());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        Response response = realmResource.users().create(userRepresentation);
        String keycloakUserId = getCreatedId(response);
        log.info("User has been saved with the id: " + keycloakUserId);

        UserResource userResource = realmResource.users().get(keycloakUserId);
        RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        log.info("Role " + role + " added successfully to user with id " + keycloakUserId);
    }
}
