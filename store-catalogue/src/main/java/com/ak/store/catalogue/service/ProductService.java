package com.ak.store.catalogue.service;


import com.ak.store.common.dto.catalogue.product.AvailableCharacteristicValuesDTO;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.SearchAvailableFilters;

import java.util.List;

public interface ProductService {
    ProductFullReadDTO findOneById(Long id);

    //ProductDTO updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest);

    AvailableFiltersResponse facet(SearchAvailableFilters searchAvailableFilters);

    List<CategoryDTO> findAllCategory();

    List<CategoryDTO> findAllCategory(String name);

    List<AvailableCharacteristicValuesDTO> findAllAvailableCharacteristic(Long categoryId);

    void createOneProduct(ProductWritePayload productPayload);
    void createAllProduct(List<ProductWritePayload> productPayloads);

    boolean updateOneProduct(ProductWritePayload productPayload, Long productId);
}