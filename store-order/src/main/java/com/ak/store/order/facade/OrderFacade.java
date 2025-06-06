package com.ak.store.order.facade;

import com.ak.store.common.model.order.snapshot.OrderCreatedSnapshotPayload;
import com.ak.store.common.model.order.snapshot.OrderSnapshot;
import com.ak.store.common.model.user.snapshot.UserIdentitySnapshot;
import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.dto.UserAuthContext;
import com.ak.store.order.model.dto.write.OrderWriteDTO;
import com.ak.store.order.model.form.werehouse.ReserveInventoryForm;
import com.ak.store.order.outbox.OutboxEventService;
import com.ak.store.order.outbox.OutboxEventType;
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
    private final OutboxEventService outboxEventService;

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

        var snapshot = OrderCreatedSnapshotPayload.builder()
                .order(OrderSnapshot.builder()
                        .id(orderPayload.getOrder().getId())
                        .userId(authContext.getId())
                        .productAmount(request.getProductAmount())
                        .totalPrice(orderPayload.getOrder().getTotalPrice())
                        .build())
                .userIdentity(UserIdentitySnapshot.builder()
                        .id(authContext.getId())
                        .email(authContext.getEmail())
                        .build())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.ORDER_CREATED);
    }
}