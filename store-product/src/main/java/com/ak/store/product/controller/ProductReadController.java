package com.ak.store.product.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.ak.store.common.dto.search.RequestPayload;
import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;
import com.ak.store.product.jdbc.ProductDao;
import com.ak.store.product.service.EsService;
import com.ak.store.product.service.ProductService;
import com.ak.store.product.test.ProductRepoES;
import com.ak.store.product.utils.ProductValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products/get")
public class ProductReadController {

    private final ProductService productService;
    private final ProductDao productDao;
    private final ProductValidator productValidator;
    private final ProductRepoES productRepoES;

    private final EsService esService;

    @Autowired
    public ProductReadController(ProductService userService, ProductDao userDao, ProductValidator productValidator,
                                 ProductRepoES productRepoES, EsService esService) {
        this.productService = userService;
        this.productDao = userDao;
        this.productValidator = productValidator;
        this.productRepoES = productRepoES;
        this.esService = esService;
    }

    @GetMapping("full")
    public List<ProductDTO> getAllFull(@RequestParam(defaultValue = "price ASC") String sort,
                                   @RequestParam(defaultValue = "0") @Min(0) int offset,
                                   @RequestParam(defaultValue = "18") @Min(1) @Max(100) int limit,
                                   @RequestBody(required = false) Map<String, String> filters) {
        if (productValidator.validateSortAndFilters(sort, filters)) {
            return null;
        }

        return productService.findAll(sort, offset, limit, filters, ProductFullDTO.class);
    }

    @GetMapping("full/{id}")
    public ProductDTO getOneByIdFull(@PathVariable("id") Long id) {
        return productService.findOneById(id, ProductFullDTO.class);
    }

    @GetMapping("preview")
    public List<ProductDTO> getAllPreview(@RequestParam(defaultValue = "price ASC") String sort,
                                          @RequestParam(defaultValue = "0") @Min(0) int offset,
                                          @RequestParam(defaultValue = "18") @Min(1) @Max(100) int limit,
                                          @RequestBody(required = false) Map<String, String> filters) {
        if (productValidator.validateSortAndFilters(sort, filters)) {
            return null;
        }

        return productService.findAll(sort, offset, limit, filters, ProductPreviewDTO.class);
    }

    @GetMapping("preview/{id}")
    public ProductDTO getOneByIdPreview(@PathVariable("id") Long id) {
        return productService.findOneById(id, ProductPreviewDTO.class);
    }

    @GetMapping("test")
    public void test(@RequestBody @Valid RequestPayload requestPayload) throws IOException {
        System.out.println(requestPayload);
        esService.getAllDocument(requestPayload);
    }
}