package com.ak.store.order.repository;

import com.ak.store.order.feign.PaymentFeign;
import com.ak.store.order.model.feign.UserBalanceView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class RestUserBalanceRepo implements UserBalanceRepo {
    private final PaymentFeign paymentFeign;

    @Override
    public UserBalanceView findOneByUserId(UUID userId) {
        return paymentFeign.findOneUserBalanceByUserId(userId);
    }
}
