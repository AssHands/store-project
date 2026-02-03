package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.IntRange;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import com.ak.store.catalogue.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProductCharacteristicValidator {
    private final CharacteristicRepo characteristicRepo;
    private final ProductCharacteristicRepo pcRepo;
    private final ProductRepo productRepo;

    @Transactional
    public void validateUpdate(WriteProductCharacteristicPayloadCommand payloadCommand) {
        if(payloadCommand.getAddCharacteristics().isEmpty()) return;

        addingCharacteristicNotContainRemoving(payloadCommand);
        productExist(payloadCommand.getProductId());
        addingCharacteristicsNotExistInProduct(payloadCommand);
        addingCharacteristicsAllowedForCategory(payloadCommand);
        addingCharacteristicValuesValid(payloadCommand);
    }

    private void addingCharacteristicNotContainRemoving(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var addingIds = payloadCommand.getAddCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var removingIds = payloadCommand.getRemoveCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var equalsIds = addingIds.stream()
                .filter(removingIds::contains)
                .toList();

        if(!equalsIds.isEmpty()) {
            throw new RuntimeException("Cannot add and remove the same characteristics at the same time: [%s]"
                    .formatted(equalsIds));
        }
    }

    private void addingCharacteristicValuesValid(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var addingIds = payloadCommand.getAddCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var characteristics = characteristicRepo.findAllWithValuesByIds(addingIds);

        var textValueMap = characteristics.stream()
                .filter(Characteristic::getIsText)
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        c -> c.getTextValues().stream()
                                .map(TextValue::getTextValue)
                                .toList()
                ));

        var numericValueMap = characteristics.stream()
                .filter(c -> !c.getIsText())
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        c -> c.getNumericValues().stream()
                                .map(val -> new IntRange(val.getFromValue(), val.getToValue()))
                                .toList()
                ));

        for (var command : payloadCommand.getAddCharacteristics()) {
            Long id = command.getCharacteristicId();
            String textValue = command.getTextValue();
            Integer numericValue = command.getNumericValue();

            //проверяем, что хотя бы одно значение задано
            if (textValue == null && numericValue == null) {
                throw new RuntimeException(
                        "Characteristic [%d]: value must be provided (text or numeric)".formatted(id)
                );
            }

            //проверяем, что не заданы оба значения одновременно
            if (textValue != null && numericValue != null) {
                throw new RuntimeException(
                        "Characteristic [%d]: only one value type allowed (text or numeric)".formatted(id)
                );
            }

            //проверка текстового значения
            if (textValue != null) {
                var allowedTexts = textValueMap.get(id);
                if (allowedTexts == null || !allowedTexts.contains(textValue)) {
                    throw new RuntimeException(
                            "Characteristic [%d]: text value '%s' is not allowed".formatted(id, textValue)
                    );
                }
            }

            //проверка числового значения
            if (numericValue != null) {
                var allowedRanges = numericValueMap.get(id);
                if (allowedRanges == null || allowedRanges.stream().noneMatch(range ->
                        numericValue >= range.from() && numericValue <= range.to()
                )) {
                    throw new RuntimeException(
                            "Characteristic [%d]: numeric value %d is not allowed".formatted(id, numericValue)
                    );
                }
            }
        }
    }

    private void addingCharacteristicsAllowedForCategory(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var product = findOneProductWithCategoryCharacteristics(payloadCommand.getProductId());

        var allowedCharacteristicIds = product.getCategory().getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .toList();

        var addCharacteristicIds = payloadCommand.getAddCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var forbiddenIds = addCharacteristicIds.stream()
                .filter(id -> !allowedCharacteristicIds.contains(id))
                .toList();

        if (!forbiddenIds.isEmpty()) {
            throw new RuntimeException("Characteristics [%s] are not allowed for this product category"
                    .formatted(forbiddenIds)
            );
        }
    }

    private void addingCharacteristicsNotExistInProduct(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var pcList = pcRepo.findAllByProductId(payloadCommand.getProductId());

        if (pcList.isEmpty()) return;

        var characteristicIds = pcList.stream()
                .map(ProductCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .toList();

        var addCharacteristicIds = payloadCommand.getAddCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var duplicateIds = characteristicIds.stream()
                .filter(addCharacteristicIds::contains)
                .toList();


        if(!duplicateIds.isEmpty()) {
            throw new RuntimeException("adding characteristics with id [%s] already exist".formatted(duplicateIds));
        }
    }

    private void productExist(Long productId) {
        productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("product does not exist"));
    }

    private Product findOneProductWithCategoryCharacteristics(Long id) {
        return productRepo.findOneWithCategoryCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}