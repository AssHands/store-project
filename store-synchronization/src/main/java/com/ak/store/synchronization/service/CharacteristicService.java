package com.ak.store.synchronization.service;

import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.repository.postgres.CharacteristicRepo;
import com.ak.store.synchronization.mapper.CharacteristicMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicService {
    private final CharacteristicRepo characteristicRepo;
    private final CharacteristicMapper characteristicMapper;

    @Transactional
    public void createOne(CharacteristicSnapshotPayload request) {
        var characteristic = characteristicMapper.toCharacteristic(request);
        characteristicRepo.save(characteristic);
    }

    @Transactional
    public void updateOne(CharacteristicSnapshotPayload request) {
        var characteristic = characteristicMapper.toCharacteristic(request);
        characteristicRepo.save(characteristic);
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicRepo.deleteById(id);
    }
}
