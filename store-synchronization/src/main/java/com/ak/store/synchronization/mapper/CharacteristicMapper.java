package com.ak.store.synchronization.mapper;

import com.ak.store.common.document.catalogue.CharacteristicDocument;
import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    @Mapping(target = "id", source = "characteristic.id")
    @Mapping(target = "name", source = "characteristic.name")
    @Mapping(target = "isText", source = "characteristic.isText")
    CharacteristicDocument toCharacteristicDocument(CharacteristicSnapshotPayload csp);

    List<CharacteristicDocument> toCharacteristicDocument(List<CharacteristicSnapshotPayload> csp);
}