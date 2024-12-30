package com.ak.store.catalogue.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.NumericFilterValue;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.io.IOException;
import java.util.*;

@Service
public class ElasticService {

    private final ElasticsearchClient esClient;
    private final CharacteristicRepo characteristicRepo;

    @Autowired
    public ElasticService(ElasticsearchClient esClient, CharacteristicRepo characteristicRepo) {
        this.esClient = esClient;
        this.characteristicRepo = characteristicRepo;
    }

    public void createOneProduct(ProductDocument productDocument) throws IOException {
        var request = IndexRequest.of(i -> i
                 .index("product")
                 .id(productDocument.getId().toString())
                 .document(productDocument));

        esClient.index(request);
    }

    public SearchAvailableFiltersResponse searchAvailableFilters(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        var request = SearchRequest.of(sr -> sr
                .index("product")
                .size(0)
                .aggregations(getAggregations(searchAvailableFiltersRequest)));

        SearchResponse<Void> response;
        System.out.println(request);

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("aggs error");
        }
        return new SearchAvailableFiltersResponse(getAvailableFilters(response));
    }

    private Filters getAvailableFilters(SearchResponse<Void> response) {
        Filters filters = new Filters();

        for(var allAggs : response.aggregations().entrySet()) {
            Long characteristicId = Long.parseLong(allAggs.getKey().split("__")[0]);
            String characteristicName = allAggs.getKey().split("__")[1];
            Set <Map.Entry<String, Aggregate>> entrySet = null;

            if(allAggs.getValue().isFilter()) {
                entrySet = allAggs.getValue().filter().aggregations().get("1").nested().
                        aggregations().entrySet().iterator().next().getValue().filter().aggregations().entrySet();
            }

            if(entrySet == null)
                entrySet = allAggs.getValue().nested().aggregations().get("2").filter().aggregations().entrySet();

            for (var entry : entrySet) {
                if(entry.getValue().isRange()) {
                    List<NumericFilterValue> rangeValues = new ArrayList<>();

                    for(var agg : entry.getValue().range().buckets().array()) {
                        if(agg.docCount() == 0) continue;
                        rangeValues.add(NumericFilterValue.builder()
                                .from(agg.from().intValue())
                                .to(agg.to().intValue())
                                .build());
                    }
                    if(rangeValues.isEmpty()) continue;
                    filters.getNumericFilters().add(NumericFilter.builder()
                            .id(characteristicId)
                            .name(characteristicName)
                            .values(rangeValues)
                            .build());
                }
                if(entry.getValue().isSterms()) {
                    List<String> textValues = new ArrayList<>();
                    for(var agg : entry.getValue().sterms().buckets().array()) {
                        textValues.add(agg.key().stringValue());
                    }
                    if(textValues.isEmpty()) continue;
                    filters.getTextFilters().add(TextFilter.builder()
                            .id(characteristicId)
                            .name(characteristicName)
                            .values(textValues)
                            .build());
                }
            }
        }

        return filters;
    }

    private Map<String, Aggregation> getAggregations(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        List<Characteristic> filters = characteristicRepo.findAllValuesByCategoryId(searchAvailableFiltersRequest.getCategoryId());
        Map<String, Aggregation> aggs = new HashMap<>();

        for(var filter : filters) {
            List<Query> availableFilters = new ArrayList<>();
            if(!searchAvailableFiltersRequest.getTextFilters().isEmpty()) {
                availableFilters.addAll(makeTextFilters(
                        searchAvailableFiltersRequest.getTextFilters(), filter.getId()));
            }

            if(!searchAvailableFiltersRequest.getNumericFilters().isEmpty()) {
                availableFilters.addAll(makeNumericFilters(
                        searchAvailableFiltersRequest.getNumericFilters(), filter.getId()));
            }

            if(availableFilters.isEmpty()) {
                aggs.put(filter.getId() + "__" + filter.getName(), makeAggregation(filter));
            } else {
                aggs.put(filter.getId() + "__" + filter.getName(),
                        Aggregation.of(a -> {
                            a.filter(f -> f.bool(b -> b.filter(availableFilters)));
                            a.aggregations("1", makeAggregation(filter));
                            return a;
                        }));
            }
        }

        return aggs;
    }

    private Aggregation makeAggregation(Characteristic filter) {
        return Aggregation.of(a -> a
                .nested(n -> n.path("characteristics"))
                .aggregations("2", fa -> {
                    fa.filter(f -> f
                            .term(t -> t
                                    .field("characteristics.id")
                                    .value(filter.getId())));

                    if(filter.isText()) {
                        fa.aggregations("text_values", tv -> tv
                                .terms(t -> t
                                        .field("characteristics.text_value")
                                        .size(20)
                                        .order(NamedValue.of("_key", SortOrder.Asc))));
                    } else {
                        fa.aggregations("numeric_values", nv -> nv
                                .range(r -> r
                                        .field("characteristics.numeric_value")
                                        .ranges(getAggregationRanges(filter))));
                    }

                    return fa;
                }));
    }

    private List<AggregationRange> getAggregationRanges(Characteristic filter) {
        List<AggregationRange> ranges = new ArrayList<>();

        for(var rangeValue : filter.getRangeValues()) {
            ranges.add(AggregationRange.of(ar -> {

                if(rangeValue.getFromValue() != null)
                    ar.from(rangeValue.getFromValue().toString());
                if(rangeValue.getToValue() != null)
                    ar.to(rangeValue.getToValue().toString());

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
            filters.addAll(makeNumericFilters(productSearchRequest.getNumericFilters(), null));

        if (!productSearchRequest.getTextFilters().isEmpty())
            filters.addAll(makeTextFilters(productSearchRequest.getTextFilters(), null));

        SearchRequest searchRequest = makeSearchRequest(productSearchRequest,
                makeSearchQuery(filters, productSearchRequest.getText()));

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
                .query(q -> q.bool(makeSearchQuery(null, text)))
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

    private BoolQuery makeSearchQuery(List<Query> filters, String fullTextSearch) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty())
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

    private SearchRequest makeSearchRequest(ProductSearchRequest productSearchRequest, BoolQuery searchQuery) {
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

    private List<Query> makeNumericFilters(List<NumericFilter> numericFilters, Long currentCharacteristicId) {
        List<RangeQuery> rangeList = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        for (var numericFilter : numericFilters) {
            if(numericFilter.getId() != null
                    && numericFilter.getId().equals(currentCharacteristicId))
                continue;

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
                                                        .field("characteristics.id")
                                                        .value(numericFilter.getId())));

                                        for (RangeQuery rangeQuery : rangeList)
                                            bool.should(s -> s.range(rangeQuery));

                                        bool.minimumShouldMatch("1");
                                        return bool;
                                    })))
                    ._toQuery());
        }

        return filters;
    }

    private List<Query> makeTextFilters(List<TextFilter> textFilters, Long currentCharacteristicId) {
        List<Query> filters = new ArrayList<>();

        for (var textFilter : textFilters) {
            if(textFilter.getId() != null
                    && textFilter.getId().equals(currentCharacteristicId))
                continue;

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
                                                                .value(textFilter.getId())))
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