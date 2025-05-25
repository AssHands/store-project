package com.ak.store.synchronization.repo.elastic;


import com.ak.store.common.model.catalogue.document.ProductDocument;

import java.util.List;

public interface ProductElasticRepo {
    void saveOne(ProductDocument productDocument);

    void updateOne(ProductDocument productDocument);

    void deleteOne(Long id);
}