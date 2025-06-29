package com.ak.store.orderSagaWorker.repository;

import com.ak.store.orderSagaWorker.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
}
