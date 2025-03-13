package com.ak.store.catalogue.validator.business;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProductCharacteristicBusinessValidator {
    private final CharacteristicRepo characteristicRepo;

    public void validateCreation(Set<ProductCharacteristicForm> productCharacteristicForms, Product product) {
        validateProductCharacteristics(productCharacteristicForms, product.getCategory().getId());
        validateExistingProductCharacteristic(product, productCharacteristicForms);
    }

    public void validateUpdate(Set<ProductCharacteristicForm> productCharacteristicForms, Long categoryId) {
        validateProductCharacteristics(productCharacteristicForms, categoryId);
    }

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

    private void validateProductCharacteristics(Set<ProductCharacteristicForm> productCharacteristicForms, Long categoryId) {
        var availableTextValues = characteristicRepo.findAllWithTextValuesByCategoryId(categoryId).stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList())
                );

        for (var productCharacteristic : productCharacteristicForms) {
            List<String> textValues = availableTextValues.get(productCharacteristic.getId());

            if (textValues == null) {
                throw new RuntimeException("characteristic with id=%s is not available"
                        .formatted(productCharacteristic.getId()));
            }

            if (productCharacteristic.getTextValue() != null) {
                if (productCharacteristic.getNumericValue() != null) {
                    throw new RuntimeException("characteristic with id=%s has both text value and numeric value"
                            .formatted(productCharacteristic.getId()));
                }

                if (textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a text one"
                            .formatted(productCharacteristic.getId()));
                }

                if (!textValues.contains(productCharacteristic.getTextValue())) {
                    throw new RuntimeException("not valid text value for characteristic with id=%s"
                            .formatted(productCharacteristic.getId()));
                }
            }

            if (productCharacteristic.getNumericValue() != null) {
                if (!textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a numeric one"
                            .formatted(productCharacteristic.getId()));
                }
            }
        }
    }
}