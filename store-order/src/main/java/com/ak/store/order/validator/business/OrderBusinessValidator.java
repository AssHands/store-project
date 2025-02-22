package com.ak.store.order.validator.business;

import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.feign.ConsumerFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderBusinessValidator {
    private final CatalogueFeign catalogueFeign;
    private final ConsumerFeign consumerFeign;

    public void validateCreation(Long consumerId, List<Long> productIds) {
        if(!catalogueFeign.availableAll(productIds)) {
            throw new RuntimeException("some of the product are not available");
        }

        if(!consumerFeign.existOne(consumerId)) {
            throw new RuntimeException("consumer not found");
        }
    }
}
