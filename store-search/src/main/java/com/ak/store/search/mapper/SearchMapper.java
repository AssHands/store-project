package com.ak.store.search.mapper;

import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.model.document.Product;
import com.ak.store.search.model.dto.ProductDTO;
import com.ak.store.search.model.dto.response.SearchFilterDTO;
import com.ak.store.search.model.dto.response.SearchProductDTO;
import com.ak.store.search.model.form.SearchFilterForm;
import com.ak.store.search.model.form.SearchProductForm;
import com.ak.store.search.model.view.response.SearchFilterView;
import com.ak.store.search.model.view.response.SearchProductView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SearchMapper {
    ProductDTO toProductDTO(Product document);

    SearchProductCommand toSearchProductCommand(SearchProductForm form);

    SearchFilterCommand toSearchFilterCommand(UUID userId, SearchFilterForm form);

    SearchProductView toSearchProductView(SearchProductDTO dto);

    SearchFilterView toSearchFilterView(SearchFilterDTO dto);
}