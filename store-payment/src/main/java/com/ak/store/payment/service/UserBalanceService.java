package com.ak.store.payment.service;

import com.ak.store.payment.mapper.UserBalanceMapper;
import com.ak.store.payment.model.dto.UserBalanceDTO;
import com.ak.store.payment.model.entity.UserBalance;
import com.ak.store.payment.repository.UserBalanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserBalanceService {
    private final UserBalanceRepo userBalanceRepo;
    private final UserBalanceMapper userBalanceMapper;

    private UserBalance findOneById(UUID id) {
        return userBalanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public UserBalanceDTO findOne(UUID id) {
        return userBalanceMapper.toUserBalanceDTO(findOneById(id));
    }
}
