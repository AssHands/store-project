package com.ak.store.userRegistration.service;

import com.ak.store.userRegistration.model.dto.write.UserWriteDTO;
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
public class UserKeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public UUID createOne(UserWriteDTO request) {
        CredentialRepresentation credential = createPasswordCredentials(request.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setEmail(request.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setFirstName(request.getName());
        user.setEnabled(true);

        UsersResource userResource = getUsersResource();
        var response = userResource.create(user);

        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new RuntimeException("email already exists");
        }

        UUID userId = UUID.fromString(CreatedResponseUtil.getCreatedId(response));
        addRealmRoleToUser(userId, List.of("ROLE_CONSUMER"));

        return userId;
    }

    public void deleteOne(UUID id) {
        UserResource usersResource = getUsersResource().get(id.toString());
        usersResource.remove();
    }

    public UUID updateOne(UUID id, UserWriteDTO request) {
        UserRepresentation user = new UserRepresentation();

        updateOneFromDTO(user, request);
        UserResource userResource = getUsersResource().get(id.toString());

        try {
            userResource.update(user);
        } catch (ClientErrorException e) {
            //todo catch 404 or 409 and send error
            throw new RuntimeException("email already exists");
        }

        return id;
    }

    private void addRealmRoleToUser(UUID id, List<String> roles) {
        List<RoleRepresentation> kcRoles = new ArrayList<>();
        for (String role : roles) {
            RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role).toRepresentation();
            kcRoles.add(roleRep);
        }

        UserResource userResource = getUsersResource().get(id.toString());
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

    private void updateOneFromDTO(UserRepresentation user, UserWriteDTO request) {
        if (request.getName() != null) {
            user.setFirstName(request.getName());
        }

        if (request.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(request.getPassword());
            user.setCredentials(Collections.singletonList(credentialRepresentation));
        }
    }

    public void verifyOne(UUID id, String email) {
        UserRepresentation user = new UserRepresentation();

        user.setEmailVerified(true);
        user.setEmail(email);

        UserResource userResource = getUsersResource().get(id.toString());
        try {
            userResource.update(user);
        } catch (ClientErrorException e) {
            throw new RuntimeException("error while verifying email kc");
        }
    }
}