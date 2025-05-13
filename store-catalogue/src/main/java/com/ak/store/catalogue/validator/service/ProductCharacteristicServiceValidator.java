package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.write.ProductCharacteristicWriteDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProductCharacteristicServiceValidator {
    //todo заменить на сервис
    private final CharacteristicRepo characteristicRepo;
    private final ProductService productService;

    private void validateExistingProductCharacteristic(Product product, Set<ProductCharacteristicForm> productCharacteristicForms) {
        List<Long> existingCharacteristicIds = product.getCharacteristics().stream()
                .map(pc -> pc.getCharacteristic().getId())
                .toList();

        if(!existingCharacteristicIds.isEmpty()) {
            List<Long> creatingCharacteristicIds = productCharacteristicForms.stream()
                    .map(ProductCharacteristicForm::getId)
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

    private void validateProductCharacteristics(List<ProductCharacteristicWriteDTO> productCharacteristics, Long categoryId) {
        var availableTextValues = characteristicRepo.findAllWithTextValuesByCategoryId(categoryId).stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList())
                );

        for (var pc : productCharacteristics) {
            List<String> textValues = availableTextValues.get(pc.getCharacteristicId());

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

    //todo make validation
    public void validateCreationNew(Long productId, List<ProductCharacteristicWriteDTO> productCharacteristics) {
        Long categoryId = productService.findOne(productId).getCategoryId();
        validateProductCharacteristics(productCharacteristics, categoryId);
    }

    //todo make validation
    public void validateUpdateNew(Long productId, List<ProductCharacteristicWriteDTO> creatproductCharacteristics) {
        Long categoryId = productService.findOne(productId).getCategoryId();
        validateProductCharacteristics(productCharacteristics, categoryId);
        validateExistingProductCharacteristic(product, productCharacteristicForms);
    }
}