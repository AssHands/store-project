package com.ak.store.order.controller;

import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("{consumerId}")
    public List<OrderView> findAllOrder(@PathVariable Long consumerId) {
        return orderFacade.findAllByConsumerId(consumerId);
    }

    @PostMapping("{consumerId}")
    public void createOne(@PathVariable Long consumerId, @RequestBody OrderDTO orderDTO) {
        orderFacade.createOne(consumerId, orderDTO);
    }
}
