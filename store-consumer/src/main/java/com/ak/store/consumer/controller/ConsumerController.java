package com.ak.store.consumer.controller;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.validationGroup.Create;
import com.ak.store.consumer.facade.ConsumerServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/consumer/consumers")
public class ConsumerController {
    private final ConsumerServiceFacade consumerServiceFacade;

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) ConsumerDTO consumerDTO) {
        return consumerServiceFacade.createOne(consumerDTO);
    }

    @GetMapping("{id}")
    public ConsumerDTO findOne(@PathVariable Long id) {
        return consumerServiceFacade.findOne(id);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        consumerServiceFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody ConsumerDTO consumerDTO) {
        return consumerServiceFacade.updateOne(id, consumerDTO);
    }

    @GetMapping("exist/{id}")
    public Boolean existOne(@PathVariable Long id) {
        return consumerServiceFacade.existOne(id);
    }
}