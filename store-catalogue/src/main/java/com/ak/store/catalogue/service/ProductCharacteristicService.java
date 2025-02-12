package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.validator.ProductCharacteristicValidator;
import com.ak.store.common.model.catalogue.dto.ProductCharacteristicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {

    private final CatalogueMapper catalogueMapper;
    private final CharacteristicRepo characteristicRepo;
    private final ProductCharacteristicValidator productCharacteristicValidator;

    public void createAll(Product updatedProduct, Set<ProductCharacteristicDTO> createCharacteristicsDTO) {
        if(createCharacteristicsDTO.isEmpty()) {
            return;
        }

        productCharacteristicValidator.validate(createCharacteristicsDTO,
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()));

        List<Long> existingCharacteristicIds = updatedProduct.getCharacteristics().stream()
                .map(pc -> pc.getCharacteristic().getId())
                .toList();

        if(!existingCharacteristicIds.isEmpty()) {
            List<Long> creatingCharacteristicIds = createCharacteristicsDTO.stream()
                    .map(ProductCharacteristicDTO::getId)
                    .toList();

            Optional<Long> notUniqCharacteristicId = creatingCharacteristicIds.stream()
                    .filter(existingCharacteristicIds::contains)
                    .findFirst();

            if(notUniqCharacteristicId.isPresent()) {
                throw new RuntimeException("characteristic with id=%s already exists"
                        .formatted(notUniqCharacteristicId.get()));
            }
        }

        List<ProductCharacteristic> createdCharacteristics = createCharacteristicsDTO.stream()
                .map(c -> catalogueMapper.mapToProductCharacteristic(c, updatedProduct))
                .toList();

        updatedProduct.addCharacteristics(createdCharacteristics);
    }

    public void updateAll(Product updatedProduct, Set<ProductCharacteristicDTO> updateCharacteristicsDTO) {
        if(updateCharacteristicsDTO.isEmpty()) {
            return;
        }

        productCharacteristicValidator.validate(updateCharacteristicsDTO,
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()));

        for(var characteristic : updateCharacteristicsDTO) {
            int index = findProductCharacteristicIndexById(
                    updatedProduct.getCharacteristics(), characteristic.getId());

            updatedProduct.getCharacteristics().get(index).setTextValue(characteristic.getTextValue());
            updatedProduct.getCharacteristics().get(index).setNumericValue(characteristic.getNumericValue());
        }
    }

    public void deleteAll(Product updatedProduct, Set<ProductCharacteristicDTO> deleteCharacteristicsDTO) {
        if(deleteCharacteristicsDTO.isEmpty()) {
            return;
        }

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