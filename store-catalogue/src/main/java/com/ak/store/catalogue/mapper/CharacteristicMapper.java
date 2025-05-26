package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.dto.write.CharacteristicWriteDTO;
import com.ak.store.catalogue.model.dto.write.NumericValueWriteDTO;
import com.ak.store.catalogue.model.dto.write.TextValueWriteDTO;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.form.CharacteristicForm;
import com.ak.store.catalogue.model.form.NumericValueForm;
import com.ak.store.catalogue.model.form.TextValueForm;
import com.ak.store.catalogue.model.view.CharacteristicView;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshot;
import com.ak.store.common.model.catalogue.snapshot.NumericValueSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    CharacteristicDTO toCharacteristicDTO(Characteristic c);
    List<CharacteristicDTO> toCharacteristicDTO(List<Characteristic> c);

    CharacteristicView toCharacteristicView(CharacteristicDTO c);
    List<CharacteristicView> toCharacteristicView(List<CharacteristicDTO> c);

    Characteristic toCharacteristic(CharacteristicWriteDTO c);

    CharacteristicSnapshot toCharacteristicSnapshot(CharacteristicDTO c);

    CharacteristicWriteDTO toCharacteristicWriteDTO(CharacteristicForm c);

    NumericValueDTO toNumericValueDTO(NumericValue nv);
    List<NumericValueDTO> toNumericValueDTO(List<NumericValue> nv);

    NumericValueSnapshot toNumericValueSnapshot(NumericValueDTO nv);
    List<NumericValueSnapshot> toNumericValueSnapshot(List<NumericValueDTO> nv);

    NumericValueWriteDTO toNumericValueWriteDTO(NumericValueForm nv);

    @Mapping(target = "characteristic.id", source = "characteristicId")
    NumericValue toNumericValue(NumericValueWriteDTO nv, Long characteristicId);

    TextValueWriteDTO toTextValueWriteDTO(TextValueForm tv);

    @Mapping(target = "characteristic.id", source = "characteristicId")
    TextValue toTextValue(TextValueWriteDTO tv, Long characteristicId);
}
