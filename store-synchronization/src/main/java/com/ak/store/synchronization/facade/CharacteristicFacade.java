package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.service.CharacteristicRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicRedisService characteristicRedisService;

    public void createAll(List<CharacteristicSnapshotPayload> request) {
        characteristicRedisService.createAll(request);
    }

    public void updateAll(List<CharacteristicSnapshotPayload> request) {
        characteristicRedisService.updateAll(request);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisService.deleteAll(ids);
    }
}
