package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ne.Characteristic;
import com.ak.store.catalogue.model.entity.ne.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @NonNull
    @EntityGraph(attributePaths = { "characteristics", "characteristics.characteristic", "category"},
            type = EntityGraph.EntityGraphType.LOAD)
    List<Product> findAll();


    @NonNull
    @EntityGraph(attributePaths = { "characteristics", "characteristics.characteristic", "category"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findById(@NonNull Long id);
}