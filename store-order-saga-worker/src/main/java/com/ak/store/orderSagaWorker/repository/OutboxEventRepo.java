package com.ak.store.orderSagaWorker.repository;

import com.ak.store.orderSagaWorker.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, Long> {
}
