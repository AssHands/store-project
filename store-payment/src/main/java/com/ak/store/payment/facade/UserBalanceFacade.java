package com.ak.store.payment.facade;

import com.ak.store.payment.model.dto.UserBalanceDTO;
import com.ak.store.payment.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserBalanceFacade {
    private final UserBalanceService userBalanceService;

    public UserBalanceDTO findOne(UUID id) {
        return userBalanceService.findOne(id);
    }
}
