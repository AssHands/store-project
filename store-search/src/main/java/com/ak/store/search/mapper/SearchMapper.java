package com.ak.store.search.mapper;

import com.ak.store.search.model.document.Product;
import com.ak.store.search.model.dto.ProductDTO;
import com.ak.store.search.model.dto.request.FilterSearchRequestDTO;
import com.ak.store.search.model.dto.request.ProductSearchRequestDTO;
import com.ak.store.search.model.dto.response.FilterSearchResponseDTO;
import com.ak.store.search.model.dto.response.ProductSearchResponseDTO;
import com.ak.store.search.model.form.request.FilterSearchRequestForm;
import com.ak.store.search.model.form.request.ProductSearchRequestForm;
import com.ak.store.search.model.view.response.FilterSearchResponseView;
import com.ak.store.search.model.view.response.ProductSearchResponseView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SearchMapper {
    ProductDTO toProductDTO(Product productDocument);

    ProductSearchRequestDTO toProductSearchRequestDTO(ProductSearchRequestForm psr);

    FilterSearchRequestDTO FilterSearchRequestDTO(FilterSearchRequestForm fsr);

    ProductSearchResponseView toProductSearchResponseView(ProductSearchResponseDTO psr);

    FilterSearchResponseView toFilterSearchResponseView(FilterSearchResponseDTO response);
}