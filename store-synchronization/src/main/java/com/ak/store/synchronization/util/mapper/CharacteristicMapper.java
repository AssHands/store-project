package com.ak.store.synchronization.util.mapper;

import com.ak.store.common.document.CharacteristicDocument;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    CharacteristicDocument toCharacteristicDocument(CharacteristicDTO characteristic);
    List<CharacteristicDocument> toCharacteristicDocument(List<CharacteristicDTO> characteristic);
}
