package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.service.ProductCharacteristicService;
import com.ak.store.catalogue.service.outbox.ProductOutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductCharacteristicFacade {
    private final ProductCharacteristicService pcService;
    private final ProductOutboxService productOutboxService;

    public List<ProductCharacteristicDTO> findAll(Long id) {
        return pcService.findAll(id);
    }

    @Transactional
    public Long updateAll(WriteProductCharacteristicPayloadCommand payloadCommand) {
        pcService.updateAll(payloadCommand);
        productOutboxService.saveUpdatedEvent(payloadCommand.getProductId());
        return payloadCommand.getProductId();
    }
}
