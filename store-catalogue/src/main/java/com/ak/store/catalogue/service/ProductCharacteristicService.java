package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.util.mapper.CharacteristicMapper;
import com.ak.store.catalogue.validator.service.ProductCharacteristicServiceValidator;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {

    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicService characteristicService;
    private final ProductCharacteristicServiceValidator productCharacteristicServiceValidator;

    public void createAll(Product product, Set<ProductCharacteristicForm> ProductCharacteristicForms) {
        if(ProductCharacteristicForms.isEmpty()) return;
        productCharacteristicServiceValidator.validateCreation(ProductCharacteristicForms, product);

        List<ProductCharacteristic> productCharacteristics = new ArrayList<>();
        for(var productCharacteristic : ProductCharacteristicForms) {
            Characteristic characteristic = characteristicService.findOne(productCharacteristic.getId());
            productCharacteristics.add(
                    characteristicMapper.toProductCharacteristic(productCharacteristic, characteristic, product)
            );
        }

        product.addCharacteristics(productCharacteristics);
    }

    public void updateAll(Product product, Set<ProductCharacteristicForm> ProductCharacteristicForms) {
        if(ProductCharacteristicForms.isEmpty()) return;
        productCharacteristicServiceValidator.validateUpdate(ProductCharacteristicForms, product.getCategory().getId());

        for(var productCharacteristic : ProductCharacteristicForms) {
            int index = findProductCharacteristicIndexById(product.getCharacteristics(), productCharacteristic.getId());

            product.getCharacteristics().get(index).setTextValue(productCharacteristic.getTextValue());
            product.getCharacteristics().get(index).setNumericValue(productCharacteristic.getNumericValue());
        }
    }

    public void deleteAll(Product product, Set<ProductCharacteristicForm> ProductCharacteristicForms) {
        if(ProductCharacteristicForms.isEmpty()) return;

        for(var characteristic : ProductCharacteristicForms) {
            int index = findProductCharacteristicIndexById(product.getCharacteristics(), characteristic.getId());

            product.getCharacteristics().remove(index);
        }
    }

    private int findProductCharacteristicIndexById(List<ProductCharacteristic> characteristics, Long id) {
        int index = 0;
        for (var characteristic : characteristics) {
            if(characteristic.getCharacteristic().getId().equals(id))
                return index;
            index++;
        }
        throw new RuntimeException("characteristic with id=%s didn't find in your product".formatted(id));
    }
}