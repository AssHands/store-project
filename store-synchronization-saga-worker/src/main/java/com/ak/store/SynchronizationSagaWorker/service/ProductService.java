package com.ak.store.SynchronizationSagaWorker.service;

import com.ak.store.SynchronizationSagaWorker.mapper.ProductMapper;
import com.ak.store.SynchronizationSagaWorker.model.dto.write.ProductWriteDTOPayload;
import com.ak.store.SynchronizationSagaWorker.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public void createOne(ProductWriteDTOPayload request) {
        productRepo.save(productMapper.toProduct(request));
    }

    public void deleteOne(Long id) {
        productRepo.deleteById(id);
    }
}
