package com.ak.store.search.repository;

import com.ak.store.search.model.entity.Characteristic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
    @EntityGraph(attributePaths = {"textValues", "numericValues"})
    @Query("""
            SELECT c FROM Characteristic c
            JOIN c.categories cc
            WHERE cc.category.id = :categoryId
            """)
    List<Characteristic> findAllWithValuesByCategoryId(Long categoryId);
}
