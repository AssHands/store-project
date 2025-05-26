package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.dto.write.ProductCharacteristicWriteDTO;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.validator.service.ProductCharacteristicServiceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {
    private final ProductCharacteristicMapper productCharacteristicMapper;
    private final ProductCharacteristicServiceValidator productCharacteristicServiceValidator;
    private final ProductCharacteristicRepo productCharacteristicRepo;

    private List<ProductCharacteristic> findAllByProductId(Long productId) {
        return productCharacteristicRepo.findAllByProductId(productId);
    }

    public List<ProductCharacteristicDTO> findAll(Long productId) {
        return productCharacteristicMapper.toProductCharacteristicDTO(findAllByProductId(productId));
    }

    @Transactional
    public List<ProductCharacteristicDTO> createAll(Long productId,
                                                    List<ProductCharacteristicWriteDTO> productCharacteristics) {
        if (productCharacteristics.isEmpty()) return findAll(productId);
        productCharacteristicServiceValidator.validateCreating(productId, productCharacteristics);

        List<ProductCharacteristic> existingProductCharacteristics =
                productCharacteristicMapper.toProductCharacteristic(productCharacteristics, productId);

        productCharacteristicRepo.saveAllAndFlush(existingProductCharacteristics);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTO> updateAll(Long productId,
                                                    List<ProductCharacteristicWriteDTO> productCharacteristics) {
        if (productCharacteristics.isEmpty()) return findAll(productId);
        productCharacteristicServiceValidator.validateUpdating(productId, productCharacteristics);

        List<ProductCharacteristic> existingProductCharacteristics = findAllByProductId(productId);
        for(var pc : productCharacteristics) {
            int index = findCharacteristicIndexById(existingProductCharacteristics, pc.getCharacteristicId());
            existingProductCharacteristics.get(index).setNumericValue(pc.getNumericValue());
            existingProductCharacteristics.get(index).setTextValue(pc.getTextValue());
        }

        productCharacteristicRepo.saveAllAndFlush(existingProductCharacteristics);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTO> deleteAllByCharacteristicIds(Long productId, List<Long> characteristicIds) {
        if (characteristicIds.isEmpty()) return findAll(productId);

        productCharacteristicRepo.deleteAllByProductIdAndCharacteristicIdIn(productId, characteristicIds);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTO> deleteAll(Long productId) {
        var productCharacteristics = findAll(productId);
        productCharacteristicRepo.deleteAllByProductId(productId);
        return productCharacteristics;
    }

    private int findCharacteristicIndexById(List<ProductCharacteristic> productCharacteristics, Long id) {
        int index = 0;
        for (var pc : productCharacteristics) {
            if (pc.getCharacteristic().getId().equals(id))
                return index;
            index++;
        }

        //todo перенести в слой валидации
        throw new RuntimeException("characteristic with id=%s didn't find in your product".formatted(id));
    }
}