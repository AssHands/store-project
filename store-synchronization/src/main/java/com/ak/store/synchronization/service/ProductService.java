package com.ak.store.synchronization.service;

import com.ak.store.synchronization.mapper.ProductMapper;
import com.ak.store.synchronization.model.command.product.WriteProductPayloadCommand;
import com.ak.store.synchronization.model.command.product.WriteProductRatingCommand;
import com.ak.store.synchronization.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public void updateOne(WriteProductPayloadCommand command) {
        productRepo.updateOne(productMapper.toDocument(command));
    }

    public void deleteOne(Long id) {
        productRepo.deleteOne(id);
    }

    public void updateOneRating(WriteProductRatingCommand command) {
        productRepo.updateOne(productMapper.toDocument(command));
    }
}