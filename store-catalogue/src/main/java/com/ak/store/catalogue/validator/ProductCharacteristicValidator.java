package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.exception.ValidationException;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.IntRange;
import com.ak.store.catalogue.service.CharacteristicQueryService;
import com.ak.store.catalogue.service.ProductCharacteristicQueryService;
import com.ak.store.catalogue.service.ProductQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProductCharacteristicValidator {
    private final CharacteristicQueryService characteristicQueryService;
    private final ProductCharacteristicQueryService productCharacteristicQueryService;
    private final ProductQueryService productQueryService;

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
            throw new ValidationException("Cannot add and remove the same characteristics at the same time: [%s]"
                    .formatted(equalsIds));
        }
    }

    private void addingCharacteristicValuesValid(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var addingIds = payloadCommand.getAddCharacteristics().stream()
                .map(WriteProductCharacteristicCommand::getCharacteristicId)
                .toList();

        var characteristics = characteristicQueryService.findAllWithValuesByIds(addingIds);

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
                throw new ValidationException(
                        "Characteristic [%d]: value must be provided (text or numeric)".formatted(id)
                );
            }

            //проверяем, что не заданы оба значения одновременно
            if (textValue != null && numericValue != null) {
                throw new ValidationException(
                        "Characteristic [%d]: only one value type allowed (text or numeric)".formatted(id)
                );
            }

            //проверка текстового значения
            if (textValue != null) {
                var allowedTexts = textValueMap.get(id);
                if (allowedTexts == null || !allowedTexts.contains(textValue)) {
                    throw new ValidationException(
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
                    throw new ValidationException(
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
            throw new ValidationException("Characteristics [%s] are not allowed for this product category"
                    .formatted(forbiddenIds)
            );
        }
    }

    private void addingCharacteristicsNotExistInProduct(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var pcList = productCharacteristicQueryService.findAllByProductId(payloadCommand.getProductId());

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
            throw new ValidationException("adding characteristics with id [%s] already exist".formatted(duplicateIds));
        }
    }

    private void productExist(Long productId) {
        productQueryService.assertExists(productId);
    }

    private Product findOneProductWithCategoryCharacteristics(Long id) {
        return productQueryService.findOneWithCategoryCharacteristicsOrThrow(id);
    }
}