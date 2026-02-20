package com.ak.store.catalogue.product.controller;

import com.ak.store.catalogue.product.facade.ProductFacade;
import com.ak.store.catalogue.product.mapper.ProductMapper;
import com.ak.store.catalogue.model.form.WriteProductForm;
import com.ak.store.catalogue.model.validationGroup.Create;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.ak.store.catalogue.model.view.ProductView;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products")
public class ProductController {
    private final ProductFacade productFacade;
    private final ProductMapper productMapper;

    @GetMapping("{id}")
    public ProductView findOne(@PathVariable Long id) {
        return productMapper.toView(productFacade.findOne(id));
    }

    @PostMapping("find")
    public List<ProductView> findAll(@RequestBody List<Long> ids) {
        return productFacade.findAll(ids).stream()
                .map(productMapper::toView)
                .toList();
    }

    @PostMapping("available")
    public Boolean isAvailableAll(@RequestBody List<Long> ids) {
        return productFacade.isAvailableAll(ids);
    }

    @PostMapping("exist")
    public Boolean isExistAll(@RequestBody List<Long> ids) {
        return productFacade.isExistAll(ids);
    }

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) WriteProductForm form) {
        return productFacade.createOne(productMapper.toWriteCommand(form));
    }

    @PatchMapping("update")
    public Long updateOne(@RequestBody @Validated(Update.class) WriteProductForm form) {
        return productFacade.updateOne(productMapper.toWriteCommand(form));
    }

    @DeleteMapping("{id}")
    public Long deleteOne(@PathVariable Long id) {
        return productFacade.deleteOne(id);
    }
}
