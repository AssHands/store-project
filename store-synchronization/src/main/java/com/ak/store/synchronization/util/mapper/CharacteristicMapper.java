package com.ak.store.synchronization.util.mapper;

import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.synchronization.model.document.CharacteristicDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    CharacteristicDocument toCharacteristicDocument(CharacteristicDTO characteristic);
    List<CharacteristicDocument> toCharacteristicDocument(List<CharacteristicDTO> characteristics);
}
