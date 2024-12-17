package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ne.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findAllByNameContainingIgnoreCase(String name);
}