package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.command.WriteCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteNumericValueCommand;
import com.ak.store.catalogue.model.command.WriteTextValueCommand;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.form.WriteCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteNumericValueForm;
import com.ak.store.catalogue.model.form.WriteTextValueForm;
import com.ak.store.catalogue.model.view.CharacteristicView;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    CharacteristicDTO toDTO(Characteristic entity);

    CharacteristicView toView(CharacteristicDTO dto);

    Characteristic toEntity(WriteCharacteristicCommand command);

    void updateEntity(WriteCharacteristicCommand command, @MappingTarget Characteristic entity);

    WriteCharacteristicCommand toWriteCommand(WriteCharacteristicForm form);

    CharacteristicSnapshot toSnapshot(CharacteristicDTO dto);

    //---------------------------

    NumericValueDTO toNumericValueDTO(NumericValue entity);

    NumericValueSnapshot toNumericValueSnapshot(NumericValueDTO dto);

    WriteNumericValueCommand toWriteNumericValueCommand(WriteNumericValueForm form);

    @Mapping(target = "characteristic.id", source = "characteristicId")
    NumericValue toNumericValue(WriteNumericValueCommand command, Long characteristicId);

    WriteTextValueCommand toWriteTextValueCommand(WriteTextValueForm form);

    @Mapping(target = "characteristic.id", source = "characteristicId")
    TextValue toTextValue(WriteTextValueCommand command, Long characteristicId);
}
