package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.validator.business.ProductCharacteristicBusinessValidator;
import com.ak.store.common.model.catalogue.dto.ProductCharacteristicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {

    private final CatalogueMapper catalogueMapper;
    private final ProductCharacteristicBusinessValidator productCharacteristicBusinessValidator;

    public void createAll(Product updatedProduct, Set<ProductCharacteristicDTO> createCharacteristicsDTO) {
        if(createCharacteristicsDTO.isEmpty()) return;
        productCharacteristicBusinessValidator.validateCreation(createCharacteristicsDTO, updatedProduct);

        List<ProductCharacteristic> createdCharacteristics = createCharacteristicsDTO.stream()
                .map(c -> catalogueMapper.mapToProductCharacteristic(c, updatedProduct))
                .toList();

        updatedProduct.addCharacteristics(createdCharacteristics);
    }

    public void updateAll(Product updatedProduct, Set<ProductCharacteristicDTO> updateCharacteristicsDTO) {
        if(updateCharacteristicsDTO.isEmpty()) return;
        productCharacteristicBusinessValidator.validateUpdate
                (updateCharacteristicsDTO, updatedProduct.getCategory().getId());

        for(var characteristic : updateCharacteristicsDTO) {
            int index = findProductCharacteristicIndexById(
                    updatedProduct.getCharacteristics(), characteristic.getId());

            updatedProduct.getCharacteristics().get(index).setTextValue(characteristic.getTextValue());
            updatedProduct.getCharacteristics().get(index).setNumericValue(characteristic.getNumericValue());
        }
    }

    public void deleteAll(Product updatedProduct, Set<ProductCharacteristicDTO> deleteCharacteristicsDTO) {
        if(deleteCharacteristicsDTO.isEmpty()) return;

        for(var characteristic : deleteCharacteristicsDTO) {
            int index = findProductCharacteristicIndexById(
                    updatedProduct.getCharacteristics(), characteristic.getId());

            updatedProduct.getCharacteristics().remove(index);
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