package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicFilter {
    private Long id;
    private Long characteristicId;
    private Long categoryId;
    private Integer from;
    private Integer to;
    private boolean isTextFilter;
}
