package com.ak.store.order.feign;

import com.ak.store.order.model.view.feign.UserBalanceView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "payment", url = "http://localhost:8093/api/v1/payment")
public interface PaymentFeign {
    @GetMapping
    UserBalanceView findOneUserBalanceByUserId(@RequestBody UUID userId);
}