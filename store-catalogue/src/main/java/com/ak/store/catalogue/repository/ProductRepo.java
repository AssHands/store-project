package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"characteristics", "characteristics.characteristic", "category", "images"})
    List<Product> findAllFullByIdIn(Collection<Long> ids);


    //todo: EntityGraph не тестировал
    @EntityGraph(attributePaths = {"characteristics", "characteristics.characteristic", "category", "images"})
    Optional<Product> findOneFullById(Long id);


    //todo: EntityGraph не тестировал
    @EntityGraph(attributePaths = {"images"})
    List<Product> findAllViewByIdIn(Collection<Long> ids);


    //todo: EntityGraph не тестировал
    @EntityGraph(attributePaths = {"images"})
    Optional<Product> findOneViewById(Long id);

    @EntityGraph(attributePaths = {"characteristics", "characteristics.characteristic", "category"})
    Optional<Product> findOneForUpdateById(Long id);

    @EntityGraph(attributePaths = {"images"})
    Optional<Product> findOneWithImagesById(Long id);
}