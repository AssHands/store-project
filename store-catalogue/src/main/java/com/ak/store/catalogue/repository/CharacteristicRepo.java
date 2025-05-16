package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
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
            JOIN c.categories cc
            WHERE cc.category.id = :categoryId
            """)
    List<Characteristic> findAllWithTextValuesByCategoryId(Long categoryId);

    Boolean existsByNameEqualsIgnoreCase(String name);

    //------------------

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @Query("""
            SELECT c FROM Characteristic c
            JOIN c.categories cc
            WHERE cc.category.id = :categoryId
            """)
    List<Characteristic> findAllByCategoryId(Long categoryId);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"textValues"})
    @Query("SELECT c FROM Characteristic c WHERE c.id = :id")
    Optional<Characteristic> findOneByIdWithTextValues(Long id);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"numericValues"})
    @Query("SELECT c FROM Characteristic c WHERE c.id = :id")
    Optional<Characteristic> findOneByIdWithNumericValues(Long id);
}