package com.ak.store.catalogue.service;


import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;

import java.util.List;

public interface ProductService {
    ProductFullReadDTO findOneById(Long id);

    //ProductDTO updateOneById(Long id, ProductPayload updatedProduct);

    void deleteOneProduct(Long id);

    ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest);

    SearchAvailableFiltersResponse facet(SearchAvailableFiltersRequest searchAvailableFiltersRequest);

    List<CategoryDTO> findAllCategory();

    List<CategoryDTO> findAllCategory(String name);

    Filters findAllAvailableCharacteristic(Long categoryId);

    void createOneProduct(ProductWritePayload productPayload);
    void createAllProduct(List<ProductWritePayload> productPayloads);

    boolean updateOneProduct(ProductWritePayload productPayload, Long productId);
}