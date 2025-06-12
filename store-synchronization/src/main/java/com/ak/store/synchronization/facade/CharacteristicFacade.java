package com.ak.store.synchronization.facade;

import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.service.CharacteristicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicService characteristicService;

    @Transactional
    public void createOne(CharacteristicSnapshotPayload request) {
        characteristicService.createOne(request);
    }

    @Transactional
    public void updateOne(CharacteristicSnapshotPayload request) {
        characteristicService.updateOne(request);
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicService.deleteOne(id);
    }
}