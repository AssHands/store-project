package com.ak.store.catalogue.integration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.NumericFilterValue;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchClient esClient;
    private final CharacteristicRepo characteristicRepo;

    private final CatalogueMapper catalogueMapper;

    public void deleteOneProduct(Long id) {
        var request = DeleteRequest.of(d -> d
                .index("product")
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("delete document error");
        }
    }

    public void createOneProduct(Product product) {
        ProductDocument productDocument = catalogueMapper.mapToProductDocument(product);
        var request = IndexRequest.of(i -> i
                 .index("product")
                 .id(productDocument.getId().toString())
                 .document(productDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("index document error");
        }
    }

    public void createAllProduct(List<Product> productList) {
        var br = new BulkRequest.Builder();
        List<ProductDocument> productDocumentList = productList.stream()
                .map(catalogueMapper::mapToProductDocument)
                .toList();

        productDocumentList.forEach(product ->
                br.operations(op -> op
                        .index(idx -> idx
                                .index("product")
                                .id(product.getId().toString())
                                .document(product))));

        BulkResponse result = null;
        
        try {
            result = esClient.bulk(br.build());
        } catch (Exception e) {
            throw new RuntimeException("bulk document error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("bulk had errors\n" + item.error().reason());
                }
            }
        }
    }

    public void updateOneProduct(Product product) {
        ProductDocument productDocument = catalogueMapper.mapToProductDocument(product);

        var request = UpdateRequest.of(u -> u
                .index("product")
                .id(productDocument.getId().toString())
                .doc(productDocument));

        try {
            esClient.update(request, ProductDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("update document error");
        }
    }

    public SearchAvailableFiltersResponse searchAvailableFilters(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        List<Query> filters = new ArrayList<>();

        if(searchAvailableFiltersRequest.getCategoryId() == null
                && (searchAvailableFiltersRequest.getText() == null || searchAvailableFiltersRequest.getText().isBlank())) {
            throw new RuntimeException("text and category_id are null when searching available filters");
            //todo: move to controller validator
        }

        if(searchAvailableFiltersRequest.getCategoryId() == null) {
            //todo: catch error and send empty filters
            searchAvailableFiltersRequest.setCategoryId(defineCategory(searchAvailableFiltersRequest.getText()));
        }

        filters.add(TermQuery.of(t -> t
                        .field("category_id")
                        .value(searchAvailableFiltersRequest.getCategoryId()))
                ._toQuery());

        if (searchAvailableFiltersRequest.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("current_price")
                            .gte(JsonData.of(searchAvailableFiltersRequest.getPriceFrom()))
                            .lte(JsonData.of(searchAvailableFiltersRequest.getPriceTo())))
                    ._toQuery());
        }

        var request = SearchRequest.of(sr -> sr
                .index("product")
                .size(0)
                .query(makeSearchQuery(filters, searchAvailableFiltersRequest.getText())._toQuery())
                .aggregations(buildAggregations(searchAvailableFiltersRequest)));

        SearchResponse<Void> response;
        System.out.println(request);

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("aggs error");
        }

        return SearchAvailableFiltersResponse.builder()
                .filters(transformSearchFiltersResponseToFiltersDTO(response))
                .categoryId(searchAvailableFiltersRequest.getCategoryId())
                .build();
    }

    //todo: move to utils class?
    private Filters transformSearchFiltersResponseToFiltersDTO(SearchResponse<Void> response) {
        Filters filters = new Filters();

        for(var allAggs : response.aggregations().entrySet()) {
            Long characteristicId = Long.parseLong(allAggs.getKey().split("__")[0]);
            String characteristicName = allAggs.getKey().split("__")[1];
            Set <Map.Entry<String, Aggregate>> entrySet = null;

            if(allAggs.getValue().isFilter()) {
                entrySet = allAggs.getValue().filter().aggregations().get("1").nested()
                        .aggregations().entrySet().iterator().next().getValue().filter().aggregations().entrySet();
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

    private Map<String, Aggregation> buildAggregations(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        List<Characteristic> filters = 
                characteristicRepo.findAllWithAllValuesByCategoryId(searchAvailableFiltersRequest.getCategoryId());
        Map<String, Aggregation> aggs = new HashMap<>();

        for(var filter : filters) {
            if(filter.getRangeValues().isEmpty() && filter.getTextValues().isEmpty()) {
                throw new RuntimeException("characteristic with id=%d has no filter values".formatted(filter.getId()));
            }

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
                aggs.put(filter.getId() + "__" + filter.getName(), makeNestedAggregation(filter));
            } else {
                aggs.put(filter.getId() + "__" + filter.getName(),
                        Aggregation.of(a -> {
                            a.filter(f -> f.bool(b -> b.filter(availableFilters)));
                            a.aggregations("1", makeNestedAggregation(filter));
                            return a;
                        }));
            }
        }

        return aggs;
    }

    private Aggregation makeNestedAggregation(Characteristic filter) {
        return Aggregation.of(a -> a
                .nested(n -> n.path("characteristics"))
                .aggregations("2", fa -> {
                    fa.filter(f -> f
                            .term(t -> t
                                    .field("characteristics.id")
                                    .value(filter.getId())));

                    if(filter.getIsText()) {
                        fa.aggregations("text_values", tv -> tv
                                .terms(t -> t
                                        .field("characteristics.text_value")
                                        .size(20)
                                        .order(NamedValue.of("_key", SortOrder.Asc))));
                    } else {
                        fa.aggregations("numeric_values", nv -> nv
                                .range(r -> r
                                        .field("characteristics.numeric_value")
                                        .ranges(makeAggregationRanges(filter))));
                    }

                    return fa;
                }));
    }

    private List<AggregationRange> makeAggregationRanges(Characteristic filter) {
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

    public ElasticSearchResult findAllProduct(SearchProductRequest searchProductRequest) {
        List<Query> filters = new ArrayList<>();

        if (searchProductRequest.getPriceTo() != 0) {
            filters.add(RangeQuery.of(r -> r
                            .field("current_price")
                            .gte(JsonData.of(searchProductRequest.getPriceFrom()))
                            .lte(JsonData.of(searchProductRequest.getPriceTo())))
                    ._toQuery());
        }

        if (searchProductRequest.getCategoryId() != null) {
            filters.add(TermQuery.of(t -> t
                            .field("category_id")
                            .value(searchProductRequest.getCategoryId()))
                    ._toQuery());
        }

        if (!searchProductRequest.getNumericFilters().isEmpty())
            filters.addAll(makeNumericFilters(searchProductRequest.getNumericFilters(), null));

        if (!searchProductRequest.getTextFilters().isEmpty())
            filters.addAll(makeTextFilters(searchProductRequest.getTextFilters(), null));

        SearchRequest searchRequest = BuildSearchRequest(searchProductRequest,
                makeSearchQuery(filters, searchProductRequest.getText()));

        SearchResponse<ProductDocument> response;

        try {
            response = esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get response from ElasticSearch server");
        }

        List<Hit<ProductDocument>> productHits = response.hits().hits();

        System.out.println(searchRequest);
        response.hits().hits().forEach(System.out::println);

        ElasticSearchResult elasticSearchResult = new ElasticSearchResult();
        elasticSearchResult.setSort(searchProductRequest.getSort());

        if(productHits.size() == 0) {
            return elasticSearchResult;
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

    private Long defineCategory(String fullTextSearch) {
        SearchRequest request = SearchRequest.of(sr -> sr
                .index("product")
                .query(q -> q.bool(makeSearchQuery(null, fullTextSearch)))
                .size(0)
                .aggregations("most_frequent_category", a -> a
                        .terms(t -> t
                                .field("category_id")
                                .size(1))));

        System.out.println(request);
        SearchResponse<Void> response;

        try {
            response = esClient.search(request, void.class);
        } catch (IOException e) {
            throw new RuntimeException("defineCategory error");
        }

        var mostFrequentCategory = response.aggregations().get("most_frequent_category")
                .sterms().buckets().array();

        try {
            return Long.parseLong(mostFrequentCategory.get(0).key().stringValue());
        } catch (Exception e) {
            throw new RuntimeException("can not define category by text");
        }
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

    private SearchRequest BuildSearchRequest(SearchProductRequest searchProductRequest, BoolQuery searchQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size((searchProductRequest.getLimit() == 0) ? 20 : searchProductRequest.getLimit());

            if (!searchProductRequest.getSearchAfter().isEmpty()) {
                searchProductRequest.getSearchAfter().forEach(val ->
                        sr.searchAfter(sa -> sa.anyValue(JsonData.of(val))));
            }

            switch (searchProductRequest.getSort()) {
                case PRICE_UP -> {
                    sr.sort(s -> s.field(sort -> sort.field("current_price")));
                }
                case PRICE_DOWN -> {
                    sr.sort(s -> s.field(sort -> sort.field("current_price").order(SortOrder.Desc)));
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