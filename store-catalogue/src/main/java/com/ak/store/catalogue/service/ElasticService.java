package com.ak.store.catalogue.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.RangeFilter;
import com.ak.store.common.dto.search.FacetFilter;
import com.ak.store.common.dto.search.nested.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ElasticService {

    private final ElasticsearchClient esClient;

    @Autowired
    public ElasticService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public AvailableFiltersResponse findAvailableFilters(SearchAvailableFilters searchAvailableFilters) {

        var request = SearchRequest.of(sr -> sr
                .index("product")
                .size(0)
                .query(q -> q.bool(getSearchQuery(null, searchAvailableFilters.getText())))
                .aggregations("test", a -> a
                        .nested(n -> n.path("characteristics"))
                        .aggregations("nested_test", na -> na
                                .terms(t -> t.field("characteristics.characteristic_id").size(100))
                                .aggregations("text_values", text -> text
                                        .terms(te -> te.field("characteristics.text_value").size(20)))
                                .aggregations("numeric_values", numeric -> numeric
                                        .range(r -> r.field("characteristics.numeric_value").ranges(re -> re.from("1").to("16")))))));

        SearchResponse<Void> response;
        System.out.println(request);

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("aggs error");
        }

//        System.out.println(response.aggregations().get("test").nested().aggregations().get("nested_test").sterms().buckets().array().get(0).aggregations().get("text_values").sterms().buckets().array().get(0).key().stringValue());
//
//        System.out.println(response.aggregations().get("test").nested().aggregations().get("nested_test").sterms().buckets().array().get(3).aggregations().get("text_values"));

        var facetFilter = new FacetFilter();
        facetFilter.setCategoryId(3);
        var a = new RangeFilter(1l, 2l, 2, 3);

        for (var element : response.aggregations().get("test").nested().aggregations().get("nested_test").sterms().buckets().array()) {

            List<String> textValues = new ArrayList<>();
            var textFilter = new TextFilter();
            textFilter.setCharacteristicId(Long.parseLong(element.key().stringValue()));

            for(var podElement : element.aggregations().get("text_values").sterms().buckets().array()) {
                if(podElement.docCount() != 0)
                    textValues.add(podElement.key().stringValue());
            }

            textFilter.setValues(textValues);

//            if(!textValues.isEmpty()) {
//                textFilter.setValues(textValues);
//                if(facetFilter.getTextFilters() == null) {
//                    facetFilter.getFilters().setTextFilters(new ArrayList<>(List.of(textFilter)));
//                }
//                else
//                    facetFilter.getTextFilters().add(textFilter);
//            }
        facetFilter.setFilters(new Filters(new ArrayList<>(), new ArrayList<>(List.of(textFilter))));

        }


        System.out.println(facetFilter);

        return null;

//        for (var element : response.aggregations().get("test").nested().aggregations().get("nested_test").sterms().buckets().array()) {
//            for(var podElement : element.aggregations().get("numeric_values").range().buckets().array()) {
//                if(podElement.docCount() != 0)
//                    System.out.println(podElement.key());
//            }
//
//        }

//        for (var e : response.aggregations().get("text_values")) {
//            for (var ee : e.getValue().nested().aggregations().entrySet()) {
//                var l = ee.getValue().sterms().buckets();
//                System.out.println(ee.getValue());
//            }
//        }
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
        } else {
            Long categoryId = defineCategory(productSearchRequest.getText());
            if(categoryId != null) {
                filters.add(TermQuery.of(t -> t
                                .field("category_id")
                                .value(categoryId))
                        ._toQuery());
            }
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

    private Long defineCategory(String text) {
        SearchRequest request = SearchRequest.of(sr -> sr
                .index("product")
                .query(q -> q.bool(getSearchQuery(null, text)))
                .size(0)
                .aggregations("most_frequent_category", agg -> agg
                        .terms(t -> t
                                .field("category_id")
                                .size(1))));

        SearchResponse<Void> response;

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("defineCategory error");
        }

        var mostFrequentCategory = response.aggregations().get("most_frequent_category")
                .lterms().buckets().array();

        if(!mostFrequentCategory.isEmpty()) {
            return Long.parseLong(mostFrequentCategory.get(0).key());
        }

        return null;
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

    private SearchRequest getSearchRequest(SearchAvailableFilters searchAvailableFilters, BoolQuery searchQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size(0)
                    .aggregations("characteristics_aggregation", a -> a
                            .nested(n -> n.path("characteristics"))
                            .aggregations("characteristics_by_id", na -> na
                                    .terms(t -> t.field("characteristics.characteristic_id").size(100))

                                    .aggregations("text_values", text -> text
                                            .terms(te -> te.field("characteristics.text_value").size(20)))

                                    .aggregations("numeric_values", numeric -> numeric
                                            .range(r -> r.field("characteristics.numeric_value")
                                                    .ranges(getRangesByCharacteristic(1L))))));

            return sr;
        });
    }

    private List<AggregationRange> getRangesByCharacteristic(Long characteristicId) {
        return List.of(AggregationRange.of(a -> a.from("1").to("100")));
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