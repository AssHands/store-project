package com.ak.store.search.repository.builder;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonData;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.util.ProductFields;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QueryBuilder {
    @Value("${elasticsearch.indexes.product}")
    private String PRODUCT_INDEX;

    public SearchRequest buildFilterRequest(BoolQuery boolQuery, Map<String, Aggregation> aggs) {
        return SearchRequest.of(sr -> sr
                .index(PRODUCT_INDEX)
                .size(0)
                .query(q -> q.bool(boolQuery))
                .aggregations(aggs));
    }

    public SearchRequest buildProductRequest(SearchProductCommand request, BoolQuery boolQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index(PRODUCT_INDEX)
                    .query(q -> q.bool(boolQuery))
                    .size((request.getLimit() == 0) ? 20 : request.getLimit());

            if (!request.getSearchAfter().isEmpty()) {
                request.getSearchAfter().forEach(val ->
                        sr.searchAfter(sa -> sa.anyValue(JsonData.of(val))));
            }

            switch (request.getSortingType()) {
                case PRICE_UP -> {
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.CURRENT_PRICE)));
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.ID)));
                }
                case PRICE_DOWN -> {
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.CURRENT_PRICE).order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.ID)));
                }
                case RATING -> {
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.GRADE).order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.AMOUNT_REVIEWS).order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.ID)));
                }
                default -> { //POPULAR
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.AMOUNT_REVIEWS).order(SortOrder.Desc)));
                    sr.sort(s -> s.field(sort -> sort.field(ProductFields.ID)));
                }
            }

            return sr;
        });
    }

    public SearchRequest buildDefineCategoryRequest(String fullTextSearch) {
        List<Query> filters = new ArrayList<>();
        filters.add(TermQuery.of(t -> t
                        .field(ProductFields.IS_AVAILABLE)
                        .value(true))
                ._toQuery()
        );

        return SearchRequest.of(sr -> sr
                .index(PRODUCT_INDEX)
                .query(q -> q.bool(buildBoolQuery(filters, fullTextSearch)))
                .size(0)
                .aggregations("most_freq_category", a -> a
                        .terms(t -> t
                                .field(ProductFields.CATEGORY_ID)
                                .size(1)
                        )
                )
        );
    }

    public BoolQuery buildBoolQuery(List<Query> filters, String fullTextSearch) {
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
                                .field(ProductFields.TITLE)
                                .query(word)
                                .fuzziness(getFuzziness(word))
                        )
                );
            }

            bool.minimumShouldMatch(getMinimumShouldMatch(words.length));

            return bool;
        });
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
}
