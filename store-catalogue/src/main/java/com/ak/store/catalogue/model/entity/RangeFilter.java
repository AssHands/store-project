package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RangeFilter {
    private Long id;
    private Long categoryId;
    private Integer from;
    private Integer to;
}
