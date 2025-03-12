package com.ak.store.catalogue.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxTaskRepo extends JpaRepository<OutboxTask, Long> {
}
