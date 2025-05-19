package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.form.ConsumerForm;
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

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createOneConsumer(ConsumerForm consumerForm) {
        CredentialRepresentation credential = createPasswordCredentials(consumerForm.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setEmail(consumerForm.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setFirstName(consumerForm.getName());
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

    public String updateOneConsumer(String consumerId, ConsumerForm consumerForm) {
        UserRepresentation user = new UserRepresentation();
        updateUser(user, consumerForm);

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

    private void updateUser(UserRepresentation user, ConsumerForm consumerForm) {
        if (consumerForm.getName() != null) {
            user.setFirstName(consumerForm.getName());
        }
        if (consumerForm.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(consumerForm.getPassword());
            user.setCredentials(Collections.singletonList(credentialRepresentation));
        }
    }

    public void verifyOneConsumer(String consumerId, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setEmailVerified(true);
        user.setEmail(email);

        UserResource userResource = getUsersResource().get(consumerId);
        try {
            userResource.update(user);
        } catch (ClientErrorException e) {
            throw new RuntimeException("error while verifying email kc");
        }
    }
}
