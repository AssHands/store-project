package com.ak.store.catalogue.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.FilterByCharacteristic;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.NumericFilterValue;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticService {

    private final ElasticsearchClient esClient;
    private final ProductDao productDao; //todo: move to main service

    @Autowired
    public ElasticService(ElasticsearchClient esClient, ProductDao productDao) {
        this.esClient = esClient;
        this.productDao = productDao;
    }

    public AvailableFiltersResponse searchAvailableFilters(SearchAvailableFilters searchAvailableFilters) {
        var request = SearchRequest.of(sr -> sr
                .index("product")
                .size(0)
                .query(q -> q.bool(getSearchQuery(null, searchAvailableFilters.getText())))
                .aggregations(getAggregations(searchAvailableFilters.getCategoryId())));

        SearchResponse<Void> response;
        System.out.println(request);

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("aggs error");
        }

        return new AvailableFiltersResponse(getAvailableFilters(response));
    }

    private Filters getAvailableFilters(SearchResponse<Void> response) {
        Filters filters = new Filters();

        for(var entry : response.aggregations().entrySet()) {
            var aggs =  entry.getValue().nested()
                    .aggregations().get("filtered_aggregation").filter().aggregations();

            if(aggs.get("text_values") != null) {
                List<String> textValues = new ArrayList<>();

                for(var textValue : aggs.get("text_values").sterms().buckets().array()) {
                    if(textValue.docCount() > 0)
                        textValues.add(textValue.key().stringValue());
                }

                if(!textValues.isEmpty()) {
                    Long characteristicId = Long.parseLong(entry.getKey().split("__")[0]);
                    String characteristicName = entry.getKey().split("__")[1];
                    filters.getTextFilters().add(new TextFilter(characteristicId, characteristicName, textValues));
                }

            } else if(aggs.get("numeric_values") != null) {
                List<NumericFilterValue> numericValues = new ArrayList<>();

                for(var numericValue : aggs.get("numeric_values").range().buckets().array()) {
                    if(numericValue.docCount() > 0) {
                        var range = new NumericFilterValue();

                        if(numericValue.from() != null)
                            range.setFrom(numericValue.from().intValue());

                        if(numericValue.to() != null)
                            range.setTo(numericValue.to().intValue());

                        numericValues.add(range);
                    }
                }

                if(!numericValues.isEmpty()) {
                    Long characteristicId = Long.parseLong(entry.getKey().split("__")[0]);
                    String characteristicName = entry.getKey().split("__")[1];

                    filters.getNumericFilters().add(new NumericFilter(characteristicId,
                            characteristicName, numericValues));
                }
            }
        }

        return filters;
    }

    private Map<String, Aggregation> getAggregations(Long categoryId) {
        List<FilterByCharacteristic> filters = productDao.findAllCharacteristicFilters(categoryId);
        Map<String, Aggregation> aggs = new HashMap<>();

        for(var filter : filters) {
            if(filter.getTextValue() != null) {
                aggs.put(filter.getCharacteristicId() + "__" + filter.getName(),
                        Aggregation.of(a -> a
                                .nested(n -> n.path("characteristics"))
                                .aggregations("filtered_aggregation", fa -> fa
                                        .filter(f -> f
                                                .term(t -> t
                                                        .field("characteristics.characteristic_id")
                                                        .value(filter.getCharacteristicId().toString())))
                                        .aggregations("text_values", va -> va
                                                .terms(t -> t
                                                        .field("characteristics.text_value")
                                                        .size(20)
                                                        .order(NamedValue.of("_key", SortOrder.Asc)))))));

            } else {
                aggs.put(filter.getCharacteristicId() + "__" + filter.getName(),
                        Aggregation.of(a -> a
                                .nested(n -> n.path("characteristics"))
                                .aggregations("filtered_aggregation", fa -> fa
                                        .filter(f -> f
                                                .term(t -> t
                                                        .field("characteristics.characteristic_id")
                                                        .value(filter.getCharacteristicId().toString())))
                                        .aggregations("numeric_values", va -> va
                                                .range(r -> r
                                                        .field("characteristics.numeric_value")
                                                        .ranges(getAggregationRanges(filter.getCharacteristicId(), filters)))))));
            }
        }

        return aggs;
    }

    private List<AggregationRange> getAggregationRanges(Long characteristicId, List<FilterByCharacteristic> filters) {
        List<AggregationRange> ranges = new ArrayList<>();
        List<FilterByCharacteristic> sameCharacteristicFilters = new ArrayList<>();

        for(var filter : filters) {
            if(filter.getCharacteristicId().longValue() == characteristicId) {
                sameCharacteristicFilters.add(filter);
            }
        }

        for(var filter : sameCharacteristicFilters) {
            ranges.add(AggregationRange.of(ar -> {
                String key = ""; //todo: probably useless

                if(filter.getFromValue() != null) {
                    ar.from(filter.getFromValue().toString());
                    key += filter.getFromValue();
                } else
                    key += "*";

                if(filter.getToValue() != null)  {
                    ar.to(filter.getToValue().toString());
                    key += "-" + filter.getToValue();
                } else
                    key += "-*";

                ar.key(key);
                return ar;
            }));

        }

        return ranges;
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
//        else {
//            Long categoryId = defineCategory(productSearchRequest.getText());
//            if(categoryId != null) {
//                filters.add(TermQuery.of(t -> t
//                                .field("category_id")
//                                .value(categoryId))
//                        ._toQuery());
//            }
//        } todo: need tests

        if (!productSearchRequest.getNumericFilters().isEmpty())
            filters.addAll(getNumericFilters(productSearchRequest.getNumericFilters()));

        if (!productSearchRequest.getTextFilters().isEmpty())
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
                .aggregations("most_frequent_category", a -> a
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
            if (!filters.isEmpty())
                b.filter(filters);

            if(fullTextSearch == null || fullTextSearch.isBlank()) {
                return b;
            }

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

            if (!productSearchRequest.getSearchAfter().isEmpty()) {
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