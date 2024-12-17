package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.dto.catalogue.product.AvailableCharacteristicValuesDTO;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.utils.CatalogueValidator;
import com.ak.store.common.payload.product.ProductWritePayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/catalogue")
public class CatalogueController {

    private final ProductService productService;
    private final CatalogueValidator catalogueValidator;
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final CharacteristicRepo characteristicRepo;

    private final CatalogueMapper catalogueMapper;


    @Autowired
    public CatalogueController(ProductService userService, CatalogueValidator catalogueValidator, ProductRepo productRepo, CategoryRepo categoryRepo, CharacteristicRepo characteristicRepo, CatalogueMapper catalogueMapper) {
        this.productService = userService;
        this.catalogueValidator = catalogueValidator;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.characteristicRepo = characteristicRepo;
        this.catalogueMapper = catalogueMapper;
    }

    @GetMapping("products/{id}")
    public ProductFullReadDTO getOneProductById(@PathVariable("id") Long id) {
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
    public List<AvailableCharacteristicValuesDTO> getAllAvailableCharacteristic(@RequestParam Long categoryId) {
        return productService.findAllAvailableCharacteristic(categoryId);
    }

    @GetMapping("test")
    public Object test() {
        var a = productRepo.findAll().stream().map(catalogueMapper::mapToProductFullReadDTO).toList();

        return a;
    }

    @GetMapping("testy")
    public Object testy() {
        //var a = productRepo.findAllById(List.of(2L, 26L, 31L, 0L)).stream().map(catalogueMapper::mapToProductViewReadDTO);
        var a = characteristicRepo.findAllValuesByCategoryId(2L).stream().map(catalogueMapper::mapToAvailableFilterValuesDTO).toList();

        return a;
    }
}