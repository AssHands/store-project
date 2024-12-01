package com.ak.store.catalogue.service;


import com.ak.store.common.dto.catalogue.others.AvailableCharacteristicDTO;
import com.ak.store.common.dto.catalogue.others.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductReadDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.SearchAvailableFilters;

import java.util.List;

public interface ProductService {
    ProductReadDTO findOneById(Long id);

    //ProductDTO updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest);

    AvailableFiltersResponse facet(SearchAvailableFilters searchAvailableFilters);

    List<CategoryDTO> findAllCategory();

    List<CategoryDTO> findAllCategory(String name);

    List<AvailableCharacteristicDTO> findAllAvailableCharacteristic(Long categoryId);

    boolean createOne(ProductWritePayload ProductPayload);
}