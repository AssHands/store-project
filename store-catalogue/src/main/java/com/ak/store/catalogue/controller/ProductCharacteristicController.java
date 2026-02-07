package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.ProductCharacteristicFacade;
import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.model.form.WriteProductCharacteristicPayloadForm;
import com.ak.store.catalogue.model.view.ProductCharacteristicView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products/characteristics")
public class ProductCharacteristicController {
    private final ProductCharacteristicFacade pcFacade;
    private final ProductCharacteristicMapper pcMapper;

    @GetMapping("{id}")
    public List<ProductCharacteristicView> findAll(@PathVariable Long id) {
        return pcFacade.findAll(id).stream()
                .map(pcMapper::toView)
                .toList();
    }

    @PostMapping("update")
    public Long updateAll(@RequestBody @Valid WriteProductCharacteristicPayloadForm payloadForm) {
        return pcFacade.updateAll(pcMapper.toWritePayloadCommand(payloadForm));
    }
}
