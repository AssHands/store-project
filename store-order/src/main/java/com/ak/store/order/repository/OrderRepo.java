package com.ak.store.order.repository;

import com.ak.store.order.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"products"})
    List<Order> findAllWithProductsByUserId(UUID userId);

    List<Order> findAllByUserId(UUID userId);
}
