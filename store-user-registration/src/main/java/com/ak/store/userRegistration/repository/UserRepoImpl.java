package com.ak.store.userRegistration.repository;

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
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepoImpl implements UserRepo {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


    @Override
    public UUID registerOne(String name, String email, String password) {
        CredentialRepresentation credential = createPasswordCredentials(password);

        UserRepresentation user = new UserRepresentation();
        user.setEmail(email);
        user.setCredentials(Collections.singletonList(credential));
        user.setFirstName(name);
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

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();

        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
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
}
