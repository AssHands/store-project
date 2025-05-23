package com.ak.store.user.mapper;

import com.ak.store.user.model.form.UserForm;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.model.entity.User;
import com.ak.store.user.model.view.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    User toUser(UserWriteDTO u);

    UserDTO toUserDTO(User u);

    UserWriteDTO toUserWriteDTO(UserForm u);

    UserView toUserView(UserDTO u);
}
