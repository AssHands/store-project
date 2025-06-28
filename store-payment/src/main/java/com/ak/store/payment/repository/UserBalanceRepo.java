package com.ak.store.payment.repository;

import com.ak.store.payment.model.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserBalanceRepo extends JpaRepository<UserBalance, UUID> {
}
