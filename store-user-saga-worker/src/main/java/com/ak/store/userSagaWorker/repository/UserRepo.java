package com.ak.store.userSagaWorker.repository;

import com.ak.store.userSagaWorker.model.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = "verificationCode")
    Optional<User> findOneWithVerificationCodeById(UUID id);
}
