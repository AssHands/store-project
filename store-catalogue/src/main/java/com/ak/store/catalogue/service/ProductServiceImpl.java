package com.ak.store.catalogue.service;

import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.common.Response.ProductResponse;
import com.ak.store.common.payload.search.RequestPayload;
import com.ak.store.common.ProductServicePayload;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.catalogue.test.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepo productRepo;
    private final EsService esService;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepo productRepo, EsService esService) {
        this.productDao = productDao;
        this.productRepo = productRepo;
        this.esService = esService;
    }

    @Override
    public ProductDTO findOneById(Long id) {
        ProductDTO productDTO = productDao.findOneById(id);

        if(productDTO == null) {
            throw new RuntimeException("No Products found by id");
        }

        return productDTO;
    }

    @Override
    public ProductResponse findAllBySearch(RequestPayload requestPayload) {
        ProductServicePayload productServicePayload = esService.findAllDocument(requestPayload);

        if(productServicePayload.getIds().isEmpty()) {
            throw new RuntimeException("No documents found");
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDao.findAllByIds(productServicePayload.getIds()));
        productResponse.setSearchAfter(productServicePayload.getSearchAfter());

        return productResponse;
    }
}