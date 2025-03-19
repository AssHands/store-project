package com.ak.store.synchronization.service;

import com.ak.store.synchronization.model.document.ProductDocument;
import com.ak.store.synchronization.repo.elastic.ProductElasticRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductElasticService {
    private final ProductElasticRepo productRepo;

    public void compensateDeleteOne(ProductDocument productDocument) {
        productRepo.saveOne(productDocument);
    }

    public void compensateCreateOne(Long id) {
        productRepo.deleteOne(id);
    }

    public void createOne(ProductDocument productDocument) {
        productRepo.saveOne(productDocument);
    }

    public void createAll(List<ProductDocument> productDocuments) {
        productRepo.saveAll(productDocuments);
    }

    public void updateOne(ProductDocument productDocument) {
        productRepo.updateOne(productDocument);
    }

    public void updateAll(List<ProductDocument> productDocuments) {
        productRepo.updateAll(productDocuments);
    }

    public void deleteOne(Long id) {
        productRepo.deleteOne(id);
    }

    public void deleteAll(List<Long> ids) {
        productRepo.deleteAll(ids);
    }
}