package com.ak.store.catalogue.controller;

import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.payload.product.ProductWritePayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/catalogue")
public class CatalogueController {

    private final ProductService productService;

    @Autowired
    public CatalogueController(ProductService userService) {
        this.productService = userService;
    }

    @GetMapping("products/{id}")
    public ProductFullReadDTO getOneProductById(@PathVariable("id") Long id) {
        return productService.findOneById(id);
    }

    @DeleteMapping("products/{id}")
    public void deleteOneProductById(@PathVariable("id") Long id) {
        productService.deleteOneProduct(id);
    }

    @PostMapping("products")
    public ProductWriteDTO createOneProduct(@RequestBody @Valid ProductWritePayload productPayload) {
        productService.createOneProduct(productPayload);
        return null;
    }
    @PostMapping("products/t")
    public ProductWriteDTO createAllProduct(@RequestBody @Valid List<ProductWritePayload> productPayloads) {
        productService.createAllProduct(productPayloads);
        return null;
    }

    @PatchMapping("products/{id}")
    public ProductWriteDTO updateOneProduct(@RequestBody @Valid ProductWritePayload productPayload,
                                            @PathVariable("id") Long productId) {
        productService.updateOneProduct(productPayload, productId);
        return null;
    }

    @GetMapping("categories")
    public List<CategoryDTO> getAllCategory(@RequestParam(required = false) String name) {
        if(name != null)
            return productService.findAllCategory(name);

        return productService.findAllCategory();
    }

    @GetMapping("characteristics")
    public Filters getAllAvailableCharacteristic(@RequestParam Long categoryId) {
        return productService.findAllAvailableCharacteristic(categoryId);
    }
}