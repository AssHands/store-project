package com.ak.store.SynchronizationSagaWorker.repository;

import com.ak.store.SynchronizationSagaWorker.model.document.Product;

public interface ProductRepo {
    void createOne(Product product);

    void deleteOneById(Long id);
}