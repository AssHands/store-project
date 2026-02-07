package com.ak.store.synchronization.repository;


import com.ak.store.synchronization.model.document.Product;

public interface ProductRepo {
    void updateOne(Product product);

    void deleteOne(Long id);
}