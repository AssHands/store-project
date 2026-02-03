package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.service.ProductCharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicFacade {
    private final ProductCharacteristicService pcService;

    public List<ProductCharacteristicDTO> findAll(Long id) {
        return pcService.findAll(id);
    }

    public Long updateAll(WriteProductCharacteristicPayloadCommand payloadCommand) {
        var pcList = pcService.updateAll(payloadCommand);
        //todo записывать в outbox
        return payloadCommand.getProductId();
    }
}
