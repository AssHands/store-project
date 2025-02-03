package com.ak.store.catalogue.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepoImpl implements ProductRepoCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void clear() {
        entityManager.clear();
    }
}
