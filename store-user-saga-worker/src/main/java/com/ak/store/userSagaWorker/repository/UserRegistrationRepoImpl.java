package com.ak.store.userSagaWorker.repository;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRegistrationRepoImpl implements UserRegistrationRepo {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void deleteOne(UUID id) {
        UserResource usersResource = getUsersResource().get(id.toString());
        usersResource.remove();
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }
}