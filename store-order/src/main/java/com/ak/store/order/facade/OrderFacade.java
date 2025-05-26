package com.ak.store.order.facade;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.kafka.OrderProducerKafka;
import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.dto.UserAuthContext;
import com.ak.store.order.model.dto.write.OrderWriteDTO;
import com.ak.store.order.model.form.werehouse.ReserveInventoryForm;
import com.ak.store.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final WarehouseFeign warehouseFeign;
    private final OrderProducerKafka orderProducerKafka;

    public List<OrderDTOPayload> findAllByUserId(UUID userId) {
        return orderService.findAllByUserId(userId);
    }

    @Transactional
    public void createOne(UserAuthContext authContext, OrderWriteDTO request) {
        var orderPayload = orderService.createOne(authContext.getId(), request);

        List<ReserveInventoryForm> reserveForm = new ArrayList<>();
        for (var entry : request.getProductAmount().entrySet()) {
            reserveForm.add(ReserveInventoryForm.builder()
                    .productId(entry.getKey())
                    .amount(entry.getValue())
                    .build());
        }

        warehouseFeign.reserveAll(reserveForm);

        var orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(orderPayload.getOrder().getId())
                .userEmail(authContext.getEmail())
                .productAmount(request.getProductAmount())
                .totalPrice(orderPayload.getOrder().getTotalPrice())
                .build();

        orderProducerKafka.send(orderCreatedEvent, orderPayload.getOrder().getId().toString());
    }
}