package com.ak.store.user.repository;

import com.ak.store.user.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsOneById(UUID id);

    @Query("SELECT c, vc FROM VerificationCode vc JOIN vc.user c WHERE vc.code = :code")
    Optional<User> findOneByVerificationCode(String code);

    @EntityGraph(attributePaths = "verificationCode")
    Optional<User> findOneWithVerificationCodeById(UUID id);
}
