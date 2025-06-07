package com.ak.store.synchronization.repo.elastic;


import com.ak.store.common.document.catalogue.ProductDocument;

public interface ProductElasticRepo {
    void saveOne(ProductDocument productDocument);

    void updateOne(ProductDocument productDocument);

    void deleteOne(Long id);
}