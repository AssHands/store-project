package com.ak.store.catalogue.characteristic.facade;

import com.ak.store.catalogue.model.command.WriteCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteNumericValueCommand;
import com.ak.store.catalogue.model.command.WriteTextValueCommand;
import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.characteristic.service.CharacteristicService;
import com.ak.store.catalogue.characteristic.service.CharacteristicOutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicService characteristicService;
    private final CharacteristicOutboxService characteristicOutboxService;

    public List<CharacteristicDTO> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Long createOne(WriteCharacteristicCommand command) {
        var characteristic = characteristicService.createOne(command);
        characteristicOutboxService.saveCreatedEvent(characteristic.getId());
        return characteristic.getId();
    }

    @Transactional
    public Long updateOne(WriteCharacteristicCommand command) {
        var characteristic = characteristicService.updateOne(command);
        characteristicOutboxService.saveUpdatedEvent(characteristic.getId());
        return characteristic.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicService.deleteOne(id);
        characteristicOutboxService.saveDeletedEvent(id);
    }

    @Transactional
    public Long addOneNumericValue(WriteNumericValueCommand command) {
        var characteristic = characteristicService.addOneNumericValue(command);
        characteristicOutboxService.saveUpdatedEvent(characteristic.getId());
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneNumericValue(WriteNumericValueCommand command) {
        var characteristic = characteristicService.removeOneNumericValue(command);
        characteristicOutboxService.saveUpdatedEvent(characteristic.getId());
        return characteristic.getId();
    }

    @Transactional
    public Long addOneTextValue(WriteTextValueCommand command) {
        var characteristic = characteristicService.addOneTextValue(command);
        characteristicOutboxService.saveUpdatedEvent(characteristic.getId());
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneTextValue(WriteTextValueCommand command) {
        var characteristic = characteristicService.removeOneTextValue(command);
        characteristicOutboxService.saveUpdatedEvent(characteristic.getId());
        return characteristic.getId();
    }
}