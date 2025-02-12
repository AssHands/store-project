package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Category;
import jakarta.persistence.QueryHint;
import lombok.NonNull;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @NonNull
    List<Category> findAll();

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @NonNull
    Optional<Category> findById(@NonNull Long id);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"characteristics"})
    Optional<Category> findOneWithCharacteristicsById(Long id);
}