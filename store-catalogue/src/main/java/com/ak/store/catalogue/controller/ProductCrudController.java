package com.ak.store.catalogue.controller;

import com.ak.store.common.dto.catalogue.AvailableCharacteristicDTO;
import com.ak.store.common.dto.catalogue.CategoryDTO;
import com.ak.store.common.dto.catalogue.ProductWriteDTO;
import com.ak.store.common.dto.catalogue.ProductReadDTO;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.utils.ProductValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/products")
public class ProductCrudController {

    private final ProductService productService;
    private final ProductValidator productValidator;


    @Autowired
    public ProductCrudController(ProductService userService, ProductValidator productValidator) {
        this.productService = userService;
        this.productValidator = productValidator;
    }

    @GetMapping("{id}")
    public ProductReadDTO getOneById(@PathVariable("id") Long id) {
        return productService.findOneById(id);
    }

    @DeleteMapping("{id}")
    public String deleteOneById(@PathVariable("id") Long id) {
        if(productService.deleteOneById(id))
            return "DELETED";

        return "NOT DELETED";
    }

    @PostMapping("create")
    public ProductWriteDTO createOne(@RequestBody @Valid ProductWriteDTO productWriteDTO) {

        return null;
    }

    @GetMapping("category")
    public List<CategoryDTO> getAllCategory(@RequestParam(required = false) String name) {
        if(name != null)
            return productService.findAllCategoryByName(name);

        return productService.findAllCategory();
    }

    @GetMapping("filter")
    public List<AvailableCharacteristicDTO> getAllAvailableFiltersByCategoryId(@RequestParam Long categoryId) {

        return productService.findAllAvailableCharacteristic(categoryId);
    }
}