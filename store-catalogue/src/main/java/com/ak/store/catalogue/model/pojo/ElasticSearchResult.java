package com.ak.store.catalogue.model.pojo;

import com.ak.store.common.dto.search.nested.SortingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchResult {
    List<Long> ids = new ArrayList<>();
    List<Object> searchAfter;
    SortingType sortingType;
}