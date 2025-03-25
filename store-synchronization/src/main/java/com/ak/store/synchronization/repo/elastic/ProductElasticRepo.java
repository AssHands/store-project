package com.ak.store.synchronization.repo.elastic;


import com.ak.store.common.model.catalogue.document.ProductDocument;

import java.util.List;

public interface ProductElasticRepo {
    void saveOne(ProductDocument productDocument);
    void saveAll(List<ProductDocument> productDocuments);
    void updateOne(ProductDocument productDocument);
    void updateAll(List<ProductDocument> productDocuments);
    void deleteOne(Long id);
    void deleteAll(List<Long> ids);
}
