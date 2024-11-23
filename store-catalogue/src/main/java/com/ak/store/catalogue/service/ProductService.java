package com.ak.store.catalogue.service;


import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.common.Response.ProductResponse;
import com.ak.store.common.payload.search.RequestPayload;

public interface ProductService {
    ProductDTO findOneById(Long id);

    //ProductDTO updateOneById(Long id, ProductPayload updatedProduct);

    //boolean deleteOneById(Long id);

    ProductResponse findAllBySearch(RequestPayload requestPayload);
}
