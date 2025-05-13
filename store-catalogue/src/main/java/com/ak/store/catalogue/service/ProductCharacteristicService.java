package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.ProductCharacteristicDTOnew;
import com.ak.store.catalogue.model.dto.write.ProductCharacteristicWriteDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import com.ak.store.catalogue.util.mapper.CharacteristicMapper;
import com.ak.store.catalogue.util.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.validator.service.ProductCharacteristicServiceValidator;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {

    private final ProductCharacteristicMapper productCharacteristicMapper;
    private final ProductCharacteristicServiceValidator productCharacteristicServiceValidator;
    private final ProductCharacteristicRepo productCharacteristicRepo;

    private int findProductCharacteristicIndexById(List<ProductCharacteristic> characteristics, Long id) {
        int index = 0;
        for (var characteristic : characteristics) {
            if (characteristic.getCharacteristic().getId().equals(id))
                return index;
            index++;
        }
        throw new RuntimeException("characteristic with id=%s didn't find in your product".formatted(id));
    }

    //-----------------------------

    private List<ProductCharacteristic> findAllByProductId(Long productId) {
        return productCharacteristicRepo.findAllByProductId(productId);
    }

    public List<ProductCharacteristicDTOnew> findAll(Long productId) {
        return productCharacteristicMapper.toProductCharacteristicDTOnew(findAllByProductId(productId));
    }

    @Transactional
    public List<ProductCharacteristicDTOnew> createAll(Long productId,
                                                       List<ProductCharacteristicWriteDTO> productCharacteristicDTOs) {
        if (productCharacteristicDTOs.isEmpty()) return Collections.emptyList();

        productCharacteristicServiceValidator.validateCreationNew(productId, productCharacteristicDTOs);
        List<ProductCharacteristic> productCharacteristics =
                productCharacteristicMapper.toProductCharacteristic(productCharacteristicDTOs, productId);

        productCharacteristicRepo.saveAllAndFlush(productCharacteristics);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTOnew> updateAll(Long productId,
                                                       List<ProductCharacteristicWriteDTO> productCharacteristicDTOs) {
        if (productCharacteristicDTOs.isEmpty()) return Collections.emptyList();
        productCharacteristicServiceValidator.validateUpdateNew(productId, productCharacteristicDTOs);

        List<ProductCharacteristic> productCharacteristics = findAllByProductId(productId);
        for(var pcDTO : productCharacteristicDTOs) {
            int index = findProductCharacteristicIndexById(productCharacteristics, pcDTO.getCharacteristicId());

            productCharacteristics.get(index).setNumericValue(pcDTO.getNumericValue());
            productCharacteristics.get(index).setTextValue(pcDTO.getTextValue());
        }

        productCharacteristicRepo.saveAllAndFlush(productCharacteristics);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTOnew> deleteAll(Long productId, List<Long> ids) {
        if (ids.isEmpty()) return Collections.emptyList();

        productCharacteristicRepo.deleteAllByProductIdAndCharacteristicIdIn(productId, ids);
        return findAll(productId);
    }
}