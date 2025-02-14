package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Product;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long>, ProductRepoCustom {
    //todo: нахуя characteristics.characteristic
    @EntityGraph(attributePaths = {"characteristics", "characteristics.characteristic", "category"})
    Optional<Product> findOneWithCharacteristicsAndCategoryById(Long id);

    @EntityGraph(attributePaths = {"images"})
    List<Product> findAllWithImagesByIdIn(Collection<Long> ids);

    @EntityGraph(attributePaths = {"images"})
    List<Product> findAllWithImagesByIdIn(Collection<Long> ids, Sort sort);

    @EntityGraph(attributePaths = {"images"})
    Optional<Product> findOneWithImagesById(Long id);

    boolean existsById(@NonNull Long id);

    boolean existsByIdAndIsAvailableIsTrue(Long id);
}