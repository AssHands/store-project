package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createConsumer(ConsumerDTO consumerDTO) {
        CredentialRepresentation credential = createPasswordCredentials(consumerDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(consumerDTO.getEmail());
        user.setEmail(consumerDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setFirstName(consumerDTO.getName());
        user.setEnabled(true);

        UsersResource usersResource = getUsersResource();
        var response = usersResource.create(user);

        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new RuntimeException("user or email already exists");
        }

        String consumerId = CreatedResponseUtil.getCreatedId(response);
        addRealmRoleToUser(consumerId, List.of("ROLE_CONSUMER"));

        return consumerId;
    }

    private void addRealmRoleToUser(String consumerId, List<String> roles) {
        List<RoleRepresentation> kcRoles = new ArrayList<>();
        for(String role : roles) {
            RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role).toRepresentation();
            kcRoles.add(roleRep);
        }

        UserResource uniqUserResource = getUsersResource().get(consumerId);
        uniqUserResource.roles().realmLevel().add(kcRoles);
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
