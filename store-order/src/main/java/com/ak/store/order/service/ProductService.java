package com.ak.store.order.service;

import com.ak.store.order.model.feign.ProductView;
import com.ak.store.order.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public Map<Long, Integer> getProductPriceMap(List<Long> ids) {
        return productRepo.findAllByIds(ids).stream()
                .collect(Collectors.toMap(
                        ProductView::getId,
                        ProductView::getCurrentPrice)
                );
    }
}

