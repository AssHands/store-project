package com.ak.store.user.facade;

import com.ak.store.user.model.command.WriteUserCommand;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.service.UserAuthService;
import com.ak.store.user.service.UserOutboxService;
import com.ak.store.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final UserAuthService userAuthService;
    private final UserOutboxService userOutboxService;

    public UserDTO findOne(UUID id) {
        return userService.findOne(id);
    }

    @Transactional
    public UserDTO updateOne(WriteUserCommand command) {
        return userService.updateOne(command);
    }

    public Boolean isExistOne(UUID id) {
        return userService.isExistOne(id);
    }

    @Transactional
    public UserDTO verifyOne(String code) {
        var user = userService.verifyOne(code);
        userAuthService.verifyOne(user.getId(), user.getEmail());
        return user;
    }

    public void registerOne(WriteUserCommand command) {
        UUID id = userAuthService.registerOne(command);
        userOutboxService.saveRegisterEvent(id);
    }

    @Transactional
    public void updateOneEmail(UUID id, String email) {
        userService.makeVerificationCode(id, email);
        userOutboxService.saveUpdatedEmailEvent(id);
    }
}