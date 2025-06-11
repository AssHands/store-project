package com.ak.store.synchronization.repository.elastic;


import com.ak.store.synchronization.model.document.Product;

public interface ProductElasticRepo {
    void saveOne(Product product);

    void updateOne(Product product);

    void deleteOne(Long id);
}