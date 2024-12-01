package com.ak.store.catalogue.controller;

import com.ak.store.common.dto.catalogue.others.AvailableCharacteristicDTO;
import com.ak.store.common.dto.catalogue.others.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.dto.catalogue.product.ProductReadDTO;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.utils.ProductValidator;
import com.ak.store.common.payload.product.ProductWritePayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/catalogue")
public class CatalogueController {

    private final ProductService productService;
    private final ProductValidator productValidator;


    @Autowired
    public CatalogueController(ProductService userService, ProductValidator productValidator) {
        this.productService = userService;
        this.productValidator = productValidator;
    }

    @GetMapping("products/{id}")
    public ProductReadDTO getOneProductById(@PathVariable("id") Long id) {
        return productService.findOneById(id);
    }

    @DeleteMapping("products/{id}")
    public String deleteOneProductById(@PathVariable("id") Long id) {
        if(productService.deleteOneById(id))
            return "DELETED";

        return "NOT DELETED";
    }

    @PostMapping("products")
    public ProductWriteDTO createOneProduct(@RequestBody @Valid ProductWritePayload productPayload) {
        System.out.println(productPayload);
        productService.createOne(productPayload);
        return null;
    }

    @GetMapping("categories")
    public List<CategoryDTO> getAllCategory(@RequestParam(required = false) String name) {
        if(name != null)
            return productService.findAllCategory(name);

        return productService.findAllCategory();
    }

    @GetMapping("characteristics")
    public List<AvailableCharacteristicDTO> getAllAvailableCharacteristic(@RequestParam Long categoryId) {
        return productService.findAllAvailableCharacteristic(categoryId);
    }
}