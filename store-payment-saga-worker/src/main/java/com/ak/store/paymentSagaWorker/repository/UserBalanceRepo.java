package com.ak.store.paymentSagaWorker.repository;

import com.ak.store.paymentSagaWorker.model.user.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserBalanceRepo extends JpaRepository<UserBalance, UUID> {
}
