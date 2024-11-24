package com.ak.store.catalogue.service;


import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.common.payload.product.ProductSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;

public interface ProductService {
    ProductDTO findOneById(Long id);

    //ProductDTO updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest);
}
