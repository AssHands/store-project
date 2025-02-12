package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Characteristic;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;

public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"textValues"})
    @Query("""
            SELECT c FROM Characteristic c
            JOIN c.category cc
            WHERE cc.category.id = :categoryId
            """)
    List<Characteristic> findAllWithTextValuesByCategoryId(Long categoryId);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"textValues"})
    Optional<Characteristic> findOneWithTextValuesById(Long id);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"rangeValues"})
    List<Characteristic> findAllWithRangeValuesById(Long id);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"rangeValues"})
    Optional<Characteristic> findOneWithRangeValuesById(Long id);

    Boolean existsByNameEqualsIgnoreCase(String name);
}