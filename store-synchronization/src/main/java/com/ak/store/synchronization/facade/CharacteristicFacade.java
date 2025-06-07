package com.ak.store.synchronization.facade;

import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.service.CharacteristicRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicRedisService characteristicRedisService;

    public void createOne(CharacteristicSnapshotPayload request) {
        characteristicRedisService.createOne(request);
    }

    public void updateOne(CharacteristicSnapshotPayload request) {
        characteristicRedisService.updateOne(request);
    }

    public void deleteOne(Long id) {
        characteristicRedisService.deleteOne(id);
    }
}
