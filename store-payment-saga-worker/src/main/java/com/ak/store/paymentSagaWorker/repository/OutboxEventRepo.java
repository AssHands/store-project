package com.ak.store.paymentSagaWorker.repository;

import com.ak.store.paymentSagaWorker.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, Long> {
}
