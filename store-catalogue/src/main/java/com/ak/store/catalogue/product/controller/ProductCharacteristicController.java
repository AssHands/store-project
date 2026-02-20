package com.ak.store.catalogue.product.controller;

import com.ak.store.catalogue.product.facade.ProductCharacteristicFacade;
import com.ak.store.catalogue.product.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.model.form.WriteProductCharacteristicPayloadForm;
import com.ak.store.catalogue.model.view.ProductCharacteristicView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products/{productId}/characteristics")
public class ProductCharacteristicController {
    private final ProductCharacteristicFacade pcFacade;
    private final ProductCharacteristicMapper pcMapper;

    @GetMapping
    public List<ProductCharacteristicView> findAll(@PathVariable Long productId) {
        return pcFacade.findAll(productId).stream()
                .map(pcMapper::toView)
                .toList();
    }

    @PatchMapping
    public Long updateAll(@PathVariable Long productId,
                          @RequestBody @Valid WriteProductCharacteristicPayloadForm payloadForm) {
        payloadForm.setProductId(productId);
        return pcFacade.updateAll(pcMapper.toWritePayloadCommand(payloadForm));
    }
}
