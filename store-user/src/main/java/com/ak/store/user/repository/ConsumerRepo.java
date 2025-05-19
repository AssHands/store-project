package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.Consumer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer, UUID> {
    boolean existsOneById(UUID id);

    @Query("SELECT c, vc FROM VerificationCode vc JOIN vc.consumer c WHERE vc.code = :code")
    Optional<Consumer> findOneByVerificationCode(String code);

    @EntityGraph(attributePaths = "verificationCode")
    Optional<Consumer> findOneWithCodeById(UUID id);
}
