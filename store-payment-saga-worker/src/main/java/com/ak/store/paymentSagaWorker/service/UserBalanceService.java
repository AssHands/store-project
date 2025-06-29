package com.ak.store.paymentSagaWorker.service;

import com.ak.store.paymentSagaWorker.model.entity.UserBalance;
import com.ak.store.paymentSagaWorker.repository.UserBalanceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserBalanceService {
    private final UserBalanceRepo userBalanceRepo;

    private UserBalance findOneById(UUID id) {
        return userBalanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void reserveFunds(UUID id, Integer sum) {
        var userBalance = findOneById(id);
        int newBalance = userBalance.getBalance() - sum;

        if (newBalance >= 0) {
            userBalance.setBalance(newBalance);
            userBalanceRepo.save(userBalance);
        } else {
            throw new RuntimeException("not enough balance");
        }
    }

    public void releaseFunds(UUID id, Integer sum) {
        var userBalance = findOneById(id);
        userBalance.setBalance(userBalance.getBalance() + sum);
        userBalanceRepo.save(userBalance);
    }
}
