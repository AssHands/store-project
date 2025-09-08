package com.ak.store.catalogueSagaWorker.repository;

import com.ak.store.catalogueSagaWorker.model.product.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    //Можно читать, Нельзя писать
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = "ratingSummary")
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.status = 'ACTIVE'")
    Optional<Product> findOneWithRatingSummaryById(Long id);
}