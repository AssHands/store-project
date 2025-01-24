package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Category;
import jakarta.persistence.QueryHint;
import lombok.NonNull;
import org.hibernate.jpa.AvailableHints;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findAllByNameContainingIgnoreCase(String name);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    @NonNull
    List<Category> findAll();
}