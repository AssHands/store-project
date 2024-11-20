package com.ak.store.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.dto.search.NumericFilter;
import com.ak.store.common.dto.search.RequestPayload;
import com.ak.store.common.dto.search.TextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EsService {

    private final ElasticsearchClient esClient;

    @Autowired
    public EsService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public void getAllDocument(RequestPayload requestPayload) throws IOException {
        List<Query> filters = new ArrayList<>();

        if(requestPayload.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("price")
                            .gte(JsonData.of(requestPayload.getPriceFrom()))
                            .lte(JsonData.of(requestPayload.getPriceTo())))
                    ._toQuery());
        }

        if(requestPayload.getCategoryId() != null) {
            filters.add(TermQuery.of(t -> t
                            .field("category_id")
                            .value(requestPayload.getCategoryId()))
                    ._toQuery());
        }

        if(requestPayload.getNumericFilters() != null)
            filters.addAll(getNumericFilters(requestPayload.getNumericFilters()));

        if(requestPayload.getTextFilters() != null)
            filters.addAll(getTextFilters(requestPayload.getTextFilters()));

        SearchRequest searchRequest = getSearchRequest(requestPayload,
                getSearchQuery(filters, requestPayload.getText()));

        SearchResponse<ProductDocument> response = esClient.search(searchRequest, ProductDocument.class);

        List<Hit<ProductDocument>> hits = response.hits().hits();
        for (Hit<ProductDocument> hit: hits) {
            System.out.println(hit.source());
        }
    }

    private List<Query> getNumericFilters(List<NumericFilter> numericFilters) {
        List<RangeQuery> rangeList = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        for(var numericFilter : numericFilters) {
            for (var numericValue : numericFilter.getValues()) {
                rangeList.add(RangeQuery.of(r -> r
                        .field("characteristics.numeric_value")
                        .gte(JsonData.of(numericValue.getFrom()))
                        .lte(JsonData.of(numericValue.getTo()))));
            }

            filters.add(NestedQuery.of(n -> n
                    .path("characteristics")
                    .query(q -> q
                            .bool(bool -> {
                                bool.must(m -> m
                                        .term(t -> t
                                                .field("characteristics.characteristic_id")
                                                .value(numericFilter.getCharacteristicId())));

                                for (RangeQuery rangeQuery : rangeList)
                                    bool.should(s -> s.range(rangeQuery));

                                bool.minimumShouldMatch("1");
                                return bool;
                            })))
                    ._toQuery());
        }

        return filters;
    }

    private List<Query> getTextFilters(List<TextFilter> textFilters) {
        List<Query> filters = new ArrayList<>();

        for(var textFilter : textFilters) {
            List<FieldValue> textValueList = new ArrayList<>();
            for(var textValue : textFilter.getValues()) {
                textValueList.add(FieldValue.of(textValue));
            }

            TermsQueryField termsValues = TermsQueryField.of(t -> t.value(textValueList));
            if(!textValueList.isEmpty()) {
                filters.add(NestedQuery.of(n -> n
                                .path("characteristics")
                                .query(q -> q
                                        .bool(b -> b
                                                .must(m -> m
                                                        .term(t -> t
                                                                .field("characteristics.characteristic_id")
                                                                .value(textFilter.getCharacteristicId())))
                                                .must(m -> m
                                                        .terms(t -> t
                                                                .field("characteristics.text_value")
                                                                .terms(termsValues))))))
                        ._toQuery());
            }
        }

        return filters;
    }

    private BoolQuery getSearchQuery(List<Query> filters, String fullTextSearch) {
        MatchQuery matchQuerySearch = MatchQuery.of(m -> m.field("title").query(fullTextSearch));
        if(filters == null || filters.isEmpty()) {
            return BoolQuery.of(b -> b
                    .should(s -> s
                            .match(matchQuerySearch)));
        } else {
            return BoolQuery.of(b -> b
                    .filter(filters)
                    .should(s -> s
                            .match(matchQuerySearch)));
        }
    }

    private SearchRequest getSearchRequest(RequestPayload requestPayload, BoolQuery searchQuery) {
        if (requestPayload.getSearchAfter() == null) {
             return SearchRequest.of(sr -> sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size((requestPayload.getLimit() == 0) ? 20 : requestPayload.getLimit())
                    .sort(s -> s.field(sort -> {
                        if(requestPayload.getSort() == null) {
                            sort.field("amount_sales").order(SortOrder.Desc); //POPULAR
                            return  sort;
                        }

                        switch (requestPayload.getSort()) {
                            case PRICE_UP -> sort.field("price");
                            case PRICE_DOWN -> sort.field("price").order(SortOrder.Desc);
                            case RATING -> sort.field("grade").order(SortOrder.Desc)
                                    .field("amount_reviews").order(SortOrder.Desc);
                            default -> sort.field("amount_sales").order(SortOrder.Desc); //POPULAR
                        }
                        return  sort;
                    })));
        } else {
             return SearchRequest.of(sr -> sr
                    .index("product")
                     .query(q -> q.bool(searchQuery))
                    .size((requestPayload.getLimit() == 0) ? 20 : requestPayload.getLimit())
                    .searchAfter(search -> {
                        for (var obj : requestPayload.getSearchAfter()) {
                            search.anyValue(JsonData.of(obj));
                        }
                        return search;
                    })
                    .sort(s -> s.field(sort -> {
                        if(requestPayload.getSort() == null) {
                            sort.field("amount_sales").order(SortOrder.Desc); //POPULAR
                            return  sort;
                        }

                        switch (requestPayload.getSort()) {
                            case PRICE_UP -> sort.field("price");
                            case PRICE_DOWN -> sort.field("price").order(SortOrder.Desc);
                            case RATING -> sort.field("grade").order(SortOrder.Desc)
                                    .field("amount_reviews").order(SortOrder.Desc);
                            default -> sort.field("amount_sales").order(SortOrder.Desc); //POPULAR
                        }
                        return  sort;
                    })));
        }
    }
}