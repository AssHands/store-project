package com.ak.store.search.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.ak.store.search.model.common.ProductFields;
import com.ak.store.search.model.dto.request.FilterSearchRequestDTO;
import com.ak.store.search.model.dto.NumericFilterDTO;
import com.ak.store.search.model.dto.request.ProductSearchRequestDTO;
import com.ak.store.search.model.dto.TextFilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilterBuilder {
    public List<Query> buildNumericFilters(List<NumericFilterDTO> filters, Long characteristicId) {
        List<Query> allFilters = new ArrayList<>();

        for (var filter : filters) {
            List<RangeQuery> rangeList = new ArrayList<>();

            if (filter.getId() != null && filter.getId().equals(characteristicId)) {
                continue;
            }

            for (var numericValue : filter.getValues()) {
                rangeList.add(RangeQuery.of(r -> r
                        .field(ProductFields.Characteristics.NUMERIC_VALUE)
                        .gte(JsonData.of(numericValue.getFromValue()))
                        .lte(JsonData.of(numericValue.getToValue()))));
            }

            allFilters.add(NestedQuery.of(n -> n
                            .path(ProductFields.CHARACTERISTICS)
                            .query(q -> q
                                    .bool(b -> {
                                        b.must(m -> m
                                                .term(t -> t
                                                        .field(ProductFields.Characteristics.ID)
                                                        .value(filter.getId()))
                                        );

                                        for (RangeQuery rangeQuery : rangeList)
                                            b.should(s -> s.range(rangeQuery));

                                        b.minimumShouldMatch("1");
                                        return b;
                                    })))
                    ._toQuery());
        }

        return allFilters;
    }

    public List<Query> buildTextFilters(List<TextFilterDTO> filters, Long characteristicId) {
        List<Query> allFilters = new ArrayList<>();

        for (var filter : filters) {
            if (filter.getId() != null && filter.getId().equals(characteristicId)) {
                continue;
            }

            List<FieldValue> textValueList = new ArrayList<>();
            for (var textValue : filter.getValues()) {
                textValueList.add(FieldValue.of(textValue));
            }

            TermsQueryField termsQuery = TermsQueryField.of(t -> t.value(textValueList));
            if (!textValueList.isEmpty()) {
                allFilters.add(NestedQuery.of(n -> n
                                .path(ProductFields.CHARACTERISTICS)
                                .query(q -> q
                                        .bool(b -> b
                                                .must(m -> m
                                                        .term(t -> t
                                                                .field(ProductFields.Characteristics.ID)
                                                                .value(filter.getId()))
                                                )
                                                .must(m -> m
                                                        .terms(t -> t
                                                                .field(ProductFields.Characteristics.TEXT_VALUE)
                                                                .terms(termsQuery))
                                                )
                                        )))
                        ._toQuery());
            }
        }

        return allFilters;
    }

    public List<Query> buildFiltersFromDTO(FilterSearchRequestDTO request) {
        List<Query> filters = new ArrayList<>();

        //todo: move to controller validator
        if (request.getCategoryId() == null
                && (request.getText() == null || request.getText().isBlank())) {
            throw new RuntimeException("text and category_id are null when searching available filters");
        }

        filters.add(TermQuery.of(t -> t
                        .field(ProductFields.CATEGORY_ID)
                        .value(request.getCategoryId()))
                ._toQuery());

        filters.add(TermQuery.of(t -> t
                        .field(ProductFields.IS_AVAILABLE)
                        .value(true))
                ._toQuery());

        //todo: add filter for priceFrom
        if (request.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field(ProductFields.CURRENT_PRICE)
                            .gte(JsonData.of(request.getPriceFrom()))
                            .lte(JsonData.of(request.getPriceTo())))
                    ._toQuery());
        }

        return filters;
    }

    public List<Query> buildFiltersFromDTO(ProductSearchRequestDTO request) {
        List<Query> filters = new ArrayList<>();

        filters.add(TermQuery.of(t -> t
                        .field(ProductFields.CATEGORY_ID)
                        .value(request.getCategoryId()))
                ._toQuery());

        filters.add(TermQuery.of(t -> t
                        .field(ProductFields.IS_AVAILABLE)
                        .value(true))
                ._toQuery());

        if (request.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field(ProductFields.CURRENT_PRICE)
                            .gte(JsonData.of(request.getPriceFrom()))
                            .lte(JsonData.of(request.getPriceTo())))
                    ._toQuery());
        }

        if (!request.getNumericFilters().isEmpty())
            filters.addAll(buildNumericFilters(request.getNumericFilters(), null));

        if (!request.getTextFilters().isEmpty())
            filters.addAll(buildTextFilters(request.getTextFilters(), null));

        return filters;
    }
}