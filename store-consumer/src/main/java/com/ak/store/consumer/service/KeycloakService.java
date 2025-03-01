package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import jakarta.ws.rs.ClientErrorException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createOneConsumer(ConsumerDTO consumerDTO) {
        CredentialRepresentation credential = createPasswordCredentials(consumerDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setEmail(consumerDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setFirstName(consumerDTO.getName());
        user.setEnabled(true);

        UsersResource userResource = getUsersResource();
        var response = userResource.create(user);

        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new RuntimeException("email already exists");
        }

        String consumerId = CreatedResponseUtil.getCreatedId(response);
        addRealmRoleToUser(consumerId, List.of("ROLE_CONSUMER"));

        return consumerId;
    }

    public void deleteOneConsumer(String consumerId) {
        UserResource usersResource = getUsersResource().get(consumerId);
        usersResource.remove();
    }

    public String updateOneConsumer(String consumerId, ConsumerDTO consumerDTO) {
        UserRepresentation user = new UserRepresentation();
        updateUser(user, consumerDTO);

        UserResource userResource = getUsersResource().get(consumerId);
        try {
            userResource.update(user);
        } catch (ClientErrorException e) {
            throw new RuntimeException("email already exists");
        }

        return consumerId;
    }

    private void addRealmRoleToUser(String consumerId, List<String> roles) {
        List<RoleRepresentation> kcRoles = new ArrayList<>();
        for (String role : roles) {
            RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role).toRepresentation();
            kcRoles.add(roleRep);
        }

        UserResource userResource = getUsersResource().get(consumerId);
        userResource.roles().realmLevel().add(kcRoles);
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

    private void updateUser(UserRepresentation user, ConsumerDTO consumerDTO) {
        if (consumerDTO.getEmail() != null) {
            user.setEmail(consumerDTO.getEmail());
        }
        if (consumerDTO.getName() != null) {
            user.setFirstName(consumerDTO.getName());
        }
        if (consumerDTO.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(consumerDTO.getPassword());
            user.setCredentials(Collections.singletonList(credentialRepresentation));
        }
    }

    public void verifyOneConsumer(String consumerId) {
        UserRepresentation user = new UserRepresentation();
        user.setEmailVerified(true);

        UserResource userResource = getUsersResource().get(consumerId);
        try {
            userResource.update(user);
        } catch (ClientErrorException e) {
            throw new RuntimeException("error while verifying email kc");
        }
    }
}
