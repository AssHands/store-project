package com.ak.store.user.mapper;

import com.ak.store.user.model.command.WriteUserCommand;
import com.ak.store.user.model.form.UserForm;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.entity.User;
import com.ak.store.user.model.view.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserDTO toDTO(User entity);

    WriteUserCommand toWriteCommand(UUID userId, UserForm form);

    UserView toView(UserDTO dto);
}
