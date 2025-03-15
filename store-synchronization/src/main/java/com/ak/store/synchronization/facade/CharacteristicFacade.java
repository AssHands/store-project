package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.synchronization.elastic.CharacteristicElasticService;
import com.ak.store.synchronization.util.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicElasticService characteristicElasticService;
    private final CharacteristicMapper characteristicMapper;

    public void createOne(CharacteristicDTO characteristic) {
        characteristicElasticService.createOne(
                characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void createAll(List<CharacteristicDTO> characteristics) {
        characteristicElasticService.createAll(
                characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void updateOne(CharacteristicDTO characteristic) {
        characteristicElasticService.updateOne(
                characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void updateAll(List<CharacteristicDTO> characteristics) {
        characteristicElasticService.updateAll(
                characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void deleteOne(Long id) {
        characteristicElasticService.deleteOne(id);
    }

    public void deleteAll(List<Long> ids) {
        characteristicElasticService.deleteAll(ids);
    }
}
