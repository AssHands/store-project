package com.ak.store.catalogue.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchResult {
    List<Long> ids;
    List<Object> searchAfter;
}