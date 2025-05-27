package com.ak.store.order.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, Long> {
}
