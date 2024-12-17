package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ne.TextValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextValueRepo extends JpaRepository<TextValue, Long> {

    List<TextValue> findAll();
}
