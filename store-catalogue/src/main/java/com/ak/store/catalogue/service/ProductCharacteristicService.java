package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.validator.ProductCharacteristicValidator;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCharacteristicService {

    private final CatalogueMapper catalogueMapper;
    private final CharacteristicRepo characteristicRepo;
    private final ProductCharacteristicValidator productCharacteristicValidator;

    public void addProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> createCharacteristicsDTO) {
        if(createCharacteristicsDTO.isEmpty()) {
            return;
        }

        Map<Long, List<String>> availableCharacteristics =
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()).stream()
                        .collect(Collectors.toMap(
                                        Characteristic::getId,
                                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()
                                )
                        );
        productCharacteristicValidator.validate(createCharacteristicsDTO, availableCharacteristics);

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
                throw new RuntimeException("Characteristic with id=%s already exists"
                        .formatted(notUniqCharacteristicId.get()));
            }
        }

        List<ProductCharacteristic> createdCharacteristics = createCharacteristicsDTO.stream()
                .map(c -> catalogueMapper.mapToProductCharacteristic(c, updatedProduct))
                .toList();

        updatedProduct.addCharacteristics(createdCharacteristics);
    }

    public void updateProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> updateCharacteristicsDTO) {
        if(updateCharacteristicsDTO.isEmpty()) {
            return;
        }

        Map<Long, List<String>> availableCharacteristics =
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()).stream()
                        .collect(Collectors.toMap(
                                        Characteristic::getId,
                                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()
                                )
                        );
        productCharacteristicValidator.validate(updateCharacteristicsDTO, availableCharacteristics);

        for(var characteristic : updateCharacteristicsDTO) {
            int index = findProductCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1) {
                updatedProduct.getCharacteristics().get(index).setTextValue(characteristic.getTextValue());
                updatedProduct.getCharacteristics().get(index).setNumericValue(characteristic.getNumericValue());
            }
        }
    }

    public void deleteProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> deleteCharacteristicsDTO) {
        if(deleteCharacteristicsDTO.isEmpty()) {
            return;
        }

        for(var characteristic : deleteCharacteristicsDTO) {
            int index = findProductCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1)
                updatedProduct.getCharacteristics().remove(index);
        }
    }

    private int findProductCharacteristicIndexById(List<ProductCharacteristic> characteristics, Long id) {
        for (int i = 0; i < characteristics.size(); i++) {
            if(characteristics.get(i).getCharacteristic().getId().equals(id))
                return i;
        }
        return -1;
    }
}
