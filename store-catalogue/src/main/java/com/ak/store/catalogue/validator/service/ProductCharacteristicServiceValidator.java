package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.dto.write.ProductCharacteristicWriteDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProductCharacteristicServiceValidator {
    //todo заменить на сервис
    private final CharacteristicRepo characteristicRepo;
    private final ProductService productService;

    private void validateExistingProductCharacteristics(List<ProductCharacteristicWriteDTO> creatingProductCharacteristics,
                                                        List<ProductCharacteristicDTO> existingProductCharacteristics) {
        List<Long> existingCharacteristicIds = existingProductCharacteristics.stream()
                .map(ProductCharacteristicDTO::getCharacteristicId)
                .toList();

        if(!existingCharacteristicIds.isEmpty()) {
            List<Long> creatingCharacteristicIds = creatingProductCharacteristics.stream()
                    .map(ProductCharacteristicWriteDTO::getCharacteristicId)
                    .toList();

            Optional<Long> notUniqCharacteristicId = creatingCharacteristicIds.stream()
                    .filter(existingCharacteristicIds::contains)
                    .findFirst();

            if(notUniqCharacteristicId.isPresent()) {
                throw new RuntimeException("characteristic with id=%s already exists"
                        .formatted(notUniqCharacteristicId.get()));
            }
        }
    }

    private void validateNewProductCharacteristics(List<ProductCharacteristicWriteDTO> productCharacteristics, Long categoryId) {
        var textValueMap = characteristicRepo.findAllWithTextValuesByCategoryId(categoryId).stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList())
                );

        for (var pc : productCharacteristics) {
            List<String> textValues = textValueMap.get(pc.getCharacteristicId());

            if (textValues == null) {
                throw new RuntimeException("characteristic with id=%s is not available"
                        .formatted(pc.getCharacteristicId()));
            }

            if (pc.getTextValue() != null) {
                if (pc.getNumericValue() != null) {
                    throw new RuntimeException("characteristic with id=%s has both text value and numeric value"
                            .formatted(pc.getCharacteristicId()));
                }

                if (textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a text one"
                            .formatted(pc.getCharacteristicId()));
                }

                if (!textValues.contains(pc.getTextValue())) {
                    throw new RuntimeException("not valid text value for characteristic with id=%s"
                            .formatted(pc.getCharacteristicId()));
                }
            }

            if (pc.getNumericValue() != null) {
                if (!textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a numeric one"
                            .formatted(pc.getCharacteristicId()));
                }
            }
        }
    }

    //-------------------

    public void validateCreating(Long productId, List<ProductCharacteristicWriteDTO> creatingProductCharacteristics,
                                 List<ProductCharacteristicDTO> existingProductCharacteristics) {
        Long categoryId = productService.findOne(productId).getCategoryId();
        validateNewProductCharacteristics(creatingProductCharacteristics, categoryId);
        validateExistingProductCharacteristics(creatingProductCharacteristics, existingProductCharacteristics);
    }

    //todo make validation
    public void validateUpdating(Long productId, List<ProductCharacteristicWriteDTO> creatingProductCharacteristics) {
        Long categoryId = productService.findOne(productId).getCategoryId();
        validateNewProductCharacteristics(creatingProductCharacteristics, categoryId);
    }
}