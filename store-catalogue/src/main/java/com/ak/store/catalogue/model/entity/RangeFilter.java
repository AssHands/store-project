package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RangeFilter {
    private final Long id;
    private final Long categoryId;
    private final Integer from;
    private final Integer to;
}
