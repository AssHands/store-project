package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ne.Characteristic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
    @EntityGraph(attributePaths = { "textValues", "rangeValues" }, type = EntityGraph.EntityGraphType.FETCH)
    List<Characteristic> findAllValuesByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = { "textValues" }, type = EntityGraph.EntityGraphType.FETCH)
    List<Characteristic> findTextValuesByCategoryId(Long categoryId);
}