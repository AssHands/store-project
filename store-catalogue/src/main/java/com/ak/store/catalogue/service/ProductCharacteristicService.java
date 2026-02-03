package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.ProductCharacteristicRepo;
import com.ak.store.catalogue.validator.ProductCharacteristicValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicService {
    private final ProductCharacteristicMapper pcMapper;
    private final ProductCharacteristicValidator pcValidator;
    private final ProductCharacteristicRepo pcRepo;

    private List<ProductCharacteristic> findAllByProductId(Long productId) {
        return pcRepo.findAllByProductId(productId);
    }

    public List<ProductCharacteristicDTO> findAll(Long productId) {
        return findAllByProductId(productId).stream()
                .map(pcMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<ProductCharacteristicDTO> updateAll(WriteProductCharacteristicPayloadCommand payloadCommand) {
        pcValidator.validateUpdate(payloadCommand);

        addAll(payloadCommand);
        deleteAll(payloadCommand);

        return findAll(payloadCommand.getProductId());
    }

    private void addAll(WriteProductCharacteristicPayloadCommand payloadCommand) {
        if (payloadCommand.getAddCharacteristics().isEmpty()) return;

        var addingPc = new ArrayList<ProductCharacteristic>();
        for (var command : payloadCommand.getAddCharacteristics()) {
            addingPc.add(
                    ProductCharacteristic.builder()
                            .characteristic(Characteristic.builder()
                                    .id(command.getCharacteristicId())
                                    .build())
                            .product(Product.builder()
                                    .id(payloadCommand.getProductId())
                                    .build())
                            .numericValue(command.getNumericValue())
                            .textValue(command.getTextValue())
                            .build()
            );
        }

        pcRepo.saveAllAndFlush(addingPc);
    }

    private void deleteAll(WriteProductCharacteristicPayloadCommand payloadCommand) {
        if (payloadCommand.getRemoveCharacteristics().isEmpty()) return;

        pcRepo.deleteAllByProductIdAndCharacteristicIdIn(
                payloadCommand.getProductId(),
                payloadCommand.getRemoveCharacteristics().stream()
                        .map(WriteProductCharacteristicCommand::getCharacteristicId)
                        .toList()
        );
    }
}