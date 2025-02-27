package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.consumer.id = :consumerId")
    List<Cart> findAllByConsumerId(String consumerId);

    @Query("SELECT c FROM Cart c WHERE c.consumer.id = :consumerId AND c.productId = :productId")
    Optional<Cart> findByConsumerIdAndProductId(String consumerId, Long productId);

    void deleteAllByProductId(Long productId);
}
