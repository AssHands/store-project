package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = { "characteristics", "characteristics.characteristic", "category"},
            type = EntityGraph.EntityGraphType.LOAD)
    List<Product> findAllFullByIdIn(Collection<Long> ids);

    @EntityGraph(attributePaths = { "characteristics", "characteristics.characteristic", "category"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findOneFullById(Long id);

    List<Product> findAllViewByIdIn(Collection<Long> ids);
    Optional<Product> findOneViewById(Long id);

    @NonNull
    @EntityGraph(attributePaths = { "characteristics", "characteristics.characteristic", "category"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findById(@NonNull Long id);
}