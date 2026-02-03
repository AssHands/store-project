package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category", "category.characteristics"})
    Optional<Product> findOneWithCategoryCharacteristicsById(Long id);

    @Query("SELECT COUNT(p) = :amount FROM Product p WHERE p.id IN :ids AND p.status = 'ACTIVE'")
    boolean isExistAllByIds(List<Long> ids, long amount);

    @Query("SELECT COUNT(p) = :amount FROM Product p WHERE p.id IN :ids AND p.isAvailable = true AND p.status = 'ACTIVE'")
    boolean isAvailableAllByIds(List<Long> ids, long amount);
}