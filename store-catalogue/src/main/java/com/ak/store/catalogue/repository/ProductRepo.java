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

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    boolean IsExistOneById(@NonNull Long id);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.id = :id AND p.isAvailable = true AND p.isDeleted = false")
    boolean isAvailableOneById(Long id);

    @Query("SELECT COUNT(p) = :amount FROM Product p WHERE p.id IN :ids AND p.isDeleted = false")
    boolean isExistAllByIds(List<Long> ids, long amount);

    @Query("SELECT COUNT(p) = :amount FROM Product p WHERE p.id IN :ids AND p.isAvailable = true AND p.isDeleted = false")
    boolean isAvailableAllByIds(List<Long> ids, long amount);
}