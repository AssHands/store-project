package com.ak.store.catalogue.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.common.payload.search.nested.NumericFilter;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.nested.TextFilter;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EsService {

    private final ElasticsearchClient esClient;

    @Autowired
    public EsService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public ElasticSearchResult findAll(ProductSearchRequest productSearchRequest) {
        List<Query> filters = new ArrayList<>();

        if (productSearchRequest.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("price")
                            .gte(JsonData.of(productSearchRequest.getPriceFrom()))
                            .lte(JsonData.of(productSearchRequest.getPriceTo())))
                    ._toQuery());
        }

        if (productSearchRequest.getCategoryId() != null) {
            filters.add(TermQuery.of(t -> t
                            .field("category_id")
                            .value(productSearchRequest.getCategoryId()))
                    ._toQuery());
        }

        if (productSearchRequest.getNumericFilters() != null)
            filters.addAll(getNumericFilters(productSearchRequest.getNumericFilters()));

        if (productSearchRequest.getTextFilters() != null)
            filters.addAll(getTextFilters(productSearchRequest.getTextFilters()));

        SearchRequest searchRequest = getSearchRequest(productSearchRequest,
                getSearchQuery(filters, productSearchRequest.getText()));

        SearchResponse<ProductDocument> response;

        try {
            response = esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get response from ElasticSearch server");
        }

        ElasticSearchResult elasticSearchResult = new ElasticSearchResult();
        List<Hit<ProductDocument>> productHits = response.hits().hits();

        System.out.println(searchRequest);
        response.hits().hits().forEach(System.out::println);

        if(productHits.size() == 0) {
            return null;
        }

        elasticSearchResult.setIds(productHits
                .stream()
                .filter(doc -> doc.source() != null)
                .map(Hit::source)
                .map(ProductDocument::getId)
                .toList());

        elasticSearchResult.setSearchAfter(
                productHits.get(productHits.size() - 1).sort()
                        .stream()
                        .map(FieldValue::_get)
                        .toList());


        return elasticSearchResult;
    }

    private String getFuzziness(String word) {
        int size = word.length();

        if(size <= 2) return "0";
        if(size <= 4) return "1";
        if(size <= 7) return "2";

        return "3";
    }

    private String getMinimumShouldMatch(int amountWords) {
        if(amountWords <= 3) return "1";
        if(amountWords <= 4) return "2";

        return "3";
    }

    private BoolQuery getSearchQuery(List<Query> filters, String fullTextSearch) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty())
                b.filter(filters);

            String[] words = Arrays.stream(fullTextSearch.split(" "))
                    .map(word -> word.replaceAll("[^A-Za-zА-Яа-я0-9]", ""))
                    .toArray(String[]::new);

            for(String word : words) {
                b.should(s -> s
                        .match(ma -> ma
                                .field("title")
                                .query(word)
                                .fuzziness(getFuzziness(word))));
            }

            b.minimumShouldMatch(getMinimumShouldMatch(words.length));

            return b;
        });
    }

    private SearchRequest getSearchRequest(ProductSearchRequest productSearchRequest, BoolQuery searchQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size((productSearchRequest.getLimit() == 0) ? 20 : productSearchRequest.getLimit());

            if (productSearchRequest.getSearchAfter() != null && !productSearchRequest.getSearchAfter().isEmpty()) {
                productSearchRequest.getSearchAfter().forEach(val ->
                        sr.searchAfter(sa -> sa.anyValue(JsonData.of(val))));
            }

            switch (productSearchRequest.getSort()) {
                case PRICE_UP -> {
                    sr.sort(s -> s.field(sort -> sort.field("price")));
                }
                case PRICE_DOWN -> {
                    sr.sort(s -> s.field(sort -> sort.field("price").order(SortOrder.Desc)));
                }
                case RATING -> {
                    sr.sort(s -> s.field(sort -> sort.field("grade").order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field("amount_reviews").order(SortOrder.Desc)));
                }
                default -> { //POPULAR
                    sr.sort(s -> s.field(sort -> sort.field("amount_reviews").order(SortOrder.Desc)));
                }
            }

            return sr;
        });
    }

    private List<Query> getNumericFilters(List<NumericFilter> numericFilters) {
        List<RangeQuery> rangeList = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        for (var numericFilter : numericFilters) {
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

        for (var textFilter : textFilters) {
            List<FieldValue> textValueList = new ArrayList<>();
            for (var textValue : textFilter.getValues()) {
                textValueList.add(FieldValue.of(textValue));
            }

            TermsQueryField termsQuery = TermsQueryField.of(t -> t.value(textValueList));
            if (!textValueList.isEmpty()) {
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
                                                                .terms(termsQuery))))))
                        ._toQuery());
            }
        }

        return filters;
    }
}