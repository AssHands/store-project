package com.ak.store.warehouse.serivce;

import com.ak.store.common.model.order.dto.ProductAmountDTO;
import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import com.ak.store.warehouse.model.Warehouse;
import com.ak.store.warehouse.repostitory.WarehouseRepo;
import com.fasterxml.jackson.annotation.JacksonInject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepo warehouseRepo;

    public Warehouse findOneByProductId(Long productId) {
        return warehouseRepo.findOneByProductId(productId)
                .orElseThrow(() -> new RuntimeException("product is not exist in warehouse"));
    }

    public Boolean checkProductAmount(List<ProductAmountDTO> productCheckDTOList) {
        List<Long> ids = productCheckDTOList.stream()
                .map(ProductAmountDTO::getProductId)
                .toList();

        var warehouseMap = warehouseRepo.findAllByProductIdIn(ids).stream()
                .collect(Collectors.toMap(Warehouse::getProductId, Warehouse::getAmount));

        for (var product : productCheckDTOList) {
            Long productId = product.getProductId();
            Integer amount = product.getAmount();

            if (warehouseMap.get(productId) < amount) {
                return false;
            }
        }

        return true;
    }

    public void reserveAllProduct(List<ProductAmountDTO> orderProductList) {
        List<Long> ids = orderProductList.stream()
                .map(ProductAmountDTO::getProductId)
                .toList();

        var reserveProductMap = orderProductList.stream()
                .collect(Collectors.toMap(ProductAmountDTO::getProductId, ProductAmountDTO::getAmount));

        List<Warehouse> warehouseList = warehouseRepo.findAllLockedByProductIdIn(ids);

        for (var warehouse : warehouseList) {
            Long productId = warehouse.getProductId();
            Integer amount = warehouse.getAmount();

            amount -= reserveProductMap.get(productId);
            if (amount < 0) {
                throw new RuntimeException("not enough amount, product id=%d".formatted(productId));
            }
            warehouse.setAmount(amount);
        }

        warehouseRepo.saveAll(warehouseList);
    }

    public Integer getAmount(Long productId) {
        return findOneByProductId(productId).getAmount();
    }

    public Warehouse createProductWarehouse(Long productId) {
        return warehouseRepo.save(Warehouse.builder()
                .productId(productId)
                .build());
    }

    public Warehouse addAmount(Long productId, int addAmount) {
        Warehouse warehouse = findOneByProductId(productId);

        int amount = warehouse.getAmount();
        amount += addAmount;
        warehouse.setAmount(amount);

        return warehouseRepo.save(warehouse);
    }

    public Warehouse deleteAmount(Long productId, int deleteAmount) {
        Warehouse warehouse = findOneByProductId(productId);
        int amount = warehouse.getAmount();
        amount -= deleteAmount;

        if(amount < 0) {
            throw new RuntimeException("amount below zero");
        }

        warehouse.setAmount(amount);
        return warehouseRepo.save(warehouse);
    }
}