package com.ak.store.warehouseSagaWorker.repository;

import com.ak.store.warehouseSagaWorker.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, Long> {
}
