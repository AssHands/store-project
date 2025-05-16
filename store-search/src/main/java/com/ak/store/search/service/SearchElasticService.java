package com.ak.store.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.ak.store.common.model.catalogue.document.ProductDocument;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.NumericFilterValue;
import com.ak.store.common.model.search.common.TextFilter;
import com.ak.store.common.model.search.view.FiltersView;
import com.ak.store.common.payload.search.FilterSearchRequest;
import com.ak.store.common.payload.search.FilterSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.search.repo.CharacteristicRedisRepo;
import com.ak.store.search.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SearchElasticService {
    private final ElasticsearchClient esClient;

    //todo: make service
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final ProductMapper productMapper;

    public FilterSearchResponse searchAllFilter(FilterSearchRequest filterSearchRequest) {
        List<Query> filters = makeFiltersForFilterSearch(filterSearchRequest);

        var response = sendRequest(
                buildFilterSearchRequest(
                        buildBoolQuery(filters, filterSearchRequest.getText()),
                        buildAggregations(filterSearchRequest)
                )
        );

        return FilterSearchResponse.builder()
                .filters(getFiltersView(response))
                .categoryId(filterSearchRequest.getCategoryId())
                .build();
    }

    //todo: move to utils class?
    private FiltersView getFiltersView(SearchResponse<ProductDocument> response) {
        FiltersView filtersView = new FiltersView();

        for (var allAggs : response.aggregations().entrySet()) {
            Long characteristicId = Long.parseLong(allAggs.getKey().split("__")[0]);
            String characteristicName = allAggs.getKey().split("__")[1];
            Set<Map.Entry<String, Aggregate>> entrySet = null;

            if (allAggs.getValue().isFilter()) {
                entrySet = allAggs.getValue().filter().aggregations().get("1").nested()
                        .aggregations().entrySet().iterator().next().getValue().filter().aggregations().entrySet();
            }

            if (entrySet == null)
                entrySet = allAggs.getValue().nested().aggregations().get("2").filter().aggregations().entrySet();

            for (var entry : entrySet) {
                if (entry.getValue().isRange()) {
                    List<NumericFilterValue> rangeValues = new ArrayList<>();

                    for (var agg : entry.getValue().range().buckets().array()) {
                        if (agg.docCount() == 0) continue;
                        rangeValues.add(NumericFilterValue.builder()
                                .from(agg.from().intValue())
                                .to(agg.to().intValue() - 1)
                                .build());
                    }

                    if (rangeValues.isEmpty()) continue;
                    filtersView.getNumericFilters().add(NumericFilter.builder()
                            .id(characteristicId)
                            .name(characteristicName)
                            .values(rangeValues)
                            .build());
                }

                if (entry.getValue().isSterms()) {
                    List<String> textValues = new ArrayList<>();
                    for (var agg : entry.getValue().sterms().buckets().array()) {
                        textValues.add(agg.key().stringValue());
                    }

                    if (textValues.isEmpty()) continue;
                    filtersView.getTextFilters().add(TextFilter.builder()
                            .id(characteristicId)
                            .name(characteristicName)
                            .values(textValues)
                            .build());
                }
            }
        }

        return filtersView;
    }

    private Map<String, Aggregation> buildAggregations(FilterSearchRequest filterSearchRequest) {
        Map<String, Aggregation> aggs = new HashMap<>();

        //todo: search in other place or throw exception
        List<CharacteristicDocument> filters = characteristicRedisRepo
                .findAllCharacteristicByCategoryId(filterSearchRequest.getCategoryId());

        if(filters.isEmpty()) {
            return aggs;
        }

        for (var filter : filters) {
            if (filter.getRangeValues().isEmpty() && filter.getTextValues().isEmpty()) {
                throw new RuntimeException("characteristic with id=%d has no filter values".formatted(filter.getId()));
            }

            List<Query> availableFilters = new ArrayList<>();
            if (!filterSearchRequest.getTextFilters().isEmpty()) {
                availableFilters.addAll(buildTextFilters(
                        filterSearchRequest.getTextFilters(), filter.getId()));
            }

            if (!filterSearchRequest.getNumericFilters().isEmpty()) {
                availableFilters.addAll(buildNumericFilters(
                        filterSearchRequest.getNumericFilters(), filter.getId()));
            }

            if (availableFilters.isEmpty()) {
                aggs.put(filter.getId() + "__" + filter.getName(), buildNestedAggregation(filter));
            } else {
                aggs.put(filter.getId() + "__" + filter.getName(),
                        Aggregation.of(a -> {
                            a.filter(f -> f.bool(b -> b.filter(availableFilters)));
                            a.aggregations("1", buildNestedAggregation(filter));
                            return a;
                        }));
            }
        }

        return aggs;
    }

    private Aggregation buildNestedAggregation(CharacteristicDocument filter) {
        return Aggregation.of(a -> a
                .nested(n -> n.path("characteristics"))
                .aggregations("2", fa -> {
                    fa.filter(f -> f
                            .term(t -> t
                                    .field("characteristics.id")
                                    .value(filter.getId())
                            )
                    );

                    if (filter.getIsText()) {
                        fa.aggregations("text_values", tv -> tv
                                .terms(t -> t
                                        .field("characteristics.text_value")
                                        .size(20)
                                        .order(NamedValue.of("_key", SortOrder.Asc))
                                )
                        );
                    } else {
                        fa.aggregations("numeric_values", nv -> nv
                                .range(r -> r
                                        .field("characteristics.numeric_value")
                                        .ranges(buildAggregationRanges(filter))
                                )
                        );
                    }

                    return fa;
                }));
    }

    private List<AggregationRange> buildAggregationRanges(CharacteristicDocument filter) {
        List<AggregationRange> ranges = new ArrayList<>();

        for (var rangeValue : filter.getRangeValues()) {
            ranges.add(AggregationRange.of(ar -> {

                if (rangeValue.getFrom() != null)
                    ar.from(rangeValue.getFrom().toString());
                if (rangeValue.getTo() != null) {
                    Integer toValue = rangeValue.getTo() + 1;
                    ar.to(toValue.toString());
                }

                return ar;
            }));

        }

        return ranges;
    }

    public ProductSearchResponse searchAllProduct(ProductSearchRequest productSearchRequest) {
        List<Query> filters = makeFiltersForProductSearch(productSearchRequest);

        var response = sendRequest(
                buildProductSearchRequest(
                        productSearchRequest,
                        buildBoolQuery(filters, productSearchRequest.getText())
                )
        );

        return ProductSearchResponse.builder()
                .content(getContent(response))
                .searchAfter(getSearchAfter(response))
                .build();
    }

    private List<ProductPoorView> getContent(SearchResponse<ProductDocument> searchResponse) {
        return searchResponse.hits().hits()
                .stream()
                .filter(doc -> doc.source() != null)
                .map(Hit::source)
                .map(productMapper::toProductPoorView)
                .toList();
    }

    private List<Object> getSearchAfter(SearchResponse<ProductDocument> searchResponse) {
        var hits = searchResponse.hits().hits();

        if(hits.isEmpty()) {
            return Collections.emptyList();
        }

        return hits.get(hits.size() - 1).sort()
                .stream()
                .map(FieldValue::_get)
                .toList();
    }

    private List<Query> makeFiltersForFilterSearch(FilterSearchRequest filterSearchRequest) {
        List<Query> filters = new ArrayList<>();

        if (filterSearchRequest.getCategoryId() == null
                && (filterSearchRequest.getText() == null || filterSearchRequest.getText().isBlank())) {
            throw new RuntimeException("text and category_id are null when searching available filters");
            //todo: move to controller validator
        }

        if (filterSearchRequest.getCategoryId() == null) {
            //todo: catch error and send empty filters
            filterSearchRequest.setCategoryId(defineCategory(filterSearchRequest.getText()));
        }

        filters.add(TermQuery.of(t -> t
                        .field("category_id")
                        .value(filterSearchRequest.getCategoryId()))
                ._toQuery());

        filters.add(TermQuery.of(t -> t
                        .field("is_available")
                        .value(true))
                ._toQuery());

        //todo: add filter for priceFrom
        if (filterSearchRequest.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("current_price")
                            .gte(JsonData.of(filterSearchRequest.getPriceFrom()))
                            .lte(JsonData.of(filterSearchRequest.getPriceTo())))
                    ._toQuery());
        }

        return filters;
    }

    private List<Query> makeFiltersForProductSearch(ProductSearchRequest productSearchRequest) {
        List<Query> filters = new ArrayList<>();

        Long categoryId = productSearchRequest.getCategoryId() != null ?
                productSearchRequest.getCategoryId() : defineCategory(productSearchRequest.getText());

        filters.add(TermQuery.of(t -> t
                        .field("category_id")
                        .value(categoryId))
                ._toQuery());

        filters.add(TermQuery.of(t -> t
                        .field("is_available")
                        .value(true))
                ._toQuery());

        if (productSearchRequest.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("current_price")
                            .gte(JsonData.of(productSearchRequest.getPriceFrom()))
                            .lte(JsonData.of(productSearchRequest.getPriceTo())))
                    ._toQuery());
        }

        if (!productSearchRequest.getNumericFilters().isEmpty())
            filters.addAll(buildNumericFilters(productSearchRequest.getNumericFilters(), null));

        if (!productSearchRequest.getTextFilters().isEmpty())
            filters.addAll(buildTextFilters(productSearchRequest.getTextFilters(), null));

        return filters;
    }

    private Long defineCategory(String fullTextSearch) {
        final String aggName = "most_frequent_category";

        var mostFrequentCategory = sendRequest(
                buildCategoryDefineRequest(fullTextSearch, aggName)
        ).aggregations().get(aggName).sterms().buckets().array();

        try {
            return Long.parseLong(mostFrequentCategory.get(0).key().stringValue());
        } catch (Exception e) {
            throw new RuntimeException("can not define category by text");
        }
    }

    private SearchRequest buildCategoryDefineRequest(String fullTextSearch, String aggName) {
        List<Query> filters = new ArrayList<>();
        filters.add(TermQuery.of(t -> t
                        .field("is_available")
                        .value(true))
                ._toQuery());

        return SearchRequest.of(sr -> sr
                .index("product")
                .query(q -> q.bool(buildBoolQuery(filters, fullTextSearch)))
                .size(0)
                .aggregations(aggName, a -> a
                        .terms(t -> t
                                .field("category_id")
                                .size(1))
                )
        );
    }

    private String getFuzziness(String word) {
        int size = word.length();

        if (size <= 2) return "0";
        if (size <= 4) return "1";
        if (size <= 7) return "2";

        return "3";
    }

    private String getMinimumShouldMatch(int amountWords) {
        if (amountWords <= 3) return "1";
        if (amountWords <= 4) return "2";

        return "3";
    }

    private BoolQuery buildBoolQuery(List<Query> filters, String fullTextSearch) {
        return BoolQuery.of(bool -> {
            if (filters != null && !filters.isEmpty()) {
                bool.filter(filters);
            }

            if (fullTextSearch == null || fullTextSearch.isBlank()) {
                return bool;
            }

            String[] words = Arrays.stream(fullTextSearch.split(" "))
                    .map(word -> word.replaceAll("[^A-Za-zА-Яа-я0-9]", ""))
                    .toArray(String[]::new);

            for (String word : words) {
                bool.should(s -> s
                        .match(ma -> ma
                                .field("title")
                                .query(word)
                                .fuzziness(getFuzziness(word))));
            }

            bool.minimumShouldMatch(getMinimumShouldMatch(words.length));

            return bool;
        });
    }

    private SearchResponse<ProductDocument> sendRequest(SearchRequest searchRequest) {
        try {
            return esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("send request error");
        }
    }

    private SearchRequest buildFilterSearchRequest(BoolQuery boolQuery, Map<String, Aggregation> aggs) {
        return SearchRequest.of(sr -> sr
                .index("product")
                .size(0)
                .query(q -> q.bool(boolQuery))
                .aggregations(aggs));
    }

    private SearchRequest buildProductSearchRequest(ProductSearchRequest productSearchRequest, BoolQuery boolQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(boolQuery))
                    .size((productSearchRequest.getLimit() == 0) ? 20 : productSearchRequest.getLimit());

            if (!productSearchRequest.getSearchAfter().isEmpty()) {
                productSearchRequest.getSearchAfter().forEach(val ->
                        sr.searchAfter(sa -> sa.anyValue(JsonData.of(val))));
            }

            switch (productSearchRequest.getSortingType()) {
                case PRICE_UP -> {
                    sr.sort(s -> s.field(sort -> sort.field("current_price")));
                    sr.sort(s -> s.field(sort -> sort.field("id")));
                }
                case PRICE_DOWN -> {
                    sr.sort(s -> s.field(sort -> sort.field("current_price").order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field("id")));
                }
                case RATING -> {
                    sr.sort(s -> s.field(sort -> sort.field("grade").order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field("amount_reviews").order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field("id")));
                }
                default -> { //POPULAR
                    sr.sort(s -> s.field(sort -> sort.field("amount_reviews").order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field("id")));
                }
            }

            return sr;
        });
    }

    private List<Query> buildNumericFilters(List<NumericFilter> numericFilters, Long characteristicId) {
        List<Query> filters = new ArrayList<>();

        for (var numericFilter : numericFilters) {
            List<RangeQuery> rangeList = new ArrayList<>();

            if (numericFilter.getId() != null && numericFilter.getId().equals(characteristicId)) {
                continue;
            }

            for (var numericValue : numericFilter.getValues()) {
                rangeList.add(RangeQuery.of(r -> r
                        .field("characteristics.numeric_value")
                        .gte(JsonData.of(numericValue.getFrom()))
                        .lte(JsonData.of(numericValue.getTo()))));
            }

            filters.add(NestedQuery.of(n -> n
                            .path("characteristics")
                            .query(q -> q
                                    .bool(b -> {
                                        b.must(m -> m
                                                .term(t -> t
                                                        .field("characteristics.id")
                                                        .value(numericFilter.getId()))
                                        );

                                        for (RangeQuery rangeQuery : rangeList)
                                            b.should(s -> s.range(rangeQuery));

                                        b.minimumShouldMatch("1");
                                        return b;
                                    })))
                    ._toQuery());
        }

        return filters;
    }

    private List<Query> buildTextFilters(List<TextFilter> textFilters, Long characteristicId) {
        List<Query> filters = new ArrayList<>();

        for (var textFilter : textFilters) {
            if (textFilter.getId() != null && textFilter.getId().equals(characteristicId)) {
                continue;
            }

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
                                                                .field("characteristics.id")
                                                                .value(textFilter.getId()))
                                                )
                                                .must(m -> m
                                                        .terms(t -> t
                                                                .field("characteristics.text_value")
                                                                .terms(termsQuery))
                                                )
                                        )))
                        ._toQuery());
            }
        }

        return filters;
    }
}