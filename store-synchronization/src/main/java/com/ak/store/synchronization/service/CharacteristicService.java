package com.ak.store.synchronization.service;

import com.ak.store.synchronization.mapper.CharacteristicMapper;
import com.ak.store.synchronization.model.command.characteristic.WriteCharacteristicPayloadCommand;
import com.ak.store.synchronization.repository.CharacteristicRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicService {
    private final CharacteristicRepo characteristicRepo;
    private final CharacteristicMapper characteristicMapper;

    @Transactional
    public void createOne(WriteCharacteristicPayloadCommand command) {
        characteristicRepo.save(characteristicMapper.toDocument(command));
    }

    @Transactional
    public void updateOne(WriteCharacteristicPayloadCommand command) {
        characteristicRepo.save(characteristicMapper.toDocument(command));
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicRepo.deleteById(id);
    }
}
