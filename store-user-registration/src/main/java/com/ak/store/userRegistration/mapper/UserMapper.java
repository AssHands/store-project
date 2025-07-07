package com.ak.store.userRegistration.mapper;

import com.ak.store.userRegistration.model.dto.write.UserWriteDTO;
import com.ak.store.userRegistration.model.form.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserWriteDTO toUserWriteDTO(UserForm u);
}
