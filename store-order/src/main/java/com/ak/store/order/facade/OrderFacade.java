package com.ak.store.order.facade;

import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.common.model.warehouse.dto.ReserveProductDTO;
import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.service.OrderService;
import com.ak.store.order.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final WarehouseFeign warehouseFeign;

    public List<OrderView> findAllByConsumerId(Long consumerId) {
        return orderService.findAllByConsumerId(consumerId).stream()
                .map(orderMapper::mapToOrderView)
                .toList();
    }

    @Transactional
    public void createOne(Long consumerId, OrderDTO orderDTO) {
        orderService.createOne(consumerId, orderDTO);

        List<ReserveProductDTO> reserveProductDTOList = new ArrayList<>();
        for(var orderProduct : orderDTO.getProducts()) {
            reserveProductDTOList.add(new ReserveProductDTO(
                    orderProduct.getProductId(), orderProduct.getAmount()));
        }

        warehouseFeign.reserveAll(reserveProductDTOList);
    }
}