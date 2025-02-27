package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer, UUID> {
    boolean existsOneById(UUID id);
}
