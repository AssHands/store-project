package com.ak.store.order.repository;

import com.ak.store.order.model.view.feign.UserBalanceView;

import java.util.UUID;

public interface UserBalanceRepo {
    UserBalanceView findOneByUserId(UUID userId);
}
