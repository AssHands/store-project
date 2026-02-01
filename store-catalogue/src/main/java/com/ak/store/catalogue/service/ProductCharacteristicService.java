package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import com.ak.store.catalogue.validator.service.ProductCharacteristicServiceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return findAllByProductId(productId).stream()
                .map(productCharacteristicMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<ProductCharacteristicDTO> createAll(Long productId,
                                                    List<WriteProductCharacteristicCommand> productCharacteristics) {
        if (productCharacteristics.isEmpty()) return findAll(productId);
        productCharacteristicServiceValidator.validateCreate(productId, productCharacteristics);

        List<ProductCharacteristic> existingProductCharacteristics =
                productCharacteristicMapper.toEntity(productCharacteristics, productId);

        productCharacteristicRepo.saveAllAndFlush(existingProductCharacteristics);
        return findAll(productId);
    }

    @Transactional
    public List<ProductCharacteristicDTO> updateAll(Long productId,
                                                    List<WriteProductCharacteristicCommand> productCharacteristics) {
        if (productCharacteristics.isEmpty()) return findAll(productId);
        productCharacteristicServiceValidator.validateUpdate(productId, productCharacteristics);

        List<ProductCharacteristic> existing = findAllByProductId(productId);
        Map<Long, ProductCharacteristic> existingByCharacteristicId =
                existing.stream()
                        .collect(Collectors.toMap(
                                pc -> pc.getCharacteristic().getId(),
                                Function.identity()
                        ));

        for (var cmd : productCharacteristics) {
            ProductCharacteristic pc = existingByCharacteristicId.get(cmd.getCharacteristicId());

            pc.setNumericValue(cmd.getNumericValue());
            pc.setTextValue(cmd.getTextValue());
        }

        productCharacteristicRepo.saveAllAndFlush(existing);
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
}