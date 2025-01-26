package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Characteristic;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = { "textValues", "rangeValues" })
    List<Characteristic> findAllWithAllValuesByCategoryId(Long categoryId);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = { "textValues" })
    List<Characteristic> findAllWithTextValuesByCategoryId(Long categoryId);
}