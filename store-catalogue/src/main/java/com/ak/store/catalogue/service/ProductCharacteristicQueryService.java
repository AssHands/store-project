package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCharacteristicQueryService {
    private final ProductCharacteristicRepo productCharacteristicRepo;

    public List<ProductCharacteristic> findAllByProductId(Long productId) {
        return productCharacteristicRepo.findAllByProductId(productId);
    }
}
