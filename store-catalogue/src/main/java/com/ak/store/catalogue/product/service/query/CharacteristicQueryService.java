package com.ak.store.catalogue.product.service.query;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicQueryService {
    private final CharacteristicRepo characteristicRepo;

    public List<Characteristic> findAllWithValuesByIds(List<Long> ids) {
        return characteristicRepo.findAllWithValuesByIds(ids);
    }
}
