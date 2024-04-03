package com.example.backend.service.implement;

import com.example.backend.config.KeycloakConfig;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.SignUpResponseDto;
import com.example.backend.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class KeycloakServiceImplement implements KeycloakService {
    private final KeycloakConfig keycloakConfig;

    public KeycloakServiceImplement(KeycloakConfig keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
    }

    @Override
    public SignUpResponseDto signUpStudent(SignUpDto signUpDto) {
//        Keycloak keycloak = KeycloakBuilder.builder()
//                .serverUrl(keycloakConfig.getServerUrl())
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .realm(keycloakConfig.getRealm())
//                .clientId(keycloakConfig.getAdminClientId())
//                .clientSecret(keycloakConfig.getAdminClientSecret())
//                .build();
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfig.getServerUrl())
                .realm(keycloakConfig.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(keycloakConfig.getAdminClientId())
                .clientSecret(keycloakConfig.getAdminClientSecret())
                .username(keycloakConfig.getAdminUsername())
                .password(keycloakConfig.getAdminPassword())
                .build();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(signUpDto.getUsername());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        System.out.println(signUpDto.getEmail());
        user.setEmail(signUpDto.getEmail());
        user.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(signUpDto.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> credentials = List.of(credentialRepresentation);
        user.setCredentials(credentials);
        UsersResource usersResource = keycloak.realm(keycloakConfig.getRealm()).users();

        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        RoleRepresentation studentRole = keycloak.realm(keycloakConfig.getRealm()).roles().get("student").toRepresentation();

        usersResource.get(userId).roles().realmLevel().add(List.of(studentRole));
        return null;
    }

    @Override
    public SignUpResponseDto signUpAdmin(SignUpDto signUpDto) {
        return null;
    }
}
