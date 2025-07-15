package com.ak.store.SynchronizationSagaWorker.repository;

import com.ak.store.SynchronizationSagaWorker.model.document.Product;

public interface ProductRepo {
    void save(Product product);

    void deleteById(Long id);
}