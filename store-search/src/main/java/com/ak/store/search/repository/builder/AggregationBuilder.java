package com.ak.store.search.repository.builder;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.NamedValue;
import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.entity.Characteristic;
import com.ak.store.search.util.ProductFields;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AggregationBuilder {
    private final FilterBuilder filterBuilder;

    public Map<String, Aggregation> buildAggregations(SearchFilterCommand request,
                                                      List<Characteristic> availableCharacteristics) {
        Map<String, Aggregation> aggs = new HashMap<>();

        //todo если так вернуть, то будет ли оно работать?
        if (availableCharacteristics.isEmpty()) {
            return aggs;
        }

        for (var filter : availableCharacteristics) {
            if (filter.getNumericValues().isEmpty() && filter.getTextValues().isEmpty()) {
                throw new RuntimeException("characteristic with id=%d has no filter values".formatted(filter.getId()));
            }

            List<Query> availableFilters = new ArrayList<>();
            if (!request.getTextFilters().isEmpty()) {
                availableFilters.addAll(filterBuilder.buildTextFilters(
                        request.getTextFilters(), filter.getId()));
            }

            if (!request.getNumericFilters().isEmpty()) {
                availableFilters.addAll(filterBuilder.buildNumericFilters(
                        request.getNumericFilters(), filter.getId()));
            }

            if (availableFilters.isEmpty()) {
                aggs.put(filter.getId().toString(), buildNestedAggregation(filter));
            } else {
                aggs.put(filter.getId().toString(),
                        Aggregation.of(a -> {
                            a.filter(f -> f.bool(b -> b.filter(availableFilters)));
                            a.aggregations("1", buildNestedAggregation(filter));
                            return a;
                        })
                );
            }
        }

        return aggs;
    }

    private Aggregation buildNestedAggregation(Characteristic filter) {
        return Aggregation.of(a -> a
                .nested(n -> n.path(ProductFields.CHARACTERISTICS))
                .aggregations("2", fa -> {
                    fa.filter(f -> f
                            .term(t -> t
                                    .field(ProductFields.Characteristics.ID)
                                    .value(filter.getId())
                            )
                    );

                    if (filter.getIsText()) {
                        fa.aggregations("text_values", tv -> tv
                                .terms(t -> t
                                        .field(ProductFields.Characteristics.TEXT_VALUE)
                                        .size(20)
                                        .order(NamedValue.of("_key", SortOrder.Asc))
                                )
                        );
                    } else {
                        fa.aggregations("numeric_values", nv -> nv
                                .range(r -> r
                                        .field(ProductFields.Characteristics.NUMERIC_VALUE)
                                        .ranges(buildAggregationRanges(filter))
                                )
                        );
                    }

                    return fa;
                }));
    }

    private List<AggregationRange> buildAggregationRanges(Characteristic filter) {
        List<AggregationRange> ranges = new ArrayList<>();

        for (var rangeValue : filter.getNumericValues()) {
            ranges.add(AggregationRange.of(ar -> {

                if (rangeValue.getFromValue() != null)
                    ar.from(rangeValue.getFromValue().toString());
                if (rangeValue.getToValue() != null) {
                    int toValue = rangeValue.getToValue() + 1;
                    ar.to(Integer.toString(toValue));
                }

                return ar;
            }));

        }

        return ranges;
    }
}