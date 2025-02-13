package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.consumer.id = :id")
    List<Cart> findAllByConsumerId(Long id);

    @Query("SELECT c FROM Cart c WHERE c.consumer.id = :id AND c.productId = :productId")
    Optional<Cart> findByConsumerIdAndProductId(Long id, Long productId);
}
