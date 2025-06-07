package com.ak.store.catalogueRatingUpdater.repository;

import com.ak.store.catalogueRatingUpdater.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "ratingSummary")
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Product> findOneWithRatingSummaryById(Long id);
}